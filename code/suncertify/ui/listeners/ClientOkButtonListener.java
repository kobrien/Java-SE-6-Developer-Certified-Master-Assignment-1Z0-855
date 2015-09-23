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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.app.Application;
import suncertify.business.*;
import suncertify.ui.*;

public class ClientOkButtonListener extends OKButtonListener {

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

	RemoteSubcontractorService remoteService = null;

	try {
	    final Registry registry = LocateRegistry.getRegistry(serverHost,
		    serverPort);
	    remoteService = (RemoteSubcontractorService) registry
		    .lookup(RMIRemoteSubcontractorService.SERVER_NAME);
	} catch (final RemoteException | NotBoundException e) {
	    Application.displayErrorMessageAndExit(e.getMessage());
	}

	return remoteService;

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
