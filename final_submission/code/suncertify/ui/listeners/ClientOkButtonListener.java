/*
 * @ClientOkButtonListener.java
 * 18 Sep 2015
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
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.app.Application;
import suncertify.business.RMIRemoteSubcontractorService;
import suncertify.business.SubcontractorService;
import suncertify.ui.*;

/**
 * ClientOkButtonListener extends the abstract class OKButtonListener and is
 * responsible for implementing the relevant methods in this class, such as
 * validating configuration data entered by the user, saving config data to
 * disk, and returning the correct business service for running in client mode.
 * The business service in this case is a remote service and is obtained by
 * querying the RMI Registry with the host details entered by the user.
 *
 * @author Kieran O'Brien
 *
 */
public class ClientOkButtonListener extends AbstractOKButtonListener {

    /**
     * Constructs a new {@code ClientOkButtonListener} object
     *
     * @param configDialog
     *            The client configuration dialog
     */
    public ClientOkButtonListener(final ConfigDialog configDialog) {
	super(configDialog);
    }

    @Override
    protected SubcontractorService getService() throws StartException {
	final ClientConfigDialog clientDialog = (ClientConfigDialog) getConfigDialog();

	final String serverHost = clientDialog.getServerHost();
	final int serverPort = Integer.parseInt(clientDialog.getServerPort());

	Remote remoteService = null;

	try {
	    final Registry registry = LocateRegistry.getRegistry(serverHost,
		    serverPort);
	    remoteService = registry
		    .lookup(RMIRemoteSubcontractorService.SERVER_NAME);
	} catch (final RemoteException | NotBoundException e) {
	    Application.displayErrorMessageAndExit(e.getMessage());
	}

	return (SubcontractorService) remoteService;

    }

    @Override
    protected boolean validateConfigurationProperties() {
	final ClientConfigDialogGUI dialog = (ClientConfigDialogGUI) configDialog;
	return dialog.validateConfigurationProperties();
    }

    @Override
    protected void saveConfigurationProperties() throws IOException {
	final ClientConfigDialogGUI dialog = (ClientConfigDialogGUI) configDialog;
	dialog.saveConfigurationProperties();
    }

}
