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

import javax.swing.*;

import suncertify.app.Application;
import suncertify.business.ServicesException;
import suncertify.business.SubcontractorService;
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
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize(1000, 500);

	// Populate the table model with the list of subcontractors
	this.tableModel = new SubcontractorTableModel(subcontractorService);

	// Create the JTable
	this.tableView = new JTable();

	// Add the table model to the view which registers a listener for any
	// new changes to the model
	this.tableView.setModel(this.tableModel);

	// Modify the table view so that the CSR can only select one record at a
	// time
	this.tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	// this.tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

	// Creates the panels using a simple border layout as well as some other
	// UI initialization
	// adds all panels to this (with separate layouts for each panel)
	initContainerPanel();

	this.add(this.containerPanel, BorderLayout.SOUTH);

	// Add the action listeners "controllers" to the UI elements
	addActionListeners(subcontractorService);

	// add shutdown hook
	addShutdownHook(subcontractorService);

	this.setVisible(true);
	this.setLocationRelativeTo(null);
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

    // TODO - docs
    // need to pass the SubcontractorService reference as these action listeners
    // are essentially the controllers of the application, they need to have a
    // service implementation to communicate with
    private void addActionListeners(
	    final SubcontractorService subcontractorService) {
	final BookSubcontractorListener bookSubcontractorListener = new BookSubcontractorListener(
		this, subcontractorService);
	final SearchSubcontractorListener searchSubcontractorListener = new SearchSubcontractorListener(
		this, subcontractorService);

	this.bookButton.addActionListener(bookSubcontractorListener);
	this.searchButton.addActionListener(searchSubcontractorListener);

    }

    public JTable getTableView() {
	return this.tableView;
    }

    public SubcontractorTableModel getTableModel() {
	return this.tableModel;
    }

    public String getSubcontractorName() {
	return this.searchNameTextField.getText();
    }

    public String getSubcontractorLocation() {
	return this.searchLocationTextField.getText();
    }

    // TODO understand the border layout
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

}
