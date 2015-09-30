/*
 * @(#)DataClassTest.java    1.0 05/11/2008
 *
 * Candidate: Roberto Perillo
 * Prometric ID: Your Prometric ID here
 * Candidate ID: Your candidade ID here
 *
 * Sun Certified Developer for Java 2 Platform, Standard Edition Programming
 * Assignment (CX-310-252A)
 *
 * This class is part of the Programming Assignment of the Sun Certified
 * Developer for Java 2 Platform, Standard Edition certification program, must
 * not be used out of this context and must be used exclusively by Sun
 * Microsystems, Inc.
 */
package suncertify.tests;

import java.util.Random;

import suncertify.db.*;

public class TestDataClassThreading {
    private final static String FILE_PATH_COPY = "C:\\Users\\ekieobr\\workspace_java_masters\\JavaMasterProject\\db-2x2.db";
    private static Data data;

    /*
     * If any preparation has to be done before using the Data class, it can be
     * done in a static block; in this case, before using the Data class, the
     * loadDbRecords method has to be called prior to any other operation, so
     * the records in the physical .db file can be placed in the Map that keeps
     * them in memory; I also have a method called persistDbRecords, which
     * writes each record back to the physical .db file, but this test aims only
     * to test the functionalities without altering the database, so this method
     * is never called anywhere
     */
    static {
	try {
	    data = new Data(FILE_PATH_COPY);
	} catch (final Exception e) {
	    System.out.println(e);
	}
    }

    public static void main(final String[] args) throws DatabaseException {
	new TestDataClassThreading().startTests();
    }

    public static int getRandomRecord() throws RecordNotFoundException {
	// find everything
	final String[] criteria = new String[] { null, null, null, null, null,
		null, null };
	final int[] recordNumbers = data.find(criteria);
	final Random r = new Random();
	final int Low = 1;
	final int High = 20;
	final int R = r.nextInt(High - Low) + Low;
	return recordNumbers[R];
    }

    public static int getFirstRecord() throws RecordNotFoundException {
	final String[] criteria = new String[] { "", "", "", "", "", "" };

	final int[] recordNumbers = data.find(criteria);

	return recordNumbers[0];
    }

    public void startTests() {
	try {

	    /*
	     * Practically, it is not necessary to execute this loop more than 1
	     * time, but if you want, you can increase the controller variable,
	     * so it is executed as many times as you want
	     */
	    for (int i = 0; i < 27; i++) {
		final Thread updatingRandom = new BookRandomRecordThread();
		updatingRandom.start();
		final Thread updatingRecord1 = new BookRecord1Thread();
		updatingRecord1.start();

		// final Thread creatingRecord = new CreatingRecordThread();
		// creatingRecord.start();
		// final Thread deletingRecord = new DeletingRecord1Thread();
		// deletingRecord.start();
		final Thread findingRecords = new FindingRecordsThread();
		findingRecords.start();
	    }
	} catch (final Exception e) {
	    System.out.println(e.getMessage());
	}

    }

    private class BookRandomRecordThread extends Thread {

	@Override
	public void run() {
	    // get a record number
	    // TODO should introduce some bad record numbers here to test he bad
	    // path!
	    int recNo = 0;
	    try {
		recNo = getRandomRecord();
	    } catch (final RecordNotFoundException e1) {
		e1.printStackTrace();
	    }

	    // new SubcontractorRecord("Palace", "Smallville",
	    // "Drywall, Roofing",
	    // "2", "$150", "54120584");

	    try {
		System.out.println(Thread.currentThread().getId()
			+ " trying to lock record #" + recNo
			+ " on UpdatingRandomRecordThread");

		/*
		 * The generated record number may not exist in the database, so
		 * a RecordNotFoundException must be thrown by the lock method.
		 * Since the database records are in a cache, it is not
		 * necessary to put the unlock instruction in a finally block,
		 * because an exception can only occur when calling the lock
		 * method (not when calling the update/delete methods),
		 * therefore it is not necessary to call the unlock method in a
		 * finally block, but you can customize this code according to
		 * your reality
		 */

		data.lock(recNo);
		System.out.println(Thread.currentThread().getId()
			+ " trying to update record #" + recNo
			+ " on UpdatingRandomRecordThread");

		final String[] subcontractor_fields = data.read(recNo);

		System.out.println(Thread.currentThread().getId()
			+ " trying to update record #" + recNo
			+ " on UpdatingRandomRecordThread");

		/*
		 * An exception cannot occur here, otherwise, the unlock
		 * instruction will not be reached, and the record will be
		 * locked forever. In this case, I created a class called
		 * RoomRetriever, which transforms from Room to String array,
		 * and vice-versa, but it could also be done this way:
		 *
		 * data.update(recNo, new String[] {"Palace", "Smallville", "2",
		 * "Y", "$150.00", "2005/07/27", null});
		 */

		// final String[] new_record_fields = new String[] { "Palace",
		// "Smallville", "Drywall, Roofing", "2", "$150.00", null };
		subcontractor_fields[6] = "123456789";
		data.update(recNo, subcontractor_fields);

		System.out.println(Thread.currentThread().getId()
			+ " trying to unlock record #" + recNo
			+ " on UpdatingRandomRecordThread");

		data.unlock(recNo);
		data.saveData();
	    } catch (final Exception e) {
		System.out.println(e);
	    }
	}
    }

    private class BookRecord1Thread extends Thread {

	@Override
	public void run() {

	    try {
		final int recNo = getFirstRecord();

		System.out.println(Thread.currentThread().getId()
			+ " trying to lock record " + recNo + " on"
			+ " UpdatingRecord1Thread");
		data.lock(recNo);

		System.out.println(Thread.currentThread().getId()
			+ " trying to read record " + recNo + " on"
			+ " UpdatingRecord1Thread");
		final String[] subcontractor_fields = data.read(recNo);
		subcontractor_fields[6] = "9128512";

		System.out.println(Thread.currentThread().getId()
			+ " trying to update record " + recNo + " on"
			+ " UpdatingRecord1Thread");
		data.update(recNo, subcontractor_fields);
		System.out.println(Thread.currentThread().getId()
			+ " trying to unlock record " + recNo + " on"
			+ "UpdatingRecord1Thread");

		/*
		 * In order to see the deadlock, this instruction can be
		 * commented, and the other Threads, waiting to update/delete
		 * record #1 will wait forever and the deadlock will occur
		 */
		data.unlock(recNo);
	    } catch (final Exception e) {
		System.out.println(e);
	    }
	}
    }

    //
    // private class CreatingRecordThread extends Thread {
    //
    // @Override
    // @SuppressWarnings("deprecation")
    // public void run() {
    // final Room room = new Room();
    // room.setName("Elephant Inn");
    // room.setLocation("EmeraldCity");
    // room.setSize(6);
    // room.setSmoking(false);
    // room.setRate("$120.00");
    // room.setDate(new Date(2003, 06, 10));
    //
    // try {
    // System.out.println(Thread.currentThread().getId()
    // + " trying to create a record");
    // data.create(RoomRetriever.asArray(room));
    // } catch (final Exception e) {
    // System.out.println(e);
    // }
    // }
    // }
    //
    // private class DeletingRecord1Thread extends Thread {
    //
    // @Override
    // public void run() {
    // try {
    // System.out.println(Thread.currentThread().getId()
    // + " trying to lock record #1 on "
    // + "DeletingRecord1Thread");
    // data.lock(1);
    // System.out.println(Thread.currentThread().getId()
    // + " trying to delete record #1 on "
    // + "DeletingRecord1Thread");
    // data.delete(1);
    // System.out.println(Thread.currentThread().getId()
    // + " trying to unlock record #1 on "
    // + "DeletingRecord1Thread");
    // data.unlock(1);
    // } catch (final Exception e) {
    // System.out.println(e);
    // }
    // }
    // }
    //
    private class FindingRecordsThread extends Thread {

	@Override
	public void run() {
	    try {
		System.out.println(Thread.currentThread().getId()
			+ " trying to find records");
		final String[] criteria = { null, "Palace", "Smallville", null,
			null, null, null, };

		final int[] results = data.find(criteria);

		for (int i = 0; i < results.length; i++) {
		    System.out.println(results.length + " results found.");
		    try {
			final String message = Thread.currentThread().getId()
				+ " going to read record #" + results[i]
				+ " in FindingRecordsThread - still "
				+ ((results.length - 1) - i) + " to go.";
			System.out.println(message);

			data.lock(results[i]);

			final String[] room = data.read(results[i]);

			System.out
				.println("Contractor Name (FindingRecordsThread): "
					+ room[1]);
			System.out.println("Has next? "
				+ (i < (results.length - 1)));
		    } catch (final Exception e) {
			/*
			 * In case a record was found during the execution of
			 * the find method, but deleted before the execution of
			 * the read instruction, a RecordNotFoundException will
			 * occur, which would be normal then
			 */
			System.out.println("Exception in "
				+ "FindingRecordsThread - " + e);
		    } finally {
			data.unlock(results[i]);
		    }
		}
		System.out.println("Exiting for loop");
	    } catch (final Exception e) {
		System.out.println(e);
	    }
	}
    }
}