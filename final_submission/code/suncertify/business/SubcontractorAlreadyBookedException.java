/*
 * @SubcontractorAlreadyBooked.java
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
 * The class {@code SubcontractorAlreadyBookedException} should be used when a
 * Subcontractor is not able to be booked on the server if it is already booked
 * by another client
 *
 * @author Kieran O Brien
 *
 */
public class SubcontractorAlreadyBookedException extends ServicesException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SubcontractorAlreadyBookedException
     */
    public SubcontractorAlreadyBookedException() {
	super();
    }

    /**
     * Constructs a new SubcontractorAlreadyBookedException with the specified
     * detail message
     *
     * @param message
     *            description message of the exception
     */
    public SubcontractorAlreadyBookedException(final String message) {
	super(message);
    }
}
