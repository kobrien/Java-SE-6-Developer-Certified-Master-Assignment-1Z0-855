/*
 * @Cache.java
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

import java.util.*;
import java.util.logging.Logger;

/**
 * {@code SubcontractorCache} is used to store all contractor objects read from
 * the database file in memory. When the application starts all records are read
 * from disk into this cache stored as {@code Subcontractor} objects. The
 * {@code Data} class is responsible for creating the cache on start up (and
 * populating as a result of this) and flushing it's contents back to disk when
 * the application terminates either normally or abnormally. From a design point
 * of view the purpose of this class is to provide better performance as there
 * is less disk I/O involved. It also guarantees data integrity as we can
 * synchronize directly on the singleton cache object when modifying records.
 * Also as we are not writing to disk in real time we avoid any bad writes say
 * for example if the I/O stream terminates abnormally.
 *
 * The cache does not store any information about where the records are
 * persisted in the database i.e. byte locations, it is purely used to hold
 * subcontractor data.
 *
 * @author Kieran O'Brien
 *
 */
public class SubcontractorCache {
    private final static Logger LOGGER = Logger
	    .getLogger(SubcontractorCache.class.getName());

    private static SubcontractorCache instance;

    private final Map<Integer, SubcontractorRecord> cachedRecords;

    /**
     * Creates a new Cache object. This cache object is used by the Data class
     * to read/write Subcontractor records in memory. When the cache is first
     * instantiated it reads all records from the database and stores them as
     * SubContractor objects. These objects are then written back to disk when
     * the application terminates
     *
     * @throws DatabaseException
     */
    private SubcontractorCache() {
	LOGGER.info("Creating new cache instance");
	this.cachedRecords = new HashMap<Integer, SubcontractorRecord>();

	try {
	    this.populateCache();
	} catch (final DatabaseException e) {
	    // TODO what to do?
	    e.printStackTrace();
	}

    }

    /**
     * Returns the singleton cache instance creating it once if not already
     * created
     *
     * @return The {@code Cache} object
     */
    public static SubcontractorCache getInstance() {
	if (instance == null) {
	    // TODO verify i want this double checked locking
	    synchronized (SubcontractorCache.class) {
		// critical section - only 1 thread can execute here
		if (instance == null) {
		    instance = new SubcontractorCache();
		}
	    }
	}
	return instance;
    }

    /**
     * Reads all subcontractor records from the database file using
     * DBFileManager and adds them to the cache. The records are returned from
     * DBFileManager as subcontractor objects. This method only gets called once
     * when the cache is initialized. The hashcode of the Subcontractor object
     * (composite of the record name and location) is used as it's record number
     *
     * @throws DatabaseException
     */
    private void populateCache() throws DatabaseException {
	LOGGER.info("Populating cache with subcontractor records from database");

	List<SubcontractorRecord> subcontractors = new ArrayList<SubcontractorRecord>();

	subcontractors = DBFileAccess.getAllSubcontractors();

	for (final SubcontractorRecord sub : subcontractors) {
	    this.addSubcontractor(sub.getRecordNumber(), sub);
	}

	LOGGER.info("Successfully populated the cache");

    }

    /**
     * Adds a subcontractor record to the cache or if the cache already has this
     * record number replace the existing fields with the new fields of the
     * subcontractor object
     *
     * @param recordNo
     *            The unique record number for this subcontractor record
     * @param subcontractor
     *            Subcontractor object
     */
    public void addSubcontractor(final int recordNo,
	    final SubcontractorRecord subcontractor) {
	// TODO synchronize on the cache object here or from the Data class?
	// Lock should be called before calling Data.update
	synchronized (cachedRecords) {
	    this.cachedRecords.put(recordNo, subcontractor);
	}
    }

    /**
     * Returns a subcontractor record from the cache based on the record number
     * key or null if the key does not exist
     *
     * @param recordNo
     *            The unique record number key for this subcontractor record
     * @return Subcontractor object
     */
    public SubcontractorRecord getSubcontractor(final int recordNo) {
	return this.cachedRecords.get(recordNo);
    }

    /**
     * Removes a Subcontractor object with the specified record number from the
     * cache. Not only is this method used when deleting a record but also if an
     * update is made to the subcontractor's name or location fields as this
     * will result in a new record number for that subcontractor object
     *
     * @param recordNo
     *            The record number of the Subcontractor
     * @throws RecordNotFoundException
     */
    public void removeSubcontractor(final int recordNo)
	    throws RecordNotFoundException {
	if (this.cachedRecords.containsKey(recordNo)) {
	    this.cachedRecords.remove(recordNo);
	} else {
	    throw new RecordNotFoundException(
		    "No record number found for subcontractor " + recordNo);
	}
    }

    /**
     * Returns an array of record numbers that match the specified criteria.
     * Field n of the subcontractor record is described by criteria[n]. A null
     * value in criteria[n] matches any field value. A non-null value in
     * criteria[n] matches any field value that begins with criteria[n]. (For
     * example, "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria
     *            String array of search criteria. NOTE: This must not exceed a
     *            length of 6
     * @return The integer array of all matching record numbers
     */
    public int[] findSubcontractors(final String[] criteria) {
	// Get the list of all subcontractor from cache
	final List<SubcontractorRecord> subcontractors = getCachedSubcontractors();

	// Each matching subcontractor will be added to this list
	final List<Integer> matchingRecordNumbersList = new ArrayList<Integer>();

	for (final SubcontractorRecord subcontractor : subcontractors) {
	    if (subcontractor.matchesCriteria(criteria)) {
		matchingRecordNumbersList.add(subcontractor.hashCode());
	    }
	}

	// Create an integer array with size based on the number of matches
	final int numMatches = matchingRecordNumbersList.size();
	final int[] results = new int[numMatches];

	for (int index = 0; index < matchingRecordNumbersList.size(); index++) {
	    results[index] = matchingRecordNumbersList.get(index);
	}

	return results;
    }

    boolean doesSubcontractorRecordExist(final int recNo) {
	return cachedRecords.containsKey(recNo);

    }

    // TODO i think i need to synchronize this as i dont want another thread to
    // update a record in the middle of returning this list to another thread
    List<SubcontractorRecord> getCachedSubcontractors() {
	synchronized (this.cachedRecords) {
	    return new ArrayList<SubcontractorRecord>(
		    this.cachedRecords.values());
	}
    }

}
