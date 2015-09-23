/*
 * @BookSubcontractorListener.java
 * 20 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import suncertify.app.Application;
import suncertify.business.*;
import suncertify.ui.ApplicationWindow;
import suncertify.ui.SubcontractorTableModel;

public class BookSubcontractorListener implements ActionListener {
    private final ApplicationWindow window;
    private final SubcontractorService subcontractorService;

    public BookSubcontractorListener(final ApplicationWindow window,
	    final SubcontractorService subcontractorService) {
	this.window = window;
	this.subcontractorService = subcontractorService;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
	final JTable tableView = window.getTableView();
	final SubcontractorTableModel tableModel = window.getTableModel();

	final int selectedRecord = window.getTableView().getSelectedRow();

	// Check that a record was actually selected
	if (selectedRecord >= 0) {
	    boolean validCustomerID = false;
	    String customerID = null;
	    // If the user enters an ID that is not the correct length, display
	    // an error message and repeat the process until they do
	    do {
		customerID = JOptionPane.showInputDialog(null,
			"Please enter 8 digit customer ID", "Booking",
			JOptionPane.OK_CANCEL_OPTION);

		if (customerID.length() != 8) {
		    JOptionPane.showMessageDialog(null,
			    "Customer ID must be 8 digits", "Invalid Value",
			    JOptionPane.ERROR_MESSAGE);
		} else {
		    validCustomerID = true;
		}
	    } while (!validCustomerID);

	    // Get the record number value from the table view
	    final String cellValue = (String) tableView.getValueAt(
		    selectedRecord, 0);

	    final int subcontractorRecordNumber = Integer.valueOf(cellValue);

	    try {
		this.subcontractorService.bookSubcontractor(
			subcontractorRecordNumber, customerID);
	    } catch (final SubcontractorNotFoundException e) {
		JOptionPane.showMessageDialog(null, e.getMessage(),
			"Application Exception", JOptionPane.ERROR_MESSAGE);
	    } catch (final SubcontractorAlreadyBookedException e1) {
		JOptionPane.showMessageDialog(null, e1.getMessage(),
			"Application Exception", JOptionPane.ERROR_MESSAGE);
	    } catch (final RemoteException e2) {
		Application.displayErrorMessageAndExit(e2.getMessage());
	    }

	    tableModel.updateData();

	}
    }
}
