/*
 * @SubcontractorService.java
 * 15 Sep 2015
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
import java.util.List;

import suncertify.db.SubcontractorRecord;

/**
 * The {@code SubcontractorService} interface contains all business
 * functionality of the application. This interface has two implementations, one
 * for client mode and the other for standalone mode. The instantiation of
 * either is done at runtime based on whichever mode the application is started
 * with. The presentation layer accesses the business layer through this
 * interface, regardless of the implementation i.e. It doesn't know whether the
 * subcontractor records are being presented locally or remotely. The
 * functionality in this interface is similar to that of {@code DBMain} but
 * contains more abstraction and deals directly with Subcontractor Object DTOs
 * rather than string arrays
 *
 * @author Kieran O'Brien
 *
 */
public interface SubcontractorService {
    /**
     * Returns all subcontractor records from the database or an empty list if
     * none are found
     *
     * @return A List of subcontractor record objects
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     */
    List<SubcontractorRecord> getAllSubcontractors() throws RemoteException;

    /**
     * Given the specified record number this method finds and returns the
     * subcontractor record object if it exists. If the record does not exist in
     * the database an exception is raised. All data regarding this
     * subcontractor record is accessible via the subcontractor object.
     *
     * @param subcontractorRecordNumber
     *            The record number of the subcontractor
     * @return The subcontractor record
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     */
    SubcontractorRecord getSubcontractor(int subcontractorRecordNumber)
	    throws RemoteException, SubcontractorNotFoundException;

    /**
     * Books a subcontractor record for the specified customer ID. If the record
     * is already booked a SubcontractorAlreadyBookedException is thrown
     *
     * @param subcontractorRecordNumber
     *            The subcontractor record number
     * @param ownerID
     *            The customer ID to book the record with
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     * @throws SubcontractorAlreadyBookedException
     */
    void bookSubcontractor(int subcontractorRecordNumber, String ownerID)
	    throws RemoteException, SubcontractorNotFoundException,
	    SubcontractorAlreadyBookedException;

    /**
     * Finds and returns all subcontractor records that have a name and location
     * that matches exactly the specified arguments.
     *
     * DESIGN DECISION: I have excluded other subcontractor fields from being
     * searchable as only the name and location are a business requirement at
     * this moment. If it is a requirement in the future to search for other
     * fields extra methods can easily be added to this interface.
     *
     * @param name
     *            The subcontractor's name
     * @param location
     *            The subcontractor's location
     * @return A List of subcontractor record objects
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     */
    List<SubcontractorRecord> findSubcontractorsByNameAndLocation(String name,
	    String location) throws RemoteException,
	    SubcontractorNotFoundException;

    /**
     * Finds and returns all subcontractor records that have a name that matches
     * exactly the specified name.
     *
     * @param name
     *            The subcontractor's name
     * @return A List of subcontractor record objects
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     */
    List<SubcontractorRecord> findSubcontractorsByName(String name)
	    throws RemoteException, SubcontractorNotFoundException;

    /**
     * Finds and returns all subcontractor records that have a location that
     * matches exactly the specified location.
     *
     * @param location
     *            The subcontractor's location
     * @return A List of subcontractor record objects
     * @throws RemoteException
     * @throws SubcontractorNotFoundException
     */
    List<SubcontractorRecord> findSubcontractorsByLocation(String location)
	    throws RemoteException, SubcontractorNotFoundException;

    /**
     * Saves all subcontractor records that are in memory back to the database
     * file. This method is called when the application terminates.
     *
     * @throws RemoteException
     * @throws ServicesException
     */
    void saveData() throws RemoteException, ServicesException;
}
