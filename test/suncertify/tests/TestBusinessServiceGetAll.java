/*
 * @TestBusinessServiceGetAll.java
 * 17 Sep 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.tests;

import java.rmi.RemoteException;
import java.util.List;

import suncertify.business.DefaultSubcontractorService;
import suncertify.business.SubcontractorService;
import suncertify.db.*;

public class TestBusinessServiceGetAll {
    private final static String DB_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2-copy.db";

    public static void main(final String[] args) {

	try {
	    final IDatabase databaseImpl = new Data(DB_PATH);
	    final SubcontractorService service = new DefaultSubcontractorService(
		    databaseImpl);
	    final List<SubcontractorRecord> subcontractors = service
		    .getAllSubcontractors();

	    printSubcontractorsList(subcontractors);
	} catch (final DatabaseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * @param subcontractors
     */
    private static void printSubcontractorsList(
	    final List<SubcontractorRecord> subcontractors) {
	for (final SubcontractorRecord sub : subcontractors) {
	    System.out.println("#####################");
	    // System.out.println(sub.getRecordNumber());
	    System.out.println(sub.toStringBuilder());
	    System.out.println("#####################");

	}
    }
}
