/*
 * @DatabaseConstants.java
 * 22 Aug 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.db;

/**
 * {@code DatabaseConstants} contains information relating to the database
 * schema as outlined in the project specification. Information includes the
 * number of bytes that are used to hold values for different fields and values
 * etc.
 *
 * Design decision- Rather than dynamically creating these values on the fly it
 * is assumed that the database information/Schema is not likely to change but
 * if it does this class can easily be updated in this case
 *
 * @author Kieran O'Brien
 *
 */
public interface DatabaseConstants {

    /*
     * Header values
     */

    /**
     * The number of bytes that store the "magic cookie" value at the beginning
     * of the file. The value of the cookie is used to validate the data file.
     */
    public static final int MAGIC_COOKIE_VALUE_BYTES = 4;

    /**
     * The expected value of the magic cookie, this verifies that the database
     * is valid.
     */
    // NOTE: This value was obtained by reading the database file manually, the
    // reason we are not reading this dynamically is because this value must be
    // known upfont in order to verify if the DB file is valid
    public static final int EXPECTED_MAGIC_COOKIE_VALUE = 514;

    /**
     * The number of bytes that store the "offset to start of record zero" value
     */
    public static final int OFFSET_TO_START_RECORD_ZERO_VALUE_BYTES = 4;

    /** The number of bytes that store the number of fields in each record */
    public static final int NUMBER_OF_FIELDS_PER_RECORD_VALUE_BYTES = 2;

    /*
     * Schema Description Section
     */

    /** The number of bytes that store the length of each field name */
    public static final int FIELD_NAME_LENGTH_BYTES = 2;

    /** The number of bytes that store the length of a field */
    public static final int FIELD_LENGTH_BYTES = 2;

    /** The number of bytes that store the flag of each record */
    public static final int RECORD_FLAG_LENGTH_BYTES = 2;

    /*
     * Data Section values
     */

    /** The number of bytes that store the valid flag value */
    public static final int VALID_RECORD_LENGTH_BYTES = 2;

    /** The value that identifies a record as being valid */
    public static final int VALID_RECORD = 00;

    /** The value that identifies a record as being deleted */
    public static final int DELETED_RECORD = 0x8000;

    /*
     * Database Schema values
     */

    /** The number of bytes that store the contractor's name */
    public static final int CONTRACTOR_NAME_LENGTH_BYTES = 32;
    /** The number of bytes that store the contractor's location */
    public static final int CONTRACTOR_LOCATION_LENGTH_BYTES = 64;
    /** The number of bytes that store the contractor's specialities */
    public static final int CONTRACTOR_SPECIALITIES_LENGTH_BYTES = 64;
    /** The number of bytes that store the contractor's employee size */
    public static final int CONTRACTOR_EMPLOYEE_SIZE_LENGTH_BYTES = 6;
    /** The number of bytes that store the contractor's rate */
    public static final int CONTRACTOR_RATE_LENGTH_BYTES = 8;
    /** The number of bytes that store the contractor's owner Id */
    public static final int CONTRACTOR_OWNER_LENGTH_BYTES = 8;

    /** The sum in bytes of the length of all the contractor fields */
    public static final int CONTRACTOR_RECORD_LENGTH_BYTES = 182;

    /**
     * The total length in bytes of all the record fields lengths including the
     * initial 'valid byte' field
     */
    public static final int TOTAL_RECORD_LENGTH_BYTES = CONTRACTOR_RECORD_LENGTH_BYTES
	    + VALID_RECORD_LENGTH_BYTES;

    /** The character encoding of the file */
    public static final String ENCODING = "US-ASCII";

}
