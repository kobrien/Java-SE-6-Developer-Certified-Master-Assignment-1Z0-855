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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.db.SubcontractorRecord;

/**
 * The SubcontractorTableModel class acts as a proxy model to the actual
 * database by storing a list of subcontractor records which is then displayed
 * in the table view of the application's window. It extends AbstractTableModel
 * which provides the ability to fire data change events which the table view
 * then responds to as a listener and renders the results in the main window
 * ApplicationWindow. AbstractTableModel also provides other useful getter
 * methods for accessing the table contents of the table model.
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

    /**
     * Constructs a new SubcontractorTableModel object. The list of
     * subcontractor objects should be retrieved from the database via the
     * business component SubcontractorService and passed to this constructor as
     * an argument
     *
     * @param subscontractorRecords
     *            The list of subcontractor objects
     * */
    public SubcontractorTableModel(
	    final List<SubcontractorRecord> subscontractorRecords) {
	this.subscontractorRecords = subscontractorRecords;
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
     * Adds a new list of SubcontractorRecord objects to the model and fires a
     * table data changed event.
     *
     * @param subcontractorRecords
     *            The list of subcontractor record objects to update the table
     *            model data with
     *
     */
    public void updateData(final List<SubcontractorRecord> subcontractorRecords) {
	this.subscontractorRecords = subcontractorRecords;
	fireTableDataChanged();
    }
}
