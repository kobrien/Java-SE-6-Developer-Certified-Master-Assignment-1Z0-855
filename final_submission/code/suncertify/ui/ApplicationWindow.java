/*
 * @ApplicationWindow.java
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.*;

import suncertify.app.Application;
import suncertify.business.ServicesException;
import suncertify.business.SubcontractorService;
import suncertify.db.SubcontractorRecord;
import suncertify.ui.listeners.BookSubcontractorListener;
import suncertify.ui.listeners.SearchSubcontractorListener;

/**
 * The {@code ApplicationWindow} class represents the main graphical user
 * interface that the CSR uses to interact with the application. It is
 * instantiated in the actionPerformed method of the OK button listener for
 * whichever configuration dialog was launched (i.e. client or standalone). The
 * JFrame itself contains UI elements such as a JTable to list all the available
 * subcontractors and also provides other fields to search and book records as
 * per the application requirements. Because it is known which mode we are
 * executing in when we create this class we are able to "inject" the relevant
 * business component as a constructor argument. The interaction with the
 * business service component is then implemented in the action listeners of the
 * UI components i.e. Book button, Search button etc.
 *
 * @author Kieran O'Brien
 *
 */
public class ApplicationWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * The subcontractor business service that this window will interact with
     * via action listeners
     */
    private final SubcontractorService subcontractorService;

    /** Name of the subcontractor to search for */
    private final JTextField searchNameTextField = new JTextField(25);

    /** Location of the subcontractor to search for */
    private final JTextField searchLocationTextField = new JTextField(25);

    /** Searches a subcontractor using criteria from name and/or location fields */
    private final JButton searchButton = new JButton("Search");

    /** Subcontractor name label */
    private final JLabel searchNameLabel = new JLabel("Name: ");

    /** Subcontractor location label */
    private final JLabel searchLocationLabel = new JLabel("Location: ");

    /** Displays the subcontractor records from the table model */
    private final JTable tableView;

    /** Stores the subcontractor contents from the DB */
    private final SubcontractorTableModel tableModel;

    /** Books a record that is selected */
    private final JButton bookButton = new JButton("Book");

    /** Scroll up and down the table view */
    private JScrollPane scrollPane;

    /** Panel to hold the search UI components */
    private JPanel searchPanel;

    /** Panel to hold the booking UI components */
    private JPanel bookingPanel;

    /** contains both the search and bookings panel */
    private JPanel containerPanel;

    /**
     * Constructs a new {@code ApplicationWindow} object for when running the
     * application in client or standalone mode. This constructor is called when
     * the user clicks OK on the relevant configuration dialog
     *
     * @param subcontractorService
     *            The relevant business component for whichever mode is being
     *            executed i.e. {@code DefaultSubcontractorService} or
     *            {@code RMIRemoteSubcontractorService}
     * @throws RemoteException
     */
    public ApplicationWindow(final SubcontractorService subcontractorService)
	    throws RemoteException {

	super("Bodgitt and Scarper, LLC. Home Improvement Contractor Service");
	this.subcontractorService = subcontractorService;
	final List<SubcontractorRecord> subscontractorRecords = subcontractorService
		.getAllSubcontractors();

	this.tableModel = new SubcontractorTableModel(subscontractorRecords);

	this.tableView = new JTable();

	this.tableView.setModel(this.tableModel);

	this.tableView.setSelectionMode(0);

	initContainerPanel();
	add(this.containerPanel, "South");

	addActionListeners();

	addShutdownHook(subcontractorService);

	setDefaultCloseOperation(3);

	setSize(1000, 500);

	setVisible(true);

	setLocationRelativeTo(null);

	setResizable(false);
    }

    /**
     * @param subcontractorService
     *
     */
    private void addShutdownHook(final SubcontractorService subcontractorService) {
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    @Override
	    public void run() {
		try {
		    subcontractorService.saveData();
		} catch (final RemoteException | ServicesException e) {
		    Application
			    .displayErrorMessageAndExit("Encounted a problem saving data back to file. Error Message: "
				    + e.getMessage());
		    JOptionPane.showMessageDialog(null, e.getMessage(),
			    "Application Exception", JOptionPane.ERROR_MESSAGE);
		}

	    }
	});

    }

    private void addActionListeners() {
	final BookSubcontractorListener bookSubcontractorListener = new BookSubcontractorListener(
		this, this.subcontractorService);

	final SearchSubcontractorListener searchSubcontractorListener = new SearchSubcontractorListener(
		this, this.subcontractorService);

	this.bookButton.addActionListener(bookSubcontractorListener);
	this.searchButton.addActionListener(searchSubcontractorListener);
    }

    /**
     * Returns a reference to the table view which displays all the
     * subcontractor records. This can be used to retrieve a specific
     * subcontractor record and/or values by one of the controllers i.e. Action
     * Listeners
     * 
     * @return The table view
     */
    public JTable getTableView() {
	return this.tableView;
    }

    /**
     * Returns the name value entered in the location text field
     * 
     * @return The subcontractor name
     */
    public String getSubcontractorName() {
	return this.searchNameTextField.getText();
    }

    /**
     * Returns the location value entered in the location text field
     * 
     * @return The subcontractor location
     */
    public String getSubcontractorLocation() {
	return this.searchLocationTextField.getText();
    }

    private void initContainerPanel() {
	this.containerPanel = new JPanel(new BorderLayout());

	initSearchPanel();
	this.containerPanel.add(this.searchPanel, BorderLayout.NORTH);

	// Add all the UI elements to the content pane
	addScrollPane();
	this.add(this.scrollPane, BorderLayout.CENTER);

	initBookingPanel();
	this.containerPanel.add(this.bookingPanel, BorderLayout.SOUTH);

	this.add(this.containerPanel, BorderLayout.SOUTH);
    }

    private void addScrollPane() {
	this.scrollPane = new JScrollPane(this.tableView);
	this.scrollPane.setSize(600, 400);
    }

    /**
     * Initializes the search panel with all it's search UI components
     */
    private void initSearchPanel() {
	this.searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	searchPanel.add(this.searchNameLabel);
	searchPanel.add(this.searchNameTextField);
	searchPanel.add(this.searchLocationLabel);
	searchPanel.add(this.searchLocationTextField);
	searchPanel.add(this.searchButton);
    }

    /**
     *
     * Initializes the booking panel with all it's search UI components
     *
     */
    private void initBookingPanel() {
	bookingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	bookingPanel.add(this.bookButton);
    }

    /**
     * Resets both the name and location text fields to empty values
     *
     */
    public void resetTextFields() {
	this.searchNameTextField.setText("");
	this.searchLocationTextField.setText("");
    }

    /**
     * Retrieves all subcontractor records and updates the table model which as
     * a result updates the table view of the main application. This method
     * should be called by a controller when data has been changed via a service
     * request for example (book/search).
     */
    public void updateData() {
	try {
	    final List<SubcontractorRecord> subscontractorRecords = this.subcontractorService
		    .getAllSubcontractors();
	    this.tableModel.updateData(subscontractorRecords);
	} catch (final RemoteException e) {
	    Application
		    .displayErrorMessageAndExit("Error occurred retrieving remote data: "
			    + e.getMessage());
	}
    }

    /**
     * Updates the data in the table model with the specified subcontractor
     * records which as a result updates the table view of the main application.
     * This method can be called for example to display the results of a search,
     * or any other future functionality that needs to show a select number of
     * records in the table view.
     * 
     * @param subcontractorRecords
     */
    public void setData(final List<SubcontractorRecord> subcontractorRecords) {
	this.tableModel.updateData(subcontractorRecords);
    }

}
