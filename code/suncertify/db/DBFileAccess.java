package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * {@code DBFileAccess} is responsible for reading and writing records to the
 * database file. It is instantiated and should only be used by the {@code Data}
 * class. All records are read and converted to {@code Subcontractor} objects
 * and then returned to the {@code Data} class to be loaded into cache at
 * startup. Likewise this class is capable of taking a subcontractor object and
 * writing it to disk. This class uses a hashmap to maintain a mapping of record
 * numbers to their location in the database file. Using a RandomAccessFile this
 * allows us to quickly seek to that record location.
 *
 * @author Kieran O'Brien
 *
 */
public class DBFileAccess {
    private final static Logger LOGGER = Logger.getLogger(DBFileAccess.class
	    .getName());

    // The unique value at the start of the database file which is used to prove
    // the integrity of the file
    private static int magicCookie;

    // The location to which the first record value can be found
    private static int firstRecordLocationValue;

    // File object used to interact with the database file
    private static RandomAccessFile databaseFile;

    // Read and write access for the database file
    private static final String FILE_ACCESS_MODE = "rw";

    /**
     * This method is called by the constructor of {@code Data} passing it the
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
		LOGGER.info("Initializing connection to database file "
			+ databasePath);

		// Check if the database file actually exists
		final File f = new File(databasePath);
		if (!f.isFile()) {
		    throw new FileNotFoundException();
		}

		databaseFile = new RandomAccessFile(f, FILE_ACCESS_MODE);

		if (databaseFile.length() == 0) {
		    throw new DatabaseException("Empty database file "
			    + databasePath);
		}

		readDatabaseHeaderValues();
		verifyValidDatabase();

	    } catch (final FileNotFoundException exception) {
		throw new DatabaseException("Invalid database file path "
			+ databasePath);
	    } catch (final IOException e) {
		throw new DatabaseException("Could not read database file "
			+ databasePath + ". IO Error: " + e.getMessage());
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

	if (magicCookie != DatabaseConstants.EXPECTED_MAGIC_COOKIE_VALUE) {
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
    public static List<SubcontractorRecord> getAllSubcontractors()
	    throws DatabaseException {
	LOGGER.info("Reading all subcontractor records from database");
	final List<SubcontractorRecord> subcontractors = new ArrayList<SubcontractorRecord>();

	try {
	    final long databaseLength = databaseFile.length();
	    final long totalRecordLength = DatabaseConstants.TOTAL_RECORD_LENGTH_BYTES;

	    // Starting at record location zero, Iterate over each record
	    // creating subcontractor objects for each one until we reach the
	    // end of the file
	    for (long fileOffset = firstRecordLocationValue; fileOffset < databaseLength; fileOffset += totalRecordLength) {
		final SubcontractorRecord subcontractor = readSubcontractorRecord(fileOffset);

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
	    magicCookie = getIntValueFromByteArray(magicCookieValue);

	    final byte[] offsetStartRecordZero = new byte[DatabaseConstants.OFFSET_TO_START_RECORD_ZERO_VALUE_BYTES];
	    databaseFile.readFully(offsetStartRecordZero);
	    firstRecordLocationValue = getIntValueFromByteArray(offsetStartRecordZero);

	} catch (final IOException e) {
	    throw new DatabaseException(
		    "I/O error occured trying to read database file "
			    + e.getMessage());
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
    private static SubcontractorRecord readSubcontractorRecord(
	    final long fileOffset) throws DatabaseException {

	final byte[] validRecordValueBuffer = new byte[DatabaseConstants.VALID_RECORD_LENGTH_BYTES];
	final byte[] recordValuesBuffer = new byte[DatabaseConstants.CONTRACTOR_RECORD_LENGTH_BYTES];

	// Read the byte values from file
	synchronized (databaseFile) {
	    try {
		// Update file pointer to the the file location we want to read
		databaseFile.seek(fileOffset);

		databaseFile.readFully(validRecordValueBuffer);

		databaseFile.readFully(recordValuesBuffer);
	    } catch (final IOException e) {
		throw new DatabaseException(e.getMessage());
	    }
	}
	// Check if the record is valid
	final int isValidRecord = getIntValueFromByteArray(validRecordValueBuffer);

	SubcontractorRecord subcontractor = null;

	// only create the subcontractor object if the record is valid
	if (isValidRecord == DatabaseConstants.VALID_RECORD) {
	    subcontractor = createSubcontractorRecordFromByteArrayValues(recordValuesBuffer);
	}
	return subcontractor;

    }

    /**
     * Starting at record zero, writes all subcontractor objects back to the
     * database file overwriting the existing records on disk.
     *
     * @param subcontractors
     *
     * @throws DatabaseException
     */

    private static void persistAllSubcontractors(
	    final List<SubcontractorRecord> subcontractors)
	    throws DatabaseException {

	final long totalRecordLength = DatabaseConstants.TOTAL_RECORD_LENGTH_BYTES;

	// Starting at record location zero, Iterate over each subcontractor
	// object and only write the record back to disk if it is valid record
	long fileOffset = firstRecordLocationValue;

	for (final SubcontractorRecord subcontractor : subcontractors) {
	    if (subcontractor.isValidRecord()) {
		writeSubcontractorRecord(subcontractor, fileOffset);
	    }
	    fileOffset += totalRecordLength;
	}
    }

    /**
     * Writes a single subcontractor record to the database file given the
     * specified offset location. All fields are read from the subcontractor
     * object into a string builder object (maintaining all field lengths based
     * on the DB schema) and this is then written to disk. The 'valid record'
     * byte is also written to the first 2 bytes of the record location based on
     * whether the subcontractor object is valid or not
     *
     * @param subcontractor
     * @param fileOffset
     * @throws DatabaseException
     */
    private synchronized static void writeSubcontractorRecord(
	    final SubcontractorRecord subcontractor, final long fileOffset)
	    throws DatabaseException {

	// Using a string builder we can write each record field value while
	// maintaining it's correct length in bytes
	final StringBuilder builder = subcontractor.toStringBuilder();

	// Write the string value to the file
	try {
	    // Update file pointer to the the file location we want to
	    // write
	    databaseFile.seek(fileOffset);

	    // Write the 'valid record' to the first 2 bytes of the
	    // record
	    databaseFile.writeShort(DatabaseConstants.VALID_RECORD);

	    // Write the record value
	    databaseFile.write(builder.toString().getBytes());
	} catch (final IOException e) {
	    throw new DatabaseException(e.getMessage());
	}
    }

    /**
     * Saves the contents of the cache back to disk. This method should be
     * called when the application terminates, either normally or abnormally
     *
     * @throws DatabaseException
     */
    public static void writeAllSubcontractorsToFile() throws DatabaseException {
	// Get a reference to the cache, creating it if not already created
	final SubcontractorCache cache = SubcontractorCache.getInstance();

	persistAllSubcontractors(cache.getCachedSubcontractors());
    }

    /**
     * Creates a subcontractor object with values for each of it's fields given
     * the array of bytes read from the database record. The length of bytes to
     * read for each field is based on the database schema as stored in
     * {@codeDatabaseConstants}
     *
     * @param recordValues
     * @return SubcontractorRecord
     * @throws DatabaseException
     * @throws UnsupportedEncodingException
     */
    private static SubcontractorRecord createSubcontractorRecordFromByteArrayValues(
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

	    final SubcontractorRecord subcontractor = new SubcontractorRecord(
		    name, location, specialities, size, rate, owner);

	    return subcontractor;
	} catch (final UnsupportedEncodingException e) {
	    throw new DatabaseException(
		    "Encountered an error reading record value: "
			    + e.getMessage());
	}
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