/*
 * @Booking.java
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

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * The <code>Subcontractor</code> class represents a subcontractor record. It
 * contains all fields that relate to a subcontractor record. This class should
 * be used to store Subcontractor objects in the data cache and as a Data
 * Transfer Object (DTO) between the business layer and the data layer, it does
 * not contain any logic and is purely used to encapsulate data.
 *
 * Design Decision - I left out setters for certain subcontractor values that do
 * not need to be modified within the scope of this project
 *
 * @author Kieran O'Brien
 *
 */

public class Subcontractor implements Serializable {

    private static final long serialVersionUID = -2341613579485649698L;

    /** The name of the subcontractor */
    private String name;

    /** The location in which this subcontractor works */
    private String location;

    /** A list of the different types of work this subcontractor performs */
    private String specialities;

    /** The number of workers available for this subcontractor */
    private String employeeSize;

    /** The hourly rate of the subcontractor */
    private String rate;

    /** The owner customer id is an 8 digit number */
    private String owner;

    /** The number of fields for a subcontractor */
    public static int numberFields = 6;

    /**
     * Creates a new Subcontractor object given the multiple data fields
     * required
     *
     * @param name
     *            The name of the subcontractor
     * @param location
     *            The city in which this subcontractor works
     * @param specialities
     *            The list of work available from this subcontractor
     * @param size
     *            The number of workers available for this subcontractor
     * @param specialties
     *            The different types of work performed by this subcontractor
     * @param employeeSize
     *            The number of staff available for this subcontractor
     * @param rate
     *            The charge per hour for this subcontractor
     * @param owner
     *            The owner customer ID that has booked this subcontractor
     */
    public Subcontractor(final String name, final String location,
	    final String specialities, final String size, final String rate,
	    final String owner) {
	this.name = name;
	this.location = location;
	this.specialities = specialities;
	this.employeeSize = size;
	this.rate = rate;
	this.owner = owner;
    }

    /**
     * @return the name of this subcontractor
     */
    public String getName() {
	return name;
    }

    /**
     * @return the location for this subcontractor
     */
    public String getLocation() {
	return location;
    }

    /**
     * @return the specialties this subcontractor can perform
     */
    public String getSpecialities() {
	return specialities;
    }

    /**
     * @return the number of workers available for this subcontractor
     */
    public String getEmployeeSize() {
	return employeeSize;
    }

    /**
     * @return the hourly rate for this subcontractor
     */
    public String getHourlyRate() {
	return rate;
    }

    /**
     * @return the owner customer ID who currently owns this subcontractor
     */
    public String getOwner() {
	return owner;
    }

    /**
     * Sets the owner customer Id for this subcontractor record. This is set by
     * the CSR when booking a subcontractor for a particular client
     *
     * @param owner
     *            the owner customer Id to set as owner of this subcontractor
     */
    public void setOwner(final String owner) {
	if (owner == null) {
	    throw new IllegalArgumentException(
		    "Invalid owner customer Id value");
	}
	this.owner = owner;
    }

    /**
     * Updates each subcontractor field n with the data in fields[n]. Throws an
     * IllegalArgumentException if any of the data values are invalid
     *
     *
     * @param fields
     */
    public void update(final String[] fields) {
	if (fields == null || fields.length < this.toArray().length) {
	    throw new IllegalArgumentException(
		    "Invalid data fields for Subcontractor");
	}
	this.setName(fields[0]);
	this.setLocation(fields[1]);
	this.setSpecialities(fields[2]);
	this.setEmployeeSize(fields[3]);
	this.setHourlyRate(fields[4]);
	this.setOwner(fields[5]);
    }

    /**
     * @param string
     */
    private void setHourlyRate(final String hourlyRate) {
	if (hourlyRate == null) {
	    throw new IllegalArgumentException(
		    "You must provide an hourly rate ");
	}
	this.rate = hourlyRate;
    }

    /**
     * @param string
     */
    private void setEmployeeSize(final String size) {
	try {
	    if (Integer.parseInt(size) < 0) {
		throw new IllegalArgumentException(
			"Employee size cannot be less than 0");
	    }
	    this.employeeSize = size;
	} catch (final NumberFormatException nfe) {
	    throw new IllegalArgumentException(
		    "Employee size must be a valid number");
	}
    }

    /**
     * @param string
     */
    private void setSpecialities(final String specialities) {
	if (specialities == null || specialities.length() < 1) {
	    throw new IllegalArgumentException("Invalid list of specialities");
	}
	this.specialities = specialities;
    }

    /**
     * @param string
     */
    private void setLocation(final String location) {
	if (location == null) {
	    throw new IllegalArgumentException("Inalid location");
	}
	this.location = location;
    }

    /**
     * @param string
     */
    private void setName(final String name) {
	if (name == null) {
	    throw new IllegalArgumentException("Invalid Subcontractor Name ");
	}
	this.name = name;
    }

    /**
     * Checks if any of the Subcontractor's fields match the specified criteria.
     * A null value in criteria[n] matches any field value. A non-null value in
     * criteria[n] matches any field value that begins with criteria[n]. (For
     * example, "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria
     * @return True if a match was found
     */
    public boolean containsField(final String[] criteria) {
	final String[] subcontractorFields = this.toArray();

	// Iterate over the list of criteria and return true if there is a match
	for (int index = 0; index < criteria.length; index++) {

	    // Return true if we find a null value in criteria[n]
	    if (criteria[index] == null) {
		return true;
	    }

	    // final boolean isEmptyString = (criteria[index].equals(""));

	    final boolean criteriaMatches = (subcontractorFields[index]
		    .contains(criteria[index]));

	    // TODO what about .startsWith?

	    // if (isEmptyString) {
	    // continue;
	    // }

	    if (criteriaMatches) {
		return true;
	    }

	}
	return false;
    }

    /**
     * Returns a string array representation of this Subcontractor object where
     * each subcontractor field n appears in array[n].
     *
     * @return String Array
     */
    public String[] toArray() {
	return new String[] { this.name, this.location, this.specialities,
		this.employeeSize, this.rate, this.owner };
    }

    /**
     * Returns a textual string representation of this Subcontractor object and
     * all it's fields. It uses reflection to get a list of all the fields
     * rather than hardcoding.
     *
     * @return String
     */
    @Override
    public String toString() {
	final StringBuilder result = new StringBuilder();
	final String newLine = System.getProperty("line.separator");

	result.append(this.getClass().getName());
	result.append(" Object {");
	result.append(newLine);

	// determine fields declared in this class only (no fields of
	// superclass)
	final Field[] fields = this.getClass().getDeclaredFields();

	// print field names paired with their values
	for (final Field field : fields) {
	    result.append("  ");
	    try {
		result.append(field.getName());
		result.append(": ");
		// requires access to private field:
		result.append(field.get(this));
	    } catch (final IllegalAccessException ex) {
		System.out.println(ex);
	    }
	    result.append(newLine);
	}
	result.append("}");

	return result.toString();
    }

    /**
     * Checks if two Subcontractor objects are identical based on their name and
     * location fields
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}

	// Fail fast if obj is null otherwise check the class type.
	// I am not using the common isinstance method here as this can return
	// true for subclasses
	if ((obj == null) || (obj.getClass() != this.getClass())) {
	    return false;
	}

	// cast obj to a SubContractor object and compare name and location
	// values
	final Subcontractor otherSubcontractor = (Subcontractor) obj;

	// TODO what if name or location is null
	return name.equals(otherSubcontractor.name)
		&& location.equals(otherSubcontractor.location);
    }

    /**
     * Overriding the hash function to implement our own unique hash code key
     * based on the subcontractor's name and location. This returns a positive
     * integer which we then use as the subcontractor's record number
     *
     */
    @Override
    public final int hashCode() {
	return Math.abs((name + location).hashCode());
    }

}
