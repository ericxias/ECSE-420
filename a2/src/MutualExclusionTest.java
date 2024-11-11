package a2.src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;


public class MutualExclusionTest {
    private static final int NUM_THREADS = 5;
    private static final int NUM_ITERATIONS = 10;
    private static int counter = 0;
    private static boolean isInCriticalSection = false;

    public static void testLock(Lock lock){
        // Reset shared counter
        counter = 0; 
        isInCriticalSection = false;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        System.out.println("Testing with " + NUM_THREADS + " threads and"  + NUM_ITERATIONS + " iterations per thread");
        
        // Run threads
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(new lockTask(lock, executor));
        }

        executor.shutdown();
        while (!executor.isTerminated()){} 

        // Check if counter is equal to number of times threads should have entered critical section
        int expectedCount = NUM_THREADS * NUM_ITERATIONS;
        if (counter == expectedCount) {
            System.out.println("Test passed. " + counter + " times threads entered critical section");
        } else {
            System.out.println("Test failed. " + counter +  " times threads entered critical section,"  + expectedCount + ", expected: ");
        }
    }

    public static class lockTask implements Runnable {
        private final Lock lock;
        private final ExecutorService executor;

        public lockTask(Lock lock, ExecutorService executor) {
            this.lock = lock;
            this.executor = executor;
        }
        
        @Override
        public void run() {
            // for each thread and iterations, whenever a thread enters the critical section, it increments the shared counter
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                lock.lock();
                try {
                    // Check if multiple threads are in the critical section
                    if (isInCriticalSection) {
                        System.out.println("Multiple threads in critical section");
                        //shut down test -> test failed
                        executor.shutdownNow();
                    }
                    isInCriticalSection = true;

                    // Critical section
                    counter++;

                    // exit critical section
                    isInCriticalSection = false;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args){
        // Test Filter lock
        Filter filterLock = new Filter(NUM_THREADS);
        System.out.println("Testing Filter lock:");
        testLock(filterLock);

        // Test Bakery lock
        LamportBakeryLock bakeryLock = new LamportBakeryLock(NUM_THREADS);
        System.out.println("Testing Bakery lock:");
        testLock(bakeryLock);
    }
}