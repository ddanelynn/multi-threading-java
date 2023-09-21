# multi-threading-java
A Java program that takes in a string input (command line) and capitalizes it: "hello" -> "HELLO". But it has to be done in parallel (each character processing is to be done by a thread), then compiled back into the final output and returned on the console.

Must use Java Threads & Mutex. Not allowed to use Executors.

Stretch goal #1: limit the number of threads to N (N=3 for example, N < string length). The threads should be recycled.

Stretch goal #2: make the program reusable: can keep feeding it inputs even while the current input is processing. only die after the user requests the program to quit.

Stretch goal #3: use Java Process instead of Thread (can rely on process input/output streams for communication).
