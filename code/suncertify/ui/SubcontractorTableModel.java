/*
 * @SubcontractorTableModel.java
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

import java.rmi.RemoteException;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.app.Application;
import suncertify.business.SubcontractorService;
import suncertify.db.SubcontractorRecord;

/**
 * The {@code SubcontractorTableModel} class is "the model" which stores the
 * current subcontractor record contents that is being displayed in the
 * application's window table view. It extends {@code AbstractTableModel} to
 * avail of the getter methods for accessing the table contents
 *
 * @author Kieran O'Brien
 *
 */
public class SubcontractorTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    /**
     * The column names used in the JTable to represent each subcontractor field
     */
    private final String[] columnNames = { "Subcontractor RecNo", "Name",
	    "Location", "Specialities", "Number of Employees", "Hourly Rate",
    "Booked By Customer ID" };

    /** The list of subcontractor records returned from the database */
    private List<SubcontractorRecord> subscontractorRecords;

    /** The business service which this model will request data from */
    private final SubcontractorService subcontractorService;

    /**
     * Constructs a new {@code SubcontractorTableModel} object. The list of
     * subcontractor objects should be retrieved from the database via the
     * business component {@code SubcontractorService} and passed to this
     * constructor as an argument
     *
     * @param subcontractorService
     *
     * @param subscontractorRecords
     *            The list of subcontractor objects
     * */
    public SubcontractorTableModel(
	    final SubcontractorService subcontractorService) {
	this.subcontractorService = subcontractorService;

	// Get the list of subcontractor records
	try {
	    this.subscontractorRecords = subcontractorService
		    .getAllSubcontractors();
	} catch (final RemoteException e) {
	    Application
	    .displayErrorMessageAndExit("Error occurred retrieving remote data: "
		    + e.getMessage());
	}
    }

    @Override
    public int getRowCount() {
	return subscontractorRecords.size();
    }

    @Override
    public int getColumnCount() {
	return columnNames.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
	final SubcontractorRecord subcontractor = subscontractorRecords
		.get(rowIndex);
	final String[] subcontractorFields = subcontractor.toArray();
	return subcontractorFields[columnIndex];
    }

    @Override
    public String getColumnName(final int column) {
	return columnNames[column];
    }

    /**
     * Adds a new list of {@code SubcontractorRecord} objects to the model and
     * fires a table data changed event. This method is used particular for the
     * search mechanism when we want to update the table model (and view) with a
     * specific list of subcontractors
     *
     * @param SubcontractorRecord
     *            The {@code SubcontractorRecord} object
     */
    public void setData(final List<SubcontractorRecord> subcontractorRecords) {
	this.subscontractorRecords = subcontractorRecords;
	fireTableDataChanged();
    }

    /**
     * Gets an updated list of subcontractors from the business service i.e.
     * persistence and fires a table data changed event notifying all listeners
     * that the model has changed i.e the view
     *
     */
    public void updateData() {
	try {
	    this.subscontractorRecords = subcontractorService
		    .getAllSubcontractors();
	} catch (final RemoteException e) {
	    Application
	    .displayErrorMessageAndExit("Error occurred retrieving remote data: "
		    + e.getMessage());
	}
	fireTableDataChanged();
    }
}
