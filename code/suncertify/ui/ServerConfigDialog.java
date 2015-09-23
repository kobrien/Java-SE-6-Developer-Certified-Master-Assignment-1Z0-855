/*
 * @ServerConfigDialog.java
 * 23 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.ui;

public interface ServerConfigDialog extends ConfigDialog {

    /**
     * Gets the value entered in the text field for the database file path
     *
     * @return The path to the database file
     */
    String getDBPath();

    /**
     * Gets the value entered in the text field for the server port
     *
     * @return The server port in which the server will run on
     */
    String getServerPort();
}
