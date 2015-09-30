/*
 * @ClientConfigDialog.java
 * 18 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.ui;

/**
 * ClientConfigDialog extends ConfigDialog and provides more specific
 * configuration options for when running in client mode.
 * 
 * @author Kieran O'Brien
 *
 */
public interface ClientConfigDialog extends ConfigDialog {
    /**
     * Gets the value entered in the text field for the server host address
     *
     * @return The server host in which the client will connect to
     */
    String getServerHost();

    /**
     * Gets the value entered in the text field for the server port
     *
     * @return The server port in which the client will connect to
     */
    String getServerPort();
}
