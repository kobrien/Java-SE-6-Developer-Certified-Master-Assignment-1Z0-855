/*
 * @ServerWindow.java
 * 23 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;

import suncertify.app.Application;
import suncertify.business.ServicesException;
import suncertify.business.SubcontractorService;

/**
 * The ServerWindow class represents a small window used to display to the user
 * that the server has been started. It includes a stop button that allows the
 * user to stop the server which will in turn save all data in cache back to
 * disk (via the save method in DefaultSubcontractorService.saveData())
 *
 * @author Kieran O'Brien
 *
 */
public class ServerWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JLabel serverPortLabel = new JLabel(
	    "Server running on port: ");
    private final JTextField serverPortTextField = new JTextField();
    private final JButton stopServerButton = new JButton("Stop");

    /**
     * Creates a new ServerWindow object
     *
     * @param subcontractorService
     *            The business service
     * @param serverPort
     *            The port in which the server is running
     */
    public ServerWindow(final SubcontractorService subcontractorService,
	    final int serverPort) {
	super("Bodgitt and Scarper, LLC. Home Improvement Contractor Service");

	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize(500, 100);

	this.serverPortTextField.setText(String.valueOf(serverPort));

	addUIComponents();

	addActionListeners();

	// Add shutdown hook to save data from cache back to disk when the
	// server terminates
	addShutdownHook(subcontractorService);

	this.setVisible(true);
	this.setLocationRelativeTo(null);
	this.setResizable(false);

    }

    /**
     * Adds an action listener to the stop button, when clicked it will exit
     * which will trigger the shutdown hook to execute saving the cache contents
     * back to disk
     */
    private void addActionListeners() {
	this.stopServerButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		System.exit(0);
	    }
	});
    }

    /**
     * Registers a shutdown hook to save the contents of cache back to disk.
     * This method will get executed when the JVM exits
     *
     * @param subcontractorService
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

    /**
     * Adds the server port label, textfield, and stop button to the content
     * pane of this JFrame using GridBag layout
     */
    private void addUIComponents() {
	this.serverPortTextField.setEditable(false);

	final Container pane = this.getContentPane();
	pane.setLayout(new GridBagLayout());

	// Add the server port JLabel component
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 3;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	pane.add(this.serverPortLabel, constraints);

	// Add the server port JTextfield component
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 2;
	constraints.gridwidth = 1;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	pane.add(this.serverPortTextField, constraints);

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.gridwidth = 3;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.insets = new Insets(5, 25, 5, 25);
	pane.add(this.stopServerButton, constraints);
    }
}
