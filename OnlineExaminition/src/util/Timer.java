package util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Timer extends Thread {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m"; 
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m"; 
    
    private int timeLimit;  
    private final AtomicBoolean isRunning;
    private final int initialTime;
    private final Runnable timeUpCallback;
    private volatile boolean running = false;
    private final Object lock = new Object();
    private boolean canPrintTime = true;
    
    public Timer(int minutes, Runnable timeUpCallback) {
        this.timeLimit = minutes * 60;
        this.initialTime = timeLimit;
        this.isRunning = new AtomicBoolean(true);
        this.timeUpCallback = timeUpCallback;
    }
    
    @Override
    public void start() {
        running = true;
        Thread timerThread = new Thread(() -> {
            System.out.println();
            while (running && timeLimit > 0) {
                synchronized(lock) {
                    while (!canPrintTime && running) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
                
                if (!running) break;
                
                String color = GREEN;
                if (timeLimit <= initialTime * 0.1) {
                    color = RED;
                } else if (timeLimit <= initialTime * 0.5) {
                    color = YELLOW;
                }
                
                System.out.print("\033[1A\033[2K");
                System.out.printf("\r%sTime remaining: %02d:%02d%s\n", 
                    color, timeLimit / 60, timeLimit % 60, RESET);
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                timeLimit--;
            }
            if (running && timeLimit == 0) {
                System.out.println("\n" + RED + "TIME'S UP!" + RESET);
                if (timeUpCallback != null) {
                    timeUpCallback.run();
                }
            }
        });
        timerThread.start();
    }
    
    public void stopTimer() {
        isRunning.set(false);
        running = false;
        synchronized(lock) {
            lock.notify();
        }
    }
    
    public boolean isTimeRemaining() {
        return timeLimit > 0;
    }

    public void waitForQuestion() {
        synchronized(lock) {
            canPrintTime = false;
        }
    }

    public void resumeTimer() {
        synchronized(lock) {
            canPrintTime = true;
            lock.notify();
        }
    }
}