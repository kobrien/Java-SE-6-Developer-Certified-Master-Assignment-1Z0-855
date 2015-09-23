/*
 * @SearchSubcontractorListener.java
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
import java.util.List;

import javax.swing.JOptionPane;

import suncertify.app.Application;
import suncertify.business.SubcontractorNotFoundException;
import suncertify.business.SubcontractorService;
import suncertify.db.DBMain;
import suncertify.db.SubcontractorRecord;
import suncertify.ui.ApplicationWindow;
import suncertify.ui.SubcontractorTableModel;

/**
 * {@code SearchSubcontractorListener} listens to events on the search button in
 * the main application window. When it is clicked the text values are retrieved
 * from the name and location text fields. We first check to see if the user is
 * searching by both name, location, or both and then call the corresponding
 * search method in the business service, which ultimately calls the find method
 * in {@link DBMain}.
 *
 * @author Kieran O'Brien
 *
 */
public class SearchSubcontractorListener implements ActionListener {
    private final ApplicationWindow window;
    private final SubcontractorService subcontractorService;

    public SearchSubcontractorListener(final ApplicationWindow window,
	    final SubcontractorService subcontractorService) {
	this.window = window;
	this.subcontractorService = subcontractorService;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
	final String name = window.getSubcontractorName();
	final String location = window.getSubcontractorLocation();

	List<SubcontractorRecord> returnedRecords = null;
	final SubcontractorTableModel tableModel = window.getTableModel();

	try {
	    // First check if we are searching by name, location, or both
	    if (!name.isEmpty() && location.isEmpty()) {
		// Searching by name
		returnedRecords = this.subcontractorService
			.findSubcontractorsByName(name);
	    } else if (name.isEmpty() && !location.isEmpty()) {
		// Searching by location
		returnedRecords = this.subcontractorService
			.findSubcontractorsByLocation(location);
	    } else if (!name.isEmpty() && !location.isEmpty()) {
		// Searching with both name and location
		returnedRecords = this.subcontractorService
			.findSubcontractorsByNameAndLocation(name, location);
	    } else {
		// both fields are empty so just show all the records from
		// persistence
		tableModel.updateData();
	    }
	} catch (final RemoteException e) {
	    Application.displayErrorMessageAndExit(e.getMessage());
	} catch (final SubcontractorNotFoundException e1) {
	    JOptionPane.showMessageDialog(null, e1.getMessage(),
		    "Application Exception", JOptionPane.ERROR_MESSAGE);
	}

	if (returnedRecords != null) {
	    if (returnedRecords.size() == 0) {
		JOptionPane.showMessageDialog(null,
			"No match found for search", "No Match",
			JOptionPane.INFORMATION_MESSAGE);
	    } else {
		tableModel.setData(returnedRecords);
	    }
	}
	window.resetTextFields();
    }

}