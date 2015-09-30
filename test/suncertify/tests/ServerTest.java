package suncertify.tests;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.business.RMIRemoteSubcontractorService;
import suncertify.business.RemoteSubcontractorService;
import suncertify.db.*;

public class ServerTest {

    // Here you can change the IP address with the IP address of the server in
    // your LAN
    private static final String IP_ADDRESS = "127.0.0.1";
    private final static String FILE_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";

    private static final int PORT_NUMBER = 1099;

    public static void main(final String[] args) {
	startServer();
	testConnection();
    }

    public static void startServer() {
	try {
	    // The LocalDB interface extends the interface provided by Sun
	    final IDatabase db = new Data(FILE_PATH);

	    final RemoteSubcontractorService services = new RMIRemoteSubcontractorService(
		    db);

	    services.startServer(PORT_NUMBER);
	} catch (final RemoteException | DatabaseException exception) {
	    System.out.println(exception.getMessage());
	}
    }

    public static void testConnection() {
	try {
	    final Registry registry = LocateRegistry.getRegistry(IP_ADDRESS,
		    PORT_NUMBER);

	    registry.lookup(RMIRemoteSubcontractorService.SERVER_NAME);

	    // Theoretically, this isn't necessary, because if the lookup is not
	    // successfull,
	    // then a NotBoundException will be thrown
	    System.out.println("The server was started successfully!");
	} catch (final RemoteException exception) {
	    System.out.println(exception.getMessage());
	} catch (final NotBoundException exception) {
	    System.out.println(exception.getMessage());
	}
    }
}