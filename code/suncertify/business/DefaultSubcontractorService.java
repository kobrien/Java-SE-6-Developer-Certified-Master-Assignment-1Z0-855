/*
 * @DefaultSubcontractorService.java
 * 9 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import suncertify.db.*;

/**
 * DefaultSubcontractorService is the main business service implementation of
 * SubcontractorService.
 *
 * @author Kieran O'Brien
 *
 */
public class DefaultSubcontractorService implements SubcontractorService {

    private final IDatabase databaseImpl;
    private final static Logger LOGGER = Logger
	    .getLogger(DefaultSubcontractorService.class.getName());

    /**
     * Creates a new DefaultSubcontractorService object
     *
     * @param databaseManager
     *            The database implementation that this service will communicate
     *            with
     */
    public DefaultSubcontractorService(final IDatabase databaseManager) {
	super();
	this.databaseImpl = databaseManager;

    }

    @Override
    public SubcontractorRecord getSubcontractor(
	    final int subcontractorRecordNumber) throws RemoteException,
	    SubcontractorNotFoundException {

	String[] subcontractorData = null;

	try {
	    databaseImpl.lock(subcontractorRecordNumber);

	    subcontractorData = databaseImpl.read(subcontractorRecordNumber);
	} catch (final RecordNotFoundException e) {
	    final String message = "Subcontractor record number "
		    + subcontractorRecordNumber + " not found "
		    + e.getMessage();
	    throw new SubcontractorNotFoundException(message);
	} finally {
	    try {
		databaseImpl.unlock(subcontractorRecordNumber);
	    } catch (final RecordNotFoundException e) {
		throw new SubcontractorNotFoundException(
			"Subcontractor record number "
				+ subcontractorRecordNumber + " not found ");
	    }
	}
	final String name = subcontractorData[1];
	final String location = subcontractorData[2];
	final String specialities = subcontractorData[3];
	final String size = subcontractorData[4];
	final String rate = subcontractorData[5];
	final String ownerID = subcontractorData[6];

	final SubcontractorRecord subcontractorRecord = new SubcontractorRecord(
		name, location, specialities, size, rate, ownerID);

	return subcontractorRecord;
    }

    @Override
    public List<SubcontractorRecord> getAllSubcontractors()
	    throws RemoteException {

	final List<SubcontractorRecord> subcontractorsToReturn = new ArrayList<SubcontractorRecord>();

	// Searching with null values returns a match so use this pattern to
	// return everything
	final String[] searchCriteria = new String[] { null, null, null, null,
		null, null, null };

	int[] recordNumbers = null;

	try {
	    recordNumbers = databaseImpl.find(searchCriteria);
	} catch (final RecordNotFoundException e) {
	    // no records were found, so return an empty list!
	}

	if (recordNumbers != null) {
	    for (final int recordNumber : recordNumbers) {
		try {
		    subcontractorsToReturn.add(this
			    .getSubcontractor(recordNumber));
		} catch (final SubcontractorNotFoundException e) {
		    // We just want to swallow this exception and return an
		    // empty list to the presentation layer
		}
	    }
	}
	return subcontractorsToReturn;
    }

    @Override
    public void bookSubcontractor(final int subcontractorRecordNumber,
	    final String ownerID) throws RemoteException,
	    SubcontractorNotFoundException, SubcontractorAlreadyBookedException {

	LOGGER.info("Attempting to book subcontractor recNo "
		+ subcontractorRecordNumber + " with customer ID " + ownerID);

	final SubcontractorRecord subcontractorRecord = this
		.getSubcontractor(subcontractorRecordNumber);

	if (subcontractorRecord.isBooked()) {
	    throw new SubcontractorAlreadyBookedException(
		    "Subcontractor record " + subcontractorRecordNumber
		    + " is already booked by another customer ID");
	}

	final String RecNotFoundMessage = "Subcontractor record number "
		+ subcontractorRecordNumber + " not found ";

	try {
	    databaseImpl.lock(subcontractorRecordNumber);

	    // Before calling the update method we need to get the array of
	    // existing record fields for the subcontractor as these need to be
	    // passed into the update method along with the customer ID
	    final String[] recordFields = subcontractorRecord.toArray();

	    // Update the field containing the customer ID value
	    recordFields[6] = ownerID;

	    databaseImpl.update(subcontractorRecordNumber, recordFields);

	    LOGGER.info("Subcontractor recNo " + subcontractorRecordNumber
		    + " booked with customer ID " + ownerID);

	} catch (final RecordNotFoundException e) {
	    throw new SubcontractorNotFoundException(RecNotFoundMessage
		    + e.getMessage());
	} finally {
	    try {
		databaseImpl.unlock(subcontractorRecordNumber);
	    } catch (final RecordNotFoundException e) {
		throw new SubcontractorNotFoundException(RecNotFoundMessage
			+ e.getMessage());
	    }
	}

    }

    @Override
    public List<SubcontractorRecord> findSubcontractorsByNameAndLocation(
	    final String name, final String location) throws RemoteException,
	    SubcontractorNotFoundException {
	LOGGER.info("Searching for subcontractors that match name '" + name
		+ "' and location '" + location + "'");

	// Store all partial matches from DBMain.find method here
	final List<SubcontractorRecord> subcontractorsPartialMatch = new ArrayList<SubcontractorRecord>();

	// Empty strings are ignored in the find implementation
	final String[] searchCriteria = new String[] { "", name, location, "",
		"", "", "" };

	int[] recordNumbersReturned = null;

	try {
	    recordNumbersReturned = databaseImpl.find(searchCriteria);
	} catch (final RecordNotFoundException e) {
	    e.printStackTrace();
	}

	// Get the list of subcontractor objects from the record keys returned
	if (recordNumbersReturned != null) {
	    for (final int recordNumber : recordNumbersReturned) {
		subcontractorsPartialMatch.add(getSubcontractor(recordNumber));
	    }
	}

	// The find method in DBMain returns true for partial matches, but the
	// user requirements for the main application states that the search
	// values must match exactly, so we need to filter out the partial
	// matches using a regex below
	final List<SubcontractorRecord> subcontractorsExactMatch = new ArrayList<SubcontractorRecord>();

	for (final SubcontractorRecord subcontractor : subcontractorsPartialMatch) {
	    // check that the name is an exact match
	    boolean exactMatch = true;
	    if (!subcontractor.getName().matches("^" + name + "$")) {
		exactMatch = false;
	    }
	    // check that the location is an exact match
	    if (!subcontractor.getLocation().matches("^" + location + "$")) {
		exactMatch = false;
	    }
	    if (exactMatch) {
		LOGGER.info("Found exact match for subcontractor with name '"
			+ name + "' and location '" + location + "'");
		subcontractorsExactMatch.add(subcontractor);
	    }
	}

	return subcontractorsExactMatch;
    }

    @Override
    public List<SubcontractorRecord> findSubcontractorsByName(final String name)
	    throws RemoteException, SubcontractorNotFoundException {
	LOGGER.info("Searching for subcontractors that match name '" + name
		+ "'");

	// Store all partial matches from DBMain.find method here
	final List<SubcontractorRecord> subcontractorsPartialMatch = new ArrayList<SubcontractorRecord>();

	// Empty strings are ignored in the find implementation
	final String[] searchCriteria = new String[] { "", name, "", "", "",
		"", "" };

	int[] recordNumbersReturned = null;

	try {
	    recordNumbersReturned = databaseImpl.find(searchCriteria);
	} catch (final RecordNotFoundException e) {
	    e.printStackTrace();
	}

	// Get the list of subcontractor objects from the record keys returned
	if (recordNumbersReturned != null) {
	    for (final int recordNumber : recordNumbersReturned) {
		subcontractorsPartialMatch.add(getSubcontractor(recordNumber));
	    }
	}

	final List<SubcontractorRecord> subcontractorsExactMatch = new ArrayList<SubcontractorRecord>();

	for (final SubcontractorRecord subcontractor : subcontractorsPartialMatch) {
	    // check that the name is an exact match
	    boolean exactMatch = true;
	    if (!subcontractor.getName().matches("^" + name + "$")) {
		exactMatch = false;
	    }

	    if (exactMatch) {
		LOGGER.info("Found exact match for subcontractor with name '"
			+ name + "'");
		subcontractorsExactMatch.add(subcontractor);
	    }
	}

	return subcontractorsExactMatch;
    }

    @Override
    public List<SubcontractorRecord> findSubcontractorsByLocation(
	    final String location) throws RemoteException,
	    SubcontractorNotFoundException {
	LOGGER.info("Searching for subcontractors that match location '"
		+ location + "'");

	// Store all partial matches from DBMain.find method here
	final List<SubcontractorRecord> subcontractorsPartialMatch = new ArrayList<SubcontractorRecord>();

	// Empty strings are ignored in the find implementation
	final String[] searchCriteria = new String[] { "", "", location, "",
		"", "", "" };

	int[] recordNumbersReturned = null;

	try {
	    recordNumbersReturned = databaseImpl.find(searchCriteria);
	} catch (final RecordNotFoundException e) {
	    e.printStackTrace();
	}

	// Get the list of subcontractor objects from the record keys returned
	if (recordNumbersReturned != null) {
	    for (final int recordNumber : recordNumbersReturned) {
		subcontractorsPartialMatch.add(getSubcontractor(recordNumber));
	    }
	}

	// The find method in DBMain returns true for partial matches, but the
	// user requirements for the main application states that the search
	// values must match exactly, so we need to filter out the partial
	// matches using a regex below
	final List<SubcontractorRecord> subcontractorsExactMatch = new ArrayList<SubcontractorRecord>();

	for (final SubcontractorRecord subcontractor : subcontractorsPartialMatch) {
	    boolean exactMatch = true;
	    // check that the location is an exact match
	    if (!subcontractor.getLocation().matches("^" + location + "$")) {
		exactMatch = false;
	    }
	    if (exactMatch) {
		LOGGER.info("Found exact match for subcontractor with location '"
			+ location + "'");
		subcontractorsExactMatch.add(subcontractor);
	    }
	}

	return subcontractorsExactMatch;
    }

    @Override
    public void saveData() throws RemoteException, ServicesException {
	try {
	    LOGGER.info("Attempting to save application data to disk");
	    databaseImpl.saveData();
	} catch (final DatabaseException e) {
	    final String message = "Encounted a problem saving data back to file. Error Message: "
		    + e.getMessage();
	    throw new ServicesException(message);
	}
    }
}
