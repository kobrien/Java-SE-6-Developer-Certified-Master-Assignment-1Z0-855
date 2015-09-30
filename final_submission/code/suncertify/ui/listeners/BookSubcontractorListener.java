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

import suncertify.app.Application;
import suncertify.business.*;
import suncertify.ui.ApplicationWindow;

/**
 * BookSubcontractorListener acts as the controller (MVC pattern) for the book
 * button functionality. It implements ActionListener and when clicked it
 * validates that the customer ID entered by the user is valid, and then issues
 * a book request to the relevant business service
 *
 * @author Kieran O'Brien
 *
 */
public class BookSubcontractorListener implements ActionListener {
    private final ApplicationWindow window;
    private final SubcontractorService subcontractorService;

    /**
     * Constructs a new BookSubcontractorListener object
     *
     * @param window
     *            The ApplicationWindow main window class
     * @param subcontractorService
     *            The SubcontractorService business service
     */
    public BookSubcontractorListener(final ApplicationWindow window,
	    final SubcontractorService subcontractorService) {
	this.window = window;
	this.subcontractorService = subcontractorService;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
	final int selectedRecord = this.window.getTableView().getSelectedRow();
	if (selectedRecord >= 0) {
	    final String recordNumberCellValue = (String) this.window
		    .getTableView().getValueAt(selectedRecord, 0);

	    final int subcontractorRecordNumber = Integer.valueOf(
		    recordNumberCellValue).intValue();

	    final String customerIDCellValue = (String) this.window
		    .getTableView().getValueAt(selectedRecord, 6);
	    try {
		if (customerIDCellValue.length() > 0) {
		    throw new SubcontractorAlreadyBookedException(
			    "Subcontractor already booked");
		}
		final String customerID = getCustomerIDValue();
		if (customerID.length() != 8) {
		    return;
		}
		this.subcontractorService.bookSubcontractor(
			subcontractorRecordNumber, customerID);
		JOptionPane.showMessageDialog(null,
			"Successfully booked customer ID "
				+ subcontractorRecordNumber, "Booking", 1);
	    } catch (final SubcontractorNotFoundException e) {
		JOptionPane.showMessageDialog(null, e.getMessage(),
			"Application Exception", 0);
	    } catch (final SubcontractorAlreadyBookedException e1) {
		JOptionPane.showMessageDialog(null, e1.getMessage(),
			"Application Exception", 0);
	    } catch (final RemoteException e2) {
		Application.displayErrorMessageAndExit(e2.getMessage());
	    }
	    this.window.updateData();
	}
    }

    /**
     * Returns the customer ID value entered by the user, looping if it is not
     * valid. If the user clicks cancel an empty string is returned
     *
     * @return The customer ID
     */
    private String getCustomerIDValue() {
	boolean validCustomerID = false;
	String customerID = null;
	do {
	    customerID = JOptionPane.showInputDialog(null,
		    "Please enter 8 digit customer ID", "Booking", 0);
	    if (customerID == null) {
		return "";
	    }
	    if (customerID.length() == 8) {
		validCustomerID = true;
	    } else {
		JOptionPane.showMessageDialog(null,
			"Customer ID must be 8 digits", "Invalid Value", 0);
	    }
	} while (!validCustomerID);
	return customerID;
    }
}
