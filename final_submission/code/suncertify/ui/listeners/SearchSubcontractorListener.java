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

    /**
     * Creates a new SearchSubcontractorListener object
     *
     * @param window
     *            The ApplicationWindow main window class
     * @param subcontractorService
     *            The SubcontractorService business service
     */
    public SearchSubcontractorListener(final ApplicationWindow window,
	    final SubcontractorService subcontractorService) {
	this.window = window;
	this.subcontractorService = subcontractorService;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
	final String name = this.window.getSubcontractorName();
	final String location = this.window.getSubcontractorLocation();

	List<SubcontractorRecord> returnedRecords = null;
	try {
	    if ((!name.isEmpty()) && (location.isEmpty())) {
		returnedRecords = this.subcontractorService
			.findSubcontractorsByName(name);
	    } else if ((name.isEmpty()) && (!location.isEmpty())) {
		returnedRecords = this.subcontractorService
			.findSubcontractorsByLocation(location);
	    } else if ((!name.isEmpty()) && (!location.isEmpty())) {
		returnedRecords = this.subcontractorService
			.findSubcontractorsByNameAndLocation(name, location);
	    } else {
		this.window.updateData();
	    }
	} catch (final RemoteException e) {
	    Application.displayErrorMessageAndExit(e.getMessage());
	} catch (final SubcontractorNotFoundException e1) {
	    JOptionPane.showMessageDialog(null, e1.getMessage(),
		    "Application Exception", 0);
	}
	if (returnedRecords != null) {
	    if (returnedRecords.size() == 0) {
		JOptionPane.showMessageDialog(null,
			"No match found for search", "No Match", 1);

		this.window.updateData();
	    } else {
		this.window.setData(returnedRecords);
	    }
	}
	this.window.resetTextFields();
    }
}
