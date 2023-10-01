public class CapitalizeParallel {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a string as an argument.");
            return;
        }

        String input = args[0];
        char[] result = new char[input.length()];
        Thread[] threads = new Thread[input.length()];

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            threads[i] = new Thread(new CapitalizeTask(currentChar, i, result));
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
}

