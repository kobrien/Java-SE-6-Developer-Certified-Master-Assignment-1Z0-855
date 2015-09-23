/*
 * Runner.java
 * Main starting point for application
 *
 */
package suncertify.app;

import javax.swing.JOptionPane;

import suncertify.ui.*;

public class Application {

    private static String APPLICATION_MODE = null;

    private static final String USAGE = "\nUSAGE: java -jar <filename> [mode]\n\n"
	    + "where <filename> is the name of the jar\n"
	    + "where [mode] is either one of the following parameters or left blank:\n"
	    + "\n   server: Runs the server program"
	    + "\n   alone: Runs in standalone mode"
	    + "\n   (default): Runs the client in networked mode";

    /**
     * Parses command line arguments and launches the application
     *
     * @param args
     *            The supplied command line arguments
     */
    public static void main(final String[] args) {
	processCommandLineArguments(args);

	try {
	    if (APPLICATION_MODE == "server") {
		new ServerConfigDialogGUI();
	    } else if (APPLICATION_MODE == "alone") {
		new StandaloneConfigDialogGUI();
	    } else {
		new ClientConfigDialogGUI();
	    }
	} catch (final Exception e) {
	    displayErrorMessageAndExit(e.getMessage());
	}
    }

    /**
     * Parses the command line arguments and validates that they are correct.
     * Sets the application mode based on what the user entered.
     *
     * @param args
     *            The list of command line arguments
     */
    private static void processCommandLineArguments(final String[] args) {

	if (args.length == 0) {
	    APPLICATION_MODE = "client";
	}

	else if (args.length == 1) {
	    final String arg = args[0].toLowerCase().trim();

	    if (arg.equals("server")) {
		APPLICATION_MODE = "server";
	    } else if (arg.equals("alone")) {
		APPLICATION_MODE = "alone";
	    } else {
		printErrorMessageAndExit("Invalid mode argument for application: '"
			+ arg + "'");
	    }

	} else {
	    printErrorMessageAndExit("Too many arguments passed");
	}

    }

    /**
     * Prints a console error message to the user and exits the application with
     * -1 return code
     *
     * @param message
     *            The error message
     */
    public static void printErrorMessageAndExit(final String message) {
	System.out.println("\nThe application was unable to start "
		+ "due to the following error: \n\n" + message);
	System.out.println(USAGE);
	System.exit(-1);
    }

    /**
     * Displays a graphical error message to the user and exits with -1
     *
     * @param message
     *            The error message
     */
    public static void displayErrorMessageAndExit(final String message) {
	JOptionPane.showMessageDialog(null, message, "Application Exception",
		JOptionPane.ERROR_MESSAGE);
	System.exit(-1);
    }
}
