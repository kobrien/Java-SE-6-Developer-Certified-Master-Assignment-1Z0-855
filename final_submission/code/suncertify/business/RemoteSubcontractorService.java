/*
 * @RemoteSubcontractorService.java
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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote business service implementation of SubcontractorService.
 *
 * @author Kieran O'Brien
 *
 */
public interface RemoteSubcontractorService extends Remote,
SubcontractorService {

    /**
     * This method should export the server implementation as a remote object to
     * make it available to receive incoming requests on the specified port.
     *
     * @param serverPort
     *            The port in which the server will be hosted to allow remote
     *            requests to be made
     * @throws RemoteException
     */
    void startServer(int serverPort) throws RemoteException;
}
