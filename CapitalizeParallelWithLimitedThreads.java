import java.util.concurrent.Semaphore;

public class CapitalizeParallelWithLimitedThreads {
    static int numThreads = 3;
    static int currIndex = 0;
    static Semaphore semaphore = new Semaphore(numThreads);
    static Semaphore mutex = new Semaphore(1);
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a string as an argument.");
            return;
        }

        String input = args[0];
        char[] result = new char[input.length()];
        Thread[] threads = new Thread[numThreads];
        

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new CapitalizeTask(input, result, i));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String capitalizedString = new String(result);
        System.out.println(capitalizedString);
    }

    static class CapitalizeTask implements Runnable {
        private String input;
        private char[] result;
        private int i;

        public CapitalizeTask(String input, char[] result, int i) {
            this.input = input;
            this.result = result;
            this.i = i;
        }

        @Override
        public void run() {
            while (true) {
                try { 
                    semaphore.acquire(); 
                    mutex.acquire();
                    if (currIndex > input.length() - 1) {
                        break;
                    }
                    char currentChar = input.charAt(currIndex);
                    result[currIndex] = Character.toUpperCase(currentChar);
                    currIndex++;
                    mutex.release();   
                    System.out.println("Thread " + i + " processed letter: " + currentChar);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mutex.release();
            semaphore.release();
        }
    }
}

