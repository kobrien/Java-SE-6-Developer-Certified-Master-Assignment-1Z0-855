/*
 * @Database.java
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

/**
 * This interface extends DBMain interface to provide extra functionality that
 * was not included in the original interface design. For example this interface
 * adds functionality to save data back to disk which is not present in the
 * original interface. The absence of a save function is one limitation that i
 * have addressed from a design point of view as i do not want to read and write
 * to the database file in real time but would rather write when the application
 * terminates
 *
 *
 * @author Kieran O'Brien
 *
 */
public interface IDatabase extends DBMain {

    /**
     * Saves the data to disk when the application terminates
     *
     * @throws DatabaseException
     */
    void saveData() throws DatabaseException;

}
