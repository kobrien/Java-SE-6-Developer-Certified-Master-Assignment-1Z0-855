package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * {@code DBFileManager} is responsible for reading and writing records to the
 * database file. It is instantiated and should only be used by the {@code Data}
 * class. All records are read and converted to {@code Subcontractor} objects
 * and then returned to the {@Data} class to be loaded into cache at
 * startup. Likewise this class is capable of taking a subcontractor object and
 * writing it to disk. This class uses a hashmap to maintain a mapping of record
 * numbers to their location in the database file. Using a RandomAccessFile this
 * allows us to quickly seek to that record location.
 *
 * @author Kieran O'Brien
 *
 */
public class DBFileReader {
    private final static Logger LOGGER = Logger.getLogger(DBFileReader.class
	    .getName());

    private static int MAGIC_COOKIE_VALUE;
    private static int NUMBER_FIELDS_PER_RECORD_VALUE;
    private static int FIRST_RECORD_LOCATION_VALUE;

    private static RandomAccessFile databaseFile = null;

    private static final String FILE_ACCESS_MODE = "rw";

    /**
     * This method is called by the Data class constructor passing it the
     * absolute file path to the database. It creates a connection to the
     * database file and holds a reference to it for all other static methods in
     * this class to use. This is done only once and every subsequent call is
     * ignored.
     *
     * @param databasePath
     *            The full absolute file path to the database file
     *
     * @throws DatabaseException
     */
    public static void initializeConnection(final String databasePath)
	    throws DatabaseException {
	// Only initialize if the database file is null
	if (databaseFile == null) {
	    try {
		LOGGER.info("Creating connection to database file "
			+ databasePath);
		databaseFile = new RandomAccessFile(databasePath,
			FILE_ACCESS_MODE);

		readDatabaseHeaderValues();

		verifyValidDatabase();

	    } catch (final FileNotFoundException exception) {
		throw new DatabaseException(
			"The path to the database file does not exist.");
	    }
	}
    }

    /**
     * Compares the magic cookie value stored in the database header with the
     * expected value that we have previously stored and throws a Database
     * exception if they do not match.
     *
     * @throws DatabaseException
     *
     */
    private static void verifyValidDatabase() throws DatabaseException {
	LOGGER.info("Verifying database is valid based on magic cookie value");

	if (MAGIC_COOKIE_VALUE != DatabaseConstants.EXPECTED_MAGIC_COOKIE_VALUE) {
	    throw new DatabaseException(
		    "Database file is not a valid database - magic cookie values do not match");
	}
    }

    /**
     * Starting at record zero, iterates over each data record and converts it
     * to a Subcontractor object.
     *
     * @return The list of subcontractor objects
     * @throws DatabaseException
     */
    public static List<Subcontractor> getAllSubcontractors()
	    throws DatabaseException {
	LOGGER.info("Reading all subcontractor records from database");
	final List<Subcontractor> subcontractors = new ArrayList<Subcontractor>();

	try {
	    final long databaseLength = databaseFile.length();
	    final long totalRecordLength = DatabaseConstants.TOTAL_RECORD_LENGTH_BYTES;

	    // Starting at record location zero, Iterate over each record
	    // creating subcontractor objects for each one until we reach the
	    // end of the file
	    for (long fileOffset = FIRST_RECORD_LOCATION_VALUE; fileOffset < databaseLength; fileOffset += totalRecordLength) {
		final Subcontractor subcontractor = readSubcontractorRecord(fileOffset);

		if (subcontractor != null) {
		    subcontractors.add(subcontractor);
		}
	    }

	} catch (final IOException e) {
	    throw new DatabaseException(e.getMessage());
	}

	return subcontractors;

    }

    /**
     * Reads the database header values according to the length of bytes
     * specified in the database schema and stores the values statically
     *
     * @throws DatabaseException
     */
    private static void readDatabaseHeaderValues() throws DatabaseException {

	try {
	    LOGGER.info("Reading database header values");
	    databaseFile.seek(0);

	    final byte[] magicCookieValue = new byte[DatabaseConstants.MAGIC_COOKIE_VALUE_BYTES];
	    databaseFile.readFully(magicCookieValue);
	    MAGIC_COOKIE_VALUE = getIntValueFromByteArray(magicCookieValue);

	    final byte[] offsetStartRecordZero = new byte[DatabaseConstants.OFFSET_TO_START_RECORD_ZERO_VALUE_BYTES];
	    databaseFile.readFully(offsetStartRecordZero);
	    FIRST_RECORD_LOCATION_VALUE = getIntValueFromByteArray(offsetStartRecordZero);

	    final byte[] numberFieldsPerRecordValue = new byte[DatabaseConstants.NUMBER_OF_FIELDS_PER_RECORD_VALUE_BYTES];
	    databaseFile.readFully(magicCookieValue);
	    NUMBER_FIELDS_PER_RECORD_VALUE = getIntValueFromByteArray(numberFieldsPerRecordValue);

	} catch (final IOException e) {
	    throw new DatabaseException(e.getMessage());
	}

    }

    /**
     * Seeks to a specific record location offset and reads the data record in
     * bytes, if the record is valid based on the initial byte value it then
     * converts the byte fields to strings and creates a subcontractor object,
     * otherwise returns null if the record is invalid.
     *
     * @param fileOffset
     * @return The Subcontractor object
     * @throws DatabaseException
     */
    private static Subcontractor readSubcontractorRecord(final long fileOffset)
	    throws DatabaseException {

	LOGGER.info("Seeking to file offset " + fileOffset
		+ " to read single subcontractor record");

	final byte[] validRecordValue = new byte[DatabaseConstants.VALID_RECORD];
	final byte[] recordValues = new byte[DatabaseConstants.CONTRACTOR_RECORD_LENGTH_BYTES];

	// Read the byte values from file
	synchronized (databaseFile) {
	    try {
		// Update file pointer to the the file location we want to read
		databaseFile.seek(fileOffset);

		databaseFile.readFully(validRecordValue);

		databaseFile.readFully(recordValues);
	    } catch (final IOException e) {
		throw new DatabaseException(e.getMessage());
	    }
	}

	// Only create a subcontractor object if the record is valid
	final int isValidRecord = getIntValueFromByteArray(validRecordValue);

	Subcontractor subcontractor;

	if (isValidRecord == DatabaseConstants.VALID_RECORD) {
	    LOGGER.info("Valid record! Converting record values and creating subcontractor object");
	    subcontractor = createSubcontractorFromByteArrayValues(recordValues);
	} else {
	    LOGGER.info("Invalid Record! Returning null");
	    subcontractor = null;
	}

	return subcontractor;

    }

    /**
     * Creates a subcontractor object with values for each of it's fields given
     * the array of bytes read from the database record. The length of bytes to
     * read for each field is based on the database schema as stored in
     * {@codeDatabaseConstants}
     *
     * @param recordValues
     * @return
     * @throws DatabaseException
     * @throws UnsupportedEncodingException
     */
    private static Subcontractor createSubcontractorFromByteArrayValues(
	    final byte[] recordValues) throws DatabaseException {
	// The byte array is read starting at this offset which is then
	// incremented with each field length as we read
	int byteOffset = 0;
	int lengthBytes = 0;

	try {
	    // Read the subcontractor name value
	    lengthBytes = DatabaseConstants.CONTRACTOR_NAME_LENGTH_BYTES;
	    final String name = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    // Read the subcontractor location value
	    lengthBytes = DatabaseConstants.CONTRACTOR_LOCATION_LENGTH_BYTES;
	    final String location = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    // Read the subcontractor specialties value
	    lengthBytes = DatabaseConstants.CONTRACTOR_SPECIALITIES_LENGTH_BYTES;
	    final String specialities = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    // Read the subcontractor employee size value
	    lengthBytes = DatabaseConstants.CONTRACTOR_EMPLOYEE_SIZE_LENGTH_BYTES;
	    final String size = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    // Read the subcontractor rate value
	    lengthBytes = DatabaseConstants.CONTRACTOR_RATE_LENGTH_BYTES;
	    final String rate = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    // Read the subcontractor customer Id value
	    lengthBytes = DatabaseConstants.CONTRACTOR_OWNER_LENGTH_BYTES;
	    final String owner = new String(recordValues, byteOffset,
		    lengthBytes, DatabaseConstants.ENCODING).trim();
	    byteOffset += lengthBytes;

	    LOGGER.info("Successfully parsed all record byte values; Creating subcontractor object...");

	    final Subcontractor subcontractor = new Subcontractor(name,
		    location, specialities, size, rate, owner);

	    return subcontractor;
	} catch (final UnsupportedEncodingException e) {
	    throw new DatabaseException(
		    "Encountered an error reading record value: "
			    + e.getMessage());
	}
    }

    public void save(final List<Subcontractor> subcontractors) {
	// TODO Auto-generated method stub

    }

    /**
     * Converts the content of a given <code>byte</code> array to an
     * <code>int</code>.
     *
     * @param byteArray
     *            The <code>byte</code> array that contains the number to be
     *            converted.
     * @return An <code>int</code> that represents the content of the
     *         <code>byte</code> array.
     */
    private static int getIntValueFromByteArray(final byte[] byteArray) {
	int value = 0;
	final int byteArrayLength = byteArray.length;

	for (int i = 0; i < byteArrayLength; i++) {
	    final int shift = (byteArrayLength - 1 - i) * 8;
	    value += (byteArray[i] & 0x000000FF) << shift;
	}

	return value;
    }
}