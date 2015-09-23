/*
 * @SubcontractorNotFoundException.java
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
 * The class {@code SubcontractorNotFoundException} should be used when a
 * Subcontractor is not found on the server
 *
 * @author Kieran O Brien
 *
 */
public class SubcontractorNotFoundException extends ServicesException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SubcontractorNotFoundException
     */
    public SubcontractorNotFoundException() {

    }

    /**
     * Constructs a new SubcontractorNotFoundException with the specified detail
     * message
     *
     * @param message
     *            description message of the exception
     */
    public SubcontractorNotFoundException(final String message) {
	super(message);
    }
}
