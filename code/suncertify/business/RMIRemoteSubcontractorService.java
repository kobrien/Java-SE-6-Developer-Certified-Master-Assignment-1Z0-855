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
import java.util.logging.Logger;

import suncertify.db.IDatabase;

public class RMIRemoteSubcontractorService extends DefaultSubcontractorService
	implements RemoteSubcontractorService {

    /** This is the name that will be bound to the RMI registry */
    public final static String SERVER_NAME = "REMOTE_BOOKING_SERVICE";

    private final IDatabase databaseImpl;
    private final static Logger LOGGER = Logger
	    .getLogger(RMIRemoteSubcontractorService.class.getName());

    public RMIRemoteSubcontractorService(final IDatabase databaseManager) {
	super(databaseManager);
	this.databaseImpl = databaseManager;
    }

    @Override
    public void startServer(final int serverPort) throws RemoteException {
	final RemoteSubcontractorService service = (RemoteSubcontractorService) UnicastRemoteObject
		.exportObject(this, 0);

	final Registry registry = LocateRegistry.createRegistry(serverPort);
	registry.rebind(SERVER_NAME, service);
    }
}
