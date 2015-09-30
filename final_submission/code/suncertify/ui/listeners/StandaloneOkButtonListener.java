/*
 * @StandaloneOkButtonListener.java
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

import java.io.IOException;

import suncertify.business.DefaultSubcontractorService;
import suncertify.business.SubcontractorService;
import suncertify.db.*;
import suncertify.ui.*;

/**
 * StandaloneOkButtonListener extends the abstract class OKButtonListener and is
 * responsible for implementing the relevant methods in this class, such as
 * validating configuration data entered by the user, saving config data to
 * disk, and returning the correct business service for running in client mode.
 * The business service in this case is a local service and does not involve any
 * networking.
 *
 * @author Kieran O'Brien
 *
 */
public class StandaloneOkButtonListener extends AbstractOKButtonListener {

    /**
     * Constructs a new {@code StandaloneOkButtonListener} object
     *
     * @param configDialog
     *            The standalone configuration dialog
     */
    public StandaloneOkButtonListener(final ConfigDialog configDialog) {
	super(configDialog);
    }

    @Override
    protected SubcontractorService getService() throws StartException {
	final StandaloneConfigDialog standaloneDialog = (StandaloneConfigDialog) getConfigDialog();
	final String databasePath = standaloneDialog.getDBPath();
	final IDatabase database;

	try {
	    database = new Data(databasePath);
	} catch (final DatabaseException e) {
	    throw new StartException(
		    "Could not create new database instance - '"
			    + e.getMessage() + "'");
	}

	return new DefaultSubcontractorService(database);
    }

    @Override
    protected boolean validateConfigurationProperties() {
	final StandaloneConfigDialogGUI dialog = (StandaloneConfigDialogGUI) configDialog;
	return dialog.validateConfigurationProperties();
    }

    @Override
    protected void saveConfigurationProperties() throws IOException {
	final StandaloneConfigDialogGUI dialog = (StandaloneConfigDialogGUI) configDialog;
	dialog.saveConfigurationProperties();
    }
}
