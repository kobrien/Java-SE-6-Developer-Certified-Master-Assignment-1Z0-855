/*
 * @TestBusinessService.java
 * 16 Sep 2015
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
import java.util.*;

import suncertify.business.*;
import suncertify.db.*;

public class TestBusinessServiceBooking {
    private final static String DB_PATH = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";
    private static int[] recordNumbers;

    public static void main(final String[] args) throws DatabaseException {
	// Because the record numbers are not known in advance lets get them
	// upfront, note that this will be the first call to the database and
	// the cache will be populated here
	recordNumbers = getAllRecordNumbers();

	// start your RMI-server
	new TestBusinessServiceBooking().startBookerThreads();
    }

    static int[] getAllRecordNumbers() throws DatabaseException {
	final IDatabase databaseImpl = new Data(DB_PATH);

	final String[] criteria = new String[] { null, null, null, null, null,
		null, null };
	final int[] recordNumbers = databaseImpl.find(criteria);
	return recordNumbers;

    }

    static int getRandomRecordNumber() throws RecordNotFoundException {
	final Random r = new Random();
	final int Low = 1;
	final int High = 28;
	final int i = r.nextInt(High - Low);
	return recordNumbers[i];
    }

    public void startBookerThreads() {
	final List<Thread> threads = new ArrayList<Thread>();
	try {
	    // create book-threads
	    for (int i = 0; i < 100; i++) {
		threads.add(new Thread(new BookThread(i * 100), String
			.valueOf(i * 100)));
	    }
	    // random order
	    Collections.shuffle(threads);
	    // start threads
	    for (final Thread thread : threads) {
		thread.start();
	    }
	    // sleep until all threads are finished
	    for (final Thread thread : threads) {
		thread.join();
	    }
	} catch (final Exception e) {
	    System.out.println(e);
	}
	System.out.println("Done.");
    }

    private class BookThread implements Runnable {
	private final int id;
	private final String customerID;
	private boolean endRun;
	private boolean noRoom;

	public BookThread(final int id) {
	    this.id = id;
	    this.customerID = String.format("%1$08d", id);
	    this.endRun = false;
	    this.noRoom = false;
	}

	@Override
	public void run() {

	    int recNo = 0;

	    while (!endRun) {
		try {
		    recNo = getRandomRecordNumber();

		    final IDatabase databaseImpl = new Data(DB_PATH);
		    final SubcontractorService service = new DefaultSubcontractorService(
			    databaseImpl);
		    service.bookSubcontractor(recNo, customerID);
		    endRun = true;
		    service.saveData();
		} catch (final RemoteException e) {
		    System.out.println(e);
		} catch (final SubcontractorNotFoundException e) {
		    System.out.println(e);
		    noRoom = true;
		} catch (final SubcontractorAlreadyBookedException e) {
		    System.out.println(e);
		    noRoom = true;
		} catch (final DatabaseException e) {
		    System.out.println(e);
		    noRoom = true;
		} catch (final ServicesException e) {
		    e.printStackTrace();
		} finally {
		    endRun = true;
		}

	    }
	    if (noRoom) {
		System.out.println(id + " booked no room");
	    } else {
		System.out.println(id + " booked room " + recNo);
	    }
	}
    }

}