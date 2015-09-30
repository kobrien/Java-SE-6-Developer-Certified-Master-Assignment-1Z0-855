/*
 * @StartException.java
 * 15 Sep 2015
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
 * The class StartException should be used when the application is unable to
 * start for whatever reason.
 *
 * @author Kieran O'Brien
 *
 */
public class StartException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code StartException}
     */
    public StartException() {
	super();
    }

    /**
     * Constructs a new {@code StartException} with the specified detail message
     *
     * @param message
     *            description message of the exception
     */
    public StartException(final String message) {
	super(message);
    }
}
