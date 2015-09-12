/*
 * @DatabaseImpl.java
 * 25 Jul 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.db;

import java.util.logging.Logger;

/**
 * The {@code Data} class implements IDatabase which extends DBMain interface
 * thus implementing the required sun interface. It is instantiated once when
 * the application starts and is used throughout the application to
 * read/write/update records. All record contents from the database are read
 * into a cache object and flushed back to disk when the application terminates.
 * In order to guarantee database integrity all writes to this cache are
 * synchronized. This class uses a Facade design pattern by delegating data
 * management and locking responsibilities to other helper classes
 *
 * @author Kieran O'Brien
 *
 */
public class Data implements IDatabase {
    private final static Logger LOGGER = Logger.getLogger(Data.class.getName());

    private final Cache cache;

    /**
     * Constructor takes the absolute path to the database file. This path
     * should be read from the suncertify.properties file which is populated by
     * the user at startup
     *
     * This constructor also initializes the first connection to the database
     * file using DBFileManager and creates an instance of cache, populating it
     * with records from the database file if not already done so
     *
     * @param databasePath
     *            The absolute path to the database file
     *
     * @throws DatabaseException
     */
    public Data(final String databasePath) throws DatabaseException {
	LOGGER.info("Creating new Data instance");
	// Creates a connection to the database file if not already done so
	DBFileReader.initializeConnection(databasePath);

	// Get a reference to the cache, creating it if not already created
	this.cache = Cache.getInstance();
    }

    /**
     * Reads a subcontractor record from the cache if it exists and returns a
     * string array of all the fields of this subcontractor
     *
     * @param recNo
     *            - The record number to read
     * @return String array representation of a Subcontractor object where each
     *         subcontractor field n appears in array[n].
     * @throws RecordNotFoundException
     */
    @Override
    public String[] read(final int recNo) throws RecordNotFoundException {
	// TODO lock the record before reading to avoid dirty reads
	final Subcontractor subcontractor = this.cache.getSubcontractor(recNo);

	if (subcontractor == null) {
	    throw new RecordNotFoundException("Record number " + recNo
		    + " not found in cache");
	}

	return subcontractor.toArray();
    }

    /**
     * Modifies the fields of an existing subcontractor record in cache. The new
     * values for field n appears in data[n]. NOTE: If an update is made to the
     * name and/or location (data[0] and/or data [1]) the existing record number
     * will no longer be valid and so to avoid duplicates this subcontractor
     * will be removed from cache and the new record number and corresponding
     * contractor will be added to cache
     *
     * @param recNo
     *            - The record number to update
     * @param data
     *            - String array of values where each subcontractor field n
     *            appears in array[n].
     * @throws RecordNotFoundException
     */
    @Override
    public void update(final int recNo, final String[] data)
	    throws RecordNotFoundException {

	final Subcontractor subcontractor = this.cache.getSubcontractor(recNo);

	if (subcontractor == null) {
	    throw new RecordNotFoundException("Record " + recNo
		    + "not found in cache");
	}

	// update the fields for this subcontractor object - this also validates
	// that they are correct number etc.
	subcontractor.update(data);

	// Check if the record number was changed i.e. if name and/or location
	// fields were updated
	if (recNo != subcontractor.hashCode()) {
	    // if so remove the old record number from cache as this is no
	    // longer valid
	    cache.removeSubcontractor(recNo);
	}

	// Update the fields of the existing subcontractor or create a new one
	// if the record number has changed above
	cache.addSubcontractor(recNo, subcontractor);

    }

    /**
     * Deletes a subcontractor record, making the record number and associated
     * disk storage available for reuse.
     *
     * @param recNo
     *            - The record number to delete
     * @throws RecordNotFoundException
     */
    @Override
    public void delete(final int recNo) throws RecordNotFoundException {
	// TODO
    }

    /**
     * Returns an array of record numbers that match the specified criteria.
     * Field n in the database file is described by criteria[n]. A null value in
     * criteria[n] matches any field value. A non-null value in criteria[n]
     * matches any field value that begins with criteria[n]. (For example,
     * "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria
     *            String array of search criteria. NOTE: This must not exceed a
     *            length of 6
     * @return The integer array of all matching record numbers
     *
     * @throws RecordNotFoundException
     */
    @Override
    public int[] find(final String[] criteria) throws RecordNotFoundException {
	if (criteria == null) {
	    throw new IllegalArgumentException("String[] data cannot be null");
	}
	return cache.findSubcontractors(criteria);
    }

    /**
     * Creates a new record in the database (possibly reusing a deleted entry).
     * Inserts the given data, and returns the record number of the new record.
     *
     * @param data
     *            String array of the new record to create. NOTE: This must not
     *            exceed a length of 6
     * @return the record number of the newly created record
     * @throws DuplicateKeyException
     */
    @Override
    public int create(final String[] data) throws DuplicateKeyException {
	return 0;
	// TODO
    }

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * If the specified record is already locked, the current thread gives up
     * the CPU and consumes no CPU cycles until the record is unlocked.
     *
     * @param recNo
     *            The record number to lock for this client
     * @throws RecordNotFoundException
     */
    @Override
    public void lock(final int recNo) throws RecordNotFoundException {
	LockManager2.lockRecord(recNo, this);
    }

    /**
     * Releases the lock on a record.
     *
     * @param recNo
     *            The record number to unlock for this client
     * @throw RecordNotFoundException
     */
    @Override
    public void unlock(final int recNo) throws RecordNotFoundException {
	LockManager2.unlockRecord(recNo, this);

    }

    /**
     * Determines if a record is currently locked. Returns true if the record is
     * locked, false otherwise.
     *
     * @param recNo
     *            The record number to check if locked
     * @return true if locked, false if not locked
     * @throws RecordNotFoundException
     */
    @Override
    public boolean isLocked(final int recNo) throws RecordNotFoundException {
	return LockManager2.isRecordLocked(recNo);
    }

    /**
     * Saves the contents of the cache back to disk. This method should be
     * called when the application terminates, either normally or abnormally
     *
     * @throws DatabaseException
     */
    @Override
    public void saveData() throws DatabaseException {
	DBFileReader.persistAllSubcontractors(cache.getCachedSubcontractors());
    }

}
