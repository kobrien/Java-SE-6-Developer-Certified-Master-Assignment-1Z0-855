package suncertify.tests;

import java.util.*;

public class TestThreads {

    public static void main(final String[] args) throws InterruptedException {

	System.out.println("Starting.");

	final List<Thread> threads = new ArrayList<Thread>();

	// create book-threads
	for (int i = 0; i < 10; i++) {
	    final int id = i;
	    final Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
		    System.out.println("START " + id);
		    try {
			Thread.currentThread().sleep(1000);
		    } catch (final InterruptedException e) {
			e.printStackTrace();
		    }
		    System.out.println("FINISH " + id);
		}
	    });
	    threads.add(t);
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

	System.out.println("Exiting.");
    }
}