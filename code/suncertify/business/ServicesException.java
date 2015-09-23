/*
 * @ServicesException.java
 * 15 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.business;

/**
 * {@code ServicesException} is the superclass of all exceptions in the
 * suncertify.business package.
 *
 * @author Kieran O'Brien
 *
 */
public class ServicesException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ServicesException
     */
    public ServicesException() {

    }

    /**
     * Constructs a new ServicesException with the specified detail message
     *
     * @param message
     *            description message of the exception
     */
    public ServicesException(final String message) {
	super(message);
    }
}
