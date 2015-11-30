/*
 * @Application.java
 * 9 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.app;

import javax.swing.JOptionPane;

import suncertify.ui.*;

/**
 * The Application class is the starting point of the application. This class
 * contains the application's main method which is responsible for parsing the
 * command line argument entered by the user and launching the relevant
 * configuration dialog GUI.
 *
 * @author Kieran O'Brien
 *
 */
public class Application {
    // Usage text to display to the user
    private static final String USAGE = "\nUSAGE: java -jar <filename> [mode]\n\n"
	    + "where <filename> is the name of the jar\n"
	    + "where [mode] is either one of the following parameters or left blank:\n"
	    + "\n   server: Runs the server program"
	    + "\n   alone: Runs in standalone mode"
	    + "\n   (default): Runs the client in networked mode";

    /**
     * Parses command line arguments and launches the application according to
     * the specified mode
     *
     * @param args
     *            The supplied command line arguments
     */
    public static void main(final String[] args) {
	final String applicationMode = processCommandLineArguments(args);

	try {
	    if (applicationMode.equals("server")) {
		new ServerConfigDialogGUI();
	    } else if (applicationMode.equals("alone")) {
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
     * Returns the application mode based on what the user entered.
     *
     * @param args
     *            The list of command line arguments
     * @return String
     */
    private static String processCommandLineArguments(final String[] args) {
	String applicationMode = "";
	if (args.length == 0) {
	    applicationMode = "client";
	}

	else if (args.length == 1) {
	    final String arg = args[0].toLowerCase().trim();

	    if (arg.equals("server")) {
		applicationMode = "server";
	    } else if (arg.equals("alone")) {
		applicationMode = "alone";
	    } else {
		printErrorMessageAndExit("Invalid mode argument for application: '"
			+ arg + "'");
	    }

	} else {
	    printErrorMessageAndExit("Too many arguments passed");
	}
	return applicationMode;
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
