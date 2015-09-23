/*
 * @LockManager.java
 * 25 Jul 2015
 *
 * Sun Certified Developer for the Java 2 Platform: Application Submission (Version 2.3.2)
 * 1Z0-855 - Java SE 6 Developer Certified Master Assignment
 *
 * Candidate: Kieran O'Brien
 * Oracle Testing ID: OC1256324‎
 *
 */
package suncertify.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

/**
 * The <code>LockManager</code> class is responsible for handling multiple
 * concurrent clients accessing the data cache via <code>Data</code> class.
 * Before a client thread can update a record in the data cache it must first
 * obtain a lock on that record, and release the lock after it is finished. Any
 * attempt to lock a record that is already locked should cause the client
 * thread to give up the CPU i.e. wait, consuming no CPU cycles until the
 * desired resource becomes available
 *
 * All methods in this class are called from the <code>Data</code> class before
 * it modifies records in cache, all methods here are marked as synchronized.
 *
 * All methods in this class are package private, as it should only be called
 * from <code>Data</code> class within the db package
 *
 * @author Kieran O'Brien
 *
 */
public class LockManager2 {
    private static Map<Integer, Data> lockedRecords = new HashMap<Integer, Data>();
    private static Lock lock = new ReentrantLock();
    private static Condition lockReleased = lock.newCondition();

    /**
     * Locks a record so that it can only be modified by a single client. The
     * locked record and the corresponding client who locked it is kept in the
     * lockedRecords map. If the specified record is already locked the current
     * client thread waits until it is notified by the other thread that the
     * record has been unlocked, it then acquires the lock on this record.
     *
     * @param recNo
     *            The record number to lock for this client
     * @param client
     *            The client thread that is obtaining the lock on the record
     */
    static void lockRecord(final int recNo, final Data client) {
	lock.lock();
	try {
	    while (isRecordLocked(recNo)) {

		// Wait until the record lock is released by another thread
		lockReleased.await();

	    }

	    lockedRecords.put(recNo, client);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	} finally {
	    lock.unlock();
	}
    }

    /**
     * Releases the client lock for this record and notifies all waiting threads
     * that the lock has been released.
     *
     * @param recNo
     *            The record number to unlock
     * @param client
     *            The client thread that is releasing the lock on the record
     *
     */
    static void unlockRecord(final int recNo, final Data client) {
	lock.lock();
	try {
	    // Return if the record is already unlocked
	    if (isRecordLocked(recNo)) {
		// Make sure the locked record belongs to this client thread
		// and if so unlock it and signal to other threads
		if (lockedRecords.get(recNo) == client) {
		    lockedRecords.remove(recNo);
		    lockReleased.signal();
		}
	    }
	} finally {
	    lock.unlock();
	}
    }

    /**
     * Checks if the record is currently locked. Returns true if the record
     * number is found in the locked records map
     *
     * @param recNo
     *            The record number to check
     * @return True if record is locked by another client thread otherwise False
     */
    static boolean isRecordLocked(final int recNo) {
	return lockedRecords.containsKey(recNo);
    }

}
