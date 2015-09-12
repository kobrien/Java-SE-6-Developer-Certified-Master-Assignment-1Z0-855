package suncertify.tests.db;

import java.util.logging.Logger;

import suncertify.db.Data;
import suncertify.db.DatabaseException;

public class test {
    private final static Logger LOGGER = Logger.getLogger(test.class.getName());
    private final static String FILE_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";

    public static void main(final String[] args) throws DatabaseException {

	final Data database = new Data(FILE_PATH);
	LOGGER.info("created new instance of data class");

	final String[] criteria = new String[] { "", "", "", "", "", "" };
	final int[] recordNumbers = database.find(criteria);

	for (final Integer i : recordNumbers) {
	    LOGGER.info(i.toString());
	}

	final int recNo = 127341902;
	LOGGER.info("Reading subcontractor record number " + recNo
		+ " from cache.");
	final String[] subcontractorFields = database.read(recNo);
	for (final String s : subcontractorFields) {
	    LOGGER.info(s);
	}
    }
}