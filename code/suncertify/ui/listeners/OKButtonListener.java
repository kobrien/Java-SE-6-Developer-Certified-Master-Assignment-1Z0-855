/*
 * @OKButtonListener.java
 * 15 Sep 2015
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

import suncertify.business.SubcontractorService;
import suncertify.ui.*;

/**
 * {@code OKButtonListener} is an abstract class that should be implemented by
 * the OK button listener of either the client or standalone configuration
 * dialog classes. The abstract method {@code getService} should be overridden
 * by the client or standalone OK button listener to return the relevant
 * business service implementation of type {@code SubcontractorService}. This
 * service is then injected into the main application GUI window
 * {@code ApplicationWindow}.
 *
 * @author Kieran O'Brien
 *
 */
public abstract class OKButtonListener implements ActionListener {

    /**
     * {@code ConfigDialog} is the root interface for all configuration dialogs
     * (standalone, client and server). It's implementation will either be of
     * type {@code StandaloneConfigDialog}, {@code ClientConfigDialog} or
     * {@code ServerConfigDialog}
     */
    protected final ConfigDialog configDialog;

    /**
     * Constructor for {@code OKButtonListener}
     *
     * @param launchManager
     *            The type of configuration dialog that is currently being
     *            displayed
     */
    protected OKButtonListener(final ConfigDialog configDialog) {
	super();
	this.configDialog = configDialog;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    if (validateConfigurationProperties()) {
		// Get the relevant business service implementation
		final SubcontractorService subcontractorService = getService();

		System.out.println(subcontractorService);
		if (subcontractorService == null) {
		    throw new StartException(
			    "Could not retrieve business service");
		}

		// save the config properties that were entered to file
		saveConfigurationProperties();

		configDialog.closeWindow();

		// Create the main GUI window injecting the business service
		// implementation that is relevant to this dialog mode
		new ApplicationWindow(subcontractorService);

	    }
	} catch (final StartException | IOException exception) {
	    final String message = "Encountered an error starting the application \nError message: "
		    + exception.getMessage();
	    JOptionPane.showMessageDialog(null, message, "Error",
		    JOptionPane.ERROR_MESSAGE);
	    configDialog.closeWindow();
	}

    }

    /**
     * Returns an implementation of the business service interface
     * {@code SubcontractorService}. Either local or remote
     *
     * @return Either {@code DefaultSubcontractorService} or
     *         {@code RMIRemoteSubcontractorService}
     */
    protected abstract SubcontractorService getService() throws StartException;

    /**
     * @return The configuration dialog that this ok button listener belongs to
     */
    protected ConfigDialog getConfigDialog() {
	return this.configDialog;
    }

    /**
     * Saves the configuration properties entered by the user to the relevant
     * configuration file
     *
     * @throws IOException
     */
    protected abstract void saveConfigurationProperties() throws IOException;

    /**
     * Validates that the configuration properties entered by the user are of
     * the correct type and length for this particular configuration dialog
     */
    protected abstract boolean validateConfigurationProperties();

}
