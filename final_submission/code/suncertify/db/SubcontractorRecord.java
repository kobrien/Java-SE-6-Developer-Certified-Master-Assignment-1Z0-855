/*
 * @SubcontractorRecord.java
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

/**
 * The <code>SubcontractorRecord</code> class represents a subcontractor record.
 * It contains all fields that relate to a subcontractor record including the
 * initial valid byte value. This class is used in both the data and business
 * layers to represent a subcontractor record. It is serializable because it
 * will need to travel from one VM to another when the application is running in
 * server mode
 *
 * @author Kieran O'Brien
 *
 */

public class SubcontractorRecord implements Serializable {

    private static final long serialVersionUID = -2341613579485649698L;

    /** Boolean which marks the record as valid. False if record is deleted */
    private boolean validRecord = true;

    /** The subcontractor record number */
    private final int recordNumber;

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
    private String ownerID;

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
     * @param ownerID
     *            The owner customer ID that has booked this subcontractor
     */
    public SubcontractorRecord(final String name, final String location,
	    final String specialities, final String size, final String rate,
	    final String ownerID) {
	this.setName(name);
	this.setLocation(location);
	this.setSpecialities(specialities);
	this.setEmployeeSize(size);
	this.setHourlyRate(rate);
	this.setOwnerID(ownerID);

	// set the record number based on this object's hash code
	this.recordNumber = getRecordNumber();
    }

    /**
     * @return the name of this subcontractor
     */
    public String getName() {
	return this.name;
    }

    /**
     * @return the location for this subcontractor
     */
    public String getLocation() {
	return this.location;
    }

    /**
     * @return the specialties this subcontractor can perform
     */
    public String getSpecialities() {
	return this.specialities;
    }

    /**
     * @return the number of workers available for this subcontractor
     */
    public String getEmployeeSize() {
	return this.employeeSize;
    }

    /**
     * @return the hourly rate for this subcontractor
     */
    public String getHourlyRate() {
	return this.rate;
    }

    /**
     * @return the owner customer ID who currently owns this subcontractor
     */
    public String getOwnerID() {
	return this.ownerID;
    }

    /**
     * Updates each subcontractor field n with the data in data[n]. Attention as
     * this method will overwrite existing fields. Throws an
     * IllegalArgumentException if any of the data values are invalid
     *
     * @param data
     *            The string array of data to update each field with
     */
    public void update(final String[] data) {
	if (data == null) {
	    throw new IllegalArgumentException("Data  can not be null type");
	}
	if (data.length < this.toArray().length) {
	    throw new IllegalArgumentException(
		    "Data fields length must be of length "
			    + this.toArray().length);
	}

	// We never want to update the record number field so skip index 0
	this.setName(data[1]);
	this.setLocation(data[2]);
	this.setSpecialities(data[3]);
	this.setEmployeeSize(data[4]);
	this.setHourlyRate(data[5]);
	this.setOwnerID(data[6]);
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
     * Sets the owner customer ID for this subcontractor record.
     *
     * @param ownerID
     *            the owner customer Id to set as owner of this subcontractor
     */
    public void setOwnerID(final String ownerID) {
	if (ownerID == null) {
	    throw new IllegalArgumentException(
		    "Invalid owner ID value, value cannot be null");

	}

	this.ownerID = ownerID;
    }

    /**
     * @return True if the record is valid
     */
    public boolean isValidRecord() {
	return this.validRecord;
    }

    /**
     * Sets this subcontractor record as being an invalid deleted record.
     *
     * DESIGN DECISION: As i have not implemented the delete functionality this
     * method is not being used but in the future it can be used by the delete
     * method of the {@code Data} class which will result in the record not
     * being persisted back to disk. @see DBFileAccess#persistAllSubcontractors
     * for where a record gets persisted}
     */
    public void setRecordAsDeleted() {
	this.validRecord = false;
    }

    /**
     * Checks for each subcontractor's field n if it matches the specified
     * criteria[n]. A null value in criteria[n] matches any field value. A
     * non-null value in criteria[n] matches any field value that begins with
     * criteria[n]. (For example, "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria
     * @return True if a match was found
     */
    public boolean matchesCriteria(final String[] criteria) {
	final String[] subcontractorFields = this.toArray();

	// Iterate over the list of criteria and return true if there is a match
	for (int index = 0; index < criteria.length; index++) {

	    // Return true if we find a null value in criteria[n] as the
	    // requirement is 'a null value in criteria[n] matches
	    // any field value'
	    if (criteria[index] == null) {
		return true;
	    }

	    // empty string values will return true which we do not want
	    if (criteria[index].isEmpty()) {
		continue;
	    }

	    // the contains method returns true for empty strings thus
	    // satisfying the requirement 'a null value in criteria[n] matches
	    // any field value'
	    final boolean criteriaMatches = subcontractorFields[index]
		    .startsWith(criteria[index]);

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
	return new String[] { Integer.toString(this.recordNumber), this.name,
		this.location, this.specialities, this.employeeSize, this.rate,
		this.ownerID };
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
	if (obj == null || obj.getClass() != this.getClass()) {
	    return false;
	}

	// cast obj to a SubContractor object and compare name and location
	// values
	final SubcontractorRecord otherSubcontractor = (SubcontractorRecord) obj;

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

    /**
     * Returns the unique record number for this Subcontractor which is actually
     * this object's hash code
     *
     * @return The record number as a String
     */
    public int getRecordNumber() {
	return this.hashCode();
    }

    /**
     * Returns a string builder object which represents this subcontractor
     * object and all it's relevant data fields with the correct length of each
     * field measured in bytes according to the data schema. The length of each
     * field is specified in the class {@code DatabaseConstants}
     *
     * @return StringBuilder The StringBuilder object
     */
    public StringBuilder toStringBuilder() {

	int start = 0;

	// Create string builder object with correct size for the record fields
	final StringBuilder builder = new StringBuilder(new String(
		new byte[DatabaseConstants.CONTRACTOR_RECORD_LENGTH_BYTES]));

	// Write each subcontractor field to the string builder, incrementing
	// the start position with the length of each field
	final String name = this.getName();
	builder.replace(start, start + name.length(), name);
	start += DatabaseConstants.CONTRACTOR_NAME_LENGTH_BYTES;

	final String location = this.getLocation();
	builder.replace(start, start + location.length(), location);
	start += DatabaseConstants.CONTRACTOR_LOCATION_LENGTH_BYTES;

	final String specialities = this.getSpecialities();
	builder.replace(start, start + specialities.length(), specialities);
	start += DatabaseConstants.CONTRACTOR_SPECIALITIES_LENGTH_BYTES;

	final String employeeSize = this.getEmployeeSize();
	builder.replace(start, start + employeeSize.length(), employeeSize);
	start += DatabaseConstants.CONTRACTOR_EMPLOYEE_SIZE_LENGTH_BYTES;

	final String rate = this.getHourlyRate();
	builder.replace(start, start + rate.length(), rate);
	start += DatabaseConstants.CONTRACTOR_RATE_LENGTH_BYTES;

	final String owner = this.getOwnerID();
	builder.replace(start, start + owner.length(), owner);
	start += DatabaseConstants.CONTRACTOR_OWNER_LENGTH_BYTES;

	return builder;
    }

    /**
     * Checks the ownerID field to see if this subcontractor record is booked
     *
     * @return True if booked otherwise False
     */
    public boolean isBooked() {
	if (this.ownerID.isEmpty()) {
	    return false;
	} else {
	    return true;
	}
    }
}
