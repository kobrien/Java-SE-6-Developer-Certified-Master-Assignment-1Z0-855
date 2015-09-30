/*
 * @ConfigurationPropertiesManager.java
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

import java.io.*;
import java.util.Properties;

/**
 * {@code ConfigurationPropertiesManager} manages the configuration data for the
 * application. It is responsible for reading and writing all configuration
 * properties to the suncertify.properties file when the application starts
 *
 * @author Kieran O'Brien
 *
 */
public class ConfigurationPropertiesManager {
    /** This file object represents the configuration file */
    private File propertiesFile = null;

    /**
     * This properties object is used to store the list of key value strings in
     * memory when reading/writing to the file stream
     */
    private Properties properties = null;

    /** The name of the configuration properties file */
    private static final String PROPERTIES_FILE = "suncertify.properties";

    /** The path to the database file when running in standalone mode */
    public static final String STANDALONE_DB_PATH = "standalone.databasePath";

    /** The path to the database file when running in server mode */
    public static final String SERVER_DB_PATH = "server.databasePath";

    /** The server port when running in server mode */
    public static final String SERVER_PORT = "server.serverPort";

    /** The server host or IP when running in client mode */
    public static final String CLIENT_SERVER_HOST = "client.serverHost";

    /** The server port when running in client mode */
    public static final String CLIENT_SERVER_PORT = "client.serverPort";

    /**
     * Constructs a new {@code ConfigurationPropertiesManager} object
     *
     * @throws IOException
     */
    public ConfigurationPropertiesManager() throws IOException {
	// first check if the properties file exists, otherwise create it
	this.propertiesFile = new File(PROPERTIES_FILE);

	if (!this.propertiesFile.exists()) {
	    this.propertiesFile.createNewFile();
	}
	this.properties = new Properties();
	readPropertiesFromFile();
    }

    /**
     * Reads the named properties from the file input stream into the static
     * properties object
     *
     * @throws IOException
     *
     */
    private void readPropertiesFromFile() throws IOException {

	final InputStream inputStream = new FileInputStream(PROPERTIES_FILE);
	if (inputStream != null) {
	    // load the properties into the properties object
	    properties.load(inputStream);
	}

    }

    /**
     * Writes the named properties in the static properties object to the output
     * stream
     *
     * @throws IOException
     */
    public void writePropertiesToFile() throws IOException {
	final OutputStream outputStream = new FileOutputStream(
		this.propertiesFile);
	this.properties.store(outputStream, "Configuration properties");
    }

    /**
     * Returns the value for a named property if found otherwise returns an
     * empty String
     *
     * @param propertyName
     *            The property key
     * @return The property value
     */
    public String getProperty(final String propertyName) {
	return this.properties.getProperty(propertyName, "");
    }

    /**
     * Sets the value for a named property
     *
     * @param name
     *            The named property
     * @param value
     *            The value to set
     */

    public void setProperty(final String propertyName, final String value) {
	this.properties.setProperty(propertyName, value);
    }
}
