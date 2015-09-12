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
package suncertify.server;

import java.rmi.Remote;

public interface RemoteSubcontractorService extends Remote,
	SubcontractorService {

    public void startServer(int port);
}
