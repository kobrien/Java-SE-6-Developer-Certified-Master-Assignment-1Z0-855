/*
 * @DatabaseException.java
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
 * {@code DatabaseException} is the superclass of all exceptions in the
 * suncertify.db package. The purpose of this class is to simplify exception
 * handling especially with regard to throw clauses.
 *
 * @author Kieran O'Brien
 *
 */
public class DatabaseException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DatabaseException
     */
    public DatabaseException() {
	super();
    }

    /**
     * Constructs a new DatabaseException with the specified detail message
     *
     * @param message
     *            description message of the exception
     */
    public DatabaseException(final String message) {
	super(message);
    }
}
