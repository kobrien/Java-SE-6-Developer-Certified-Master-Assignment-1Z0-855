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

/**
 * The ServerOkButtonListener does not extend the generic OKButtonListener class
 * like the other OK button listeners. The reason for this being that this OK
 * button does not launch the main application window after having retrieved a
 * business service, instead it starts the actual server and launches a very
 * small separate window to display to the user that it has been started. All
 * server details entered by the user are also saved to file as a result of
 * clicking this button
 *
 * @author Kieran O'Brien
 *
 */
public class ServerOkButtonListener implements ActionListener {
    protected final ServerConfigDialogGUI configDialog;

    /**
     * Creates a new ServerOkButtonListener object
     *
     * @param configDialog
     *            Reference to the server configuration dialog window
     */
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
