import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CapitalizeParallelWithThreadPool {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a string as an argument.");
            return;
        }

        String input = args[0];
        char[] result = new char[input.length()];
        int numThreads = 3; // Change this to the desired number of threads

        CustomThreadPool threadPool = new CustomThreadPool(numThreads);

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            threadPool.execute(new CapitalizeTask(currentChar, i, result));
        }

        // Wait for all tasks to complete
        threadPool.waitForCompletion();

        String capitalizedString = new String(result);
        System.out.println(capitalizedString);
    }

    static class CapitalizeTask implements Runnable {
        private char character;
        private int index;
        private char[] result;

        public CapitalizeTask(char character, int index, char[] result) {
            this.character = character;
            this.index = index;
            this.result = result;
        }

        @Override
        public void run() {
            char capitalizedChar = Character.toUpperCase(character);
            result[index] = capitalizedChar;
        }
    }

    static class CustomThreadPool {
        private Thread[] threads;
        private BlockingQueue<Runnable> taskQueue;

        public CustomThreadPool(int numThreads) {
            threads = new Thread[numThreads];
            taskQueue = new LinkedBlockingQueue<>();

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(new Worker());
                threads[i].start();
            }
        }

        public void execute(Runnable task) {
            try {
                taskQueue.put(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void waitForCompletion() {
            for (Thread thread : threads) {
                try {
                    taskQueue.put(new NullTask()); // Add a special task to signal thread termination
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private class Worker implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        Runnable task = taskQueue.take();
                        if (task instanceof NullTask) {
                            return; // Exit the worker thread
                        }
                        task.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    static class NullTask implements Runnable {
        @Override
        public void run() {
            // This task is used to signal thread termination
        }
    }
}
