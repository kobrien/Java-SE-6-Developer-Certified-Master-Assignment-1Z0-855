/*
 * @StandaloneConfigDialogGUI.java
 * 18 Sep 2015
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
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import suncertify.ui.listeners.StandaloneOkButtonListener;

/**
 * {@code StandaloneConfigDialogGUI} implements {@code StandaloneConfigDialog}
 * interface and is responsible for getting the location of the database file
 * from the user when running the application in standalone mode
 *
 * @author Kieran O'Brien
 *
 */

public class StandaloneConfigDialogGUI extends JFrame implements
	StandaloneConfigDialog {

    private static final long serialVersionUID = 1L;

    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton browseButton = new JButton("Browse");
    private final JTextField dbFilePathTextField = new JTextField(50);
    private final JLabel dbFileLabel = new JLabel("Database File Path");
    private final JLabel dbErrorLabel = new JLabel(
	    "Database path can't be empty");

    private final ConfigurationPropertiesManager propertiesManager;

    /**
     * Constructs a new {@code StandaloneConfigDialogGUI} object
     *
     * @throws IOException
     */
    public StandaloneConfigDialogGUI() throws IOException {
	super("Standalone Configuration Dialog");

	// Modifies and disables the relevant JButtons/JLabels etc.
	disableErrorLabels();

	// Create a new configuration properties manager instance creating the
	// properties file if it doesn't already exist otherwise loading the
	// existing props
	this.propertiesManager = new ConfigurationPropertiesManager();

	// Load the existing DB file location into the JTextfield
	loadExistingConfigurationProperties();

	// Add the action listeners to the UI Components for this dialog
	addActionListeners();

	// Creates the UI layout and adds the UI components to this
	addUIComponents();

	// Set the window close button to exit the application
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	this.setVisible(true);

	// Size the window according to it's content
	this.pack();

	// Display the window in the center of the screen
	this.setLocationRelativeTo(null);

	setResizable(false);
    }

    @Override
    public void disableErrorLabels() {
	this.dbErrorLabel.setForeground(Color.RED);
	this.dbErrorLabel.setVisible(false);

    }

    @Override
    public void loadExistingConfigurationProperties() {
	final String dbFilePath = propertiesManager
		.getProperty(ConfigurationPropertiesManager.STANDALONE_DB_PATH);
	this.dbFilePathTextField.setText(dbFilePath);
    }

    @Override
    public void addActionListeners() {
	this.okButton.addActionListener(new StandaloneOkButtonListener(this));

	this.cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(final ActionEvent e) {
		System.exit(0);
	    }

	});

	this.browseButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		final JFileChooser fileBrowser = new JFileChooser(new File("."));
		final int exitCode = fileBrowser.showOpenDialog(null);

		if (exitCode == JFileChooser.APPROVE_OPTION) {
		    dbFilePathTextField.setText(fileBrowser.getSelectedFile()
			    .getAbsolutePath());
		}
	    }
	});
    }

    @Override
    public void addUIComponents() {
	final Container pane = this.getContentPane();
	pane.setLayout(new GridBagLayout());

	// Add the DB file JLabel component
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 3;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	pane.add(this.dbFileLabel, constraints);

	// Add the DB error JLabel component
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.gridwidth = 3;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	pane.add(this.dbErrorLabel, constraints);

	// Add the DB file JTextfield component
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 2;
	constraints.gridwidth = 1;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	pane.add(this.dbFilePathTextField, constraints);

	// Add the browse JButton component
	constraints = new GridBagConstraints();
	constraints.gridx = 2;
	constraints.gridy = 2;
	pane.add(this.browseButton, constraints);

	// Add the OK and cancel buttons to a JPanel and add this to the pane
	final JPanel buttonPanel = new JPanel();
	final GridLayout layout = new GridLayout(1, 2);
	layout.setHgap(50);
	buttonPanel.setLayout(layout);
	buttonPanel.add(this.okButton);
	buttonPanel.add(this.cancelButton);

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.gridwidth = 3;
	constraints.gridheight = 1;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.weightx = 1.0;
	constraints.insets = new Insets(5, 25, 5, 25);
	pane.add(buttonPanel, constraints);
    }

    @Override
    public void closeWindow() {
	this.setVisible(false);
	this.dispose();
    }

    @Override
    public String getDBPath() {
	return this.dbFilePathTextField.getText();
    }

    @Override
    public boolean validateConfigurationProperties() {
	boolean result = true;
	final String dbPath = this.getDBPath();

	if (dbPath.length() == 0) {
	    result = false;
	    this.dbErrorLabel.setVisible(true);
	}
	return result;
    }

    @Override
    public void saveConfigurationProperties() throws IOException {
	this.propertiesManager.setProperty(
		ConfigurationPropertiesManager.STANDALONE_DB_PATH,
		this.getDBPath());
	this.propertiesManager.writePropertiesToFile();
    }

}
