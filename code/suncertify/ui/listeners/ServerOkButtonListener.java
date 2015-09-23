/*
 * @ServerOkButtonListener.java
 * 23 Sep 2015
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
import java.io.IOException;

import javax.swing.JOptionPane;

import suncertify.business.RMIRemoteSubcontractorService;
import suncertify.db.*;
import suncertify.ui.ServerConfigDialogGUI;
import suncertify.ui.ServerWindow;

public class ServerOkButtonListener implements ActionListener {
    protected final ServerConfigDialogGUI configDialog;

    public ServerOkButtonListener(final ServerConfigDialogGUI configDialog) {
	super();
	this.configDialog = configDialog;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    if (configDialog.validateConfigurationProperties()) {

		// Save the server config properties that were entered to file
		configDialog.saveConfigurationProperties();

		// CLose the server config window
		configDialog.closeWindow();

		// Get the data we need to start the server
		final int serverPort = Integer.valueOf(configDialog
			.getServerPort());

		final String databasePath = configDialog.getDBPath();

		// Create a local connection to the {@code IDatabase} interface
		final IDatabase database = new Data(databasePath);

		// Create the RMI remote business service passing in the
		// database reference
		final RMIRemoteSubcontractorService server = new RMIRemoteSubcontractorService(
			database);

		// Start the server
		server.startServer(serverPort);

		// Display the server window GUI
		new ServerWindow(server, serverPort);
	    }
	} catch (DatabaseException | IOException exception) {
	    final String message = "Encountered an error starting the server \nError message: "
		    + exception.getMessage();
	    JOptionPane.showMessageDialog(null, message, "Error",
		    JOptionPane.ERROR_MESSAGE);
	    configDialog.closeWindow();
	}
    }
}
