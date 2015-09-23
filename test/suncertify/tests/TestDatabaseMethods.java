package suncertify.tests;

import java.util.Arrays;
import java.util.logging.Logger;

import suncertify.db.*;

public class TestDatabaseMethods {
    private final static Logger LOGGER = Logger.getLogger(TestDatabaseMethods.class.getName());
    private final static String FILE_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";
    private final static String FILE_PATH_COPY = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2-copy.db";
    private static Data database;

    public static void main(final String[] args) throws DatabaseException {
	database = new Data(FILE_PATH_COPY);

	// printAllRecords();

	// Test lock, read, unlock a record
	printAllNames();
	// updateRecord();
	// read();
    }

    static void printAllRecords() throws RecordNotFoundException {
	final String[] criteria = new String[] { "", "", "", "", "", "" };
	final int[] recordNumbers = database.find(criteria);

	for (final Integer i : recordNumbers) {
	    LOGGER.info(i.toString());
	}
    }

    static void printAllNames() throws RecordNotFoundException {
	final String[] criteria = new String[] { "", "", "", "", "", "" };
	final int[] recordNumbers = database.find(criteria);

	for (final Integer i : recordNumbers) {
	    final String[] fields = database.read(i);
	    LOGGER.info("Name : " + fields[0]);
	    LOGGER.info("Owner : " + fields[5]);

	}
    }

    static void read() throws RecordNotFoundException {
	final int recNo = 1273419022;
	LOGGER.info("Reading subcontractor record number " + recNo
		+ " from cache.");
	database.lock(recNo);
	final String[] subcontractorFields = database.read(recNo);
	database.unlock(recNo);

	LOGGER.info(Arrays.toString(subcontractorFields));

    }

    static void updateRecord() throws DatabaseException {
	final int recNo = 1273419022;

	// Update a record - setting a customer ID
	final String[] newFields = new String[] { "Hamaaaaaner & Tong",
		"Smallville", "Drywall, Roofing", "10", "$86", "8888" };

	database.lock(recNo);
	database.update(recNo, newFields);
	database.unlock(recNo);

	// write cache back to disk
	DBFileAccess.writeAllSubcontractorsToFile();
    }
}