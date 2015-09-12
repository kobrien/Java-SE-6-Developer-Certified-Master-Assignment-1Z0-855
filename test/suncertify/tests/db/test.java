package suncertify.tests.db;

import java.util.Arrays;
import java.util.logging.Logger;

import suncertify.db.*;

public class test {
    private final static Logger LOGGER = Logger.getLogger(test.class.getName());
    private final static String FILE_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";
    private final static String FILE_PATH_COPY = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2-new.db";
    private static Data database;

    public static void main(final String[] args) throws DatabaseException {
	database = new Data(FILE_PATH_COPY);

	// printAllRecords()

	// Test lock, read, unlock a record
	read();

	updateRecord();

	read();
    }

    static void printAllRecords() throws RecordNotFoundException {
	final String[] criteria = new String[] { "", "", "", "", "", "" };
	final int[] recordNumbers = database.find(criteria);

	for (final Integer i : recordNumbers) {
	    LOGGER.info(i.toString());
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
	final String[] newFields = new String[] { "Hamner & Tong",
		"Smallville", "Drywall, Roofing", "10", "$86", "99999" };
	database.lock(recNo);
	database.update(recNo, newFields);
	database.unlock(recNo);

	// write cache back to disk
	database.saveData();
    }
}