package com.vashchenko.task.lockers;


import org.springframework.stereotype.Component;

@Component
public class MoneyOperationLocker {
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notifyAll();
    }
}
