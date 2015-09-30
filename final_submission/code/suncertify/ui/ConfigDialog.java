/*
 * @ConfigDialog.java
 * 15 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.ui;

import java.io.IOException;

/**
 * {@code ConfigDialog} is the root interface of all the configuration dialog
 * interfaces i.e. (client, standalone, server)
 *
 * Each configuration dialog requires different configuration properties from
 * the user, those of which are clearly defined in each individual interface.
 *
 *
 * @author Kieran O'Brien
 *
 */
public interface ConfigDialog {

    /**
     * Closes the configuration dialog that is being displayed
     */
    void closeWindow();

    /**
     * Disables relevant error labels from being visible in the dialog
     */
    void disableErrorLabels();

    /**
     * Populate the configuration dialog text fields with the existing values
     * from the relevant properties file if present
     *
     * Design Note: This satisfies the project requirement
     * "configuration must be persistent between runs of the program"
     */
    void loadExistingConfigurationProperties();

    /**
     * Validates the specific properties for this configuration dialog
     *
     * @return True if properties are valid
     */
    boolean validateConfigurationProperties();

    /**
     * Saves the configuration properties that were entered by the user to the
     * relevant properties file
     *
     * @throws IOException
     */
    void saveConfigurationProperties() throws IOException;

    /**
     * Sets the container pane of the dialog JFrame to use a specific layout
     * manager and adds the appropriate layout constraints to each UI component
     */
    void addUIComponents();

    /**
     * Adds the relevant action listeners to this configuration dialog
     */
    void addActionListeners();
}
