/*
 * @RMIRemoteSubcontractorService.java
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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.IDatabase;

/**
 * RMIRemoteSubcontractorService is an RMI implementation of the remote business
 * service interface RemoteSubcontractorService. It also extends
 * DefaultSubcontractorService and therefore has access to all of the business
 * service functionality resulting in no code duplication. The remote interface
 * contains a single method to start the server on a specific port. This is
 * implemented in this class by exporting this object (which is a
 * DefaultSubcontractorService object) to the RMI Registry thus making it
 * available to receive incoming requests from clients
 *
 * @author Kieran O'Brien
 *
 */
public class RMIRemoteSubcontractorService extends DefaultSubcontractorService
implements RemoteSubcontractorService {

    /** This is the name that will be bound to the RMI registry */
    public final static String SERVER_NAME = "REMOTE_BOOKING_SERVICE";

    /**
     * Creates a new RMIRemoteSubcontractorService object
     *
     * @param databaseManager
     *            The database implementation that this service will communicate
     *            with
     */
    public RMIRemoteSubcontractorService(final IDatabase databaseManager) {
	super(databaseManager);
    }

    @Override
    public void startServer(final int serverPort) throws RemoteException {
	final RemoteSubcontractorService service = (RemoteSubcontractorService) UnicastRemoteObject
		.exportObject(this, 0);

	final Registry registry = LocateRegistry.createRegistry(serverPort);
	registry.rebind(SERVER_NAME, service);
    }
}
