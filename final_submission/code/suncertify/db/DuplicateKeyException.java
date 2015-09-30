/*
 * @DuplicateKeyException.java
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
 * The class {@code DuplicateKeyException} should be used when a duplicate
 * record is found in the database
 *
 * @author Kieran O'Brien
 *
 */
public class DuplicateKeyException extends DatabaseException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a DuplicateKeyException
     */
    public DuplicateKeyException() {
	super();
    }

    /**
     * Constructs a new DuplicateKeyException with the specified detail message
     *
     * @param message
     *            description message of the exception
     */
    public DuplicateKeyException(final String message) {
	super(message);
    }
}