package com.kxindot.goblin.limiter;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhaoQingJiang
 */
public abstract class RateLimiter {
    
    public static void main(String[] args) {
        
        System.out.println(TimeUnit.SECONDS.toMicros(-1));
        
    }
    
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    
    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, MICROSECONDS);
    }
    
    
    public boolean tryAcquire(long timeout, TimeUnit timeUnit) {
        return tryAcquire(1, 0, MICROSECONDS);
    }
    
    
    public boolean tryAcquire(int permits, long timeout, TimeUnit timeUnit) {
        
        return false;
    }
    
    
    public double acquire() {
        return acquire(1);
    }
    
    
    public double acquire(int permits) {
        
        return -1;
    }
    
    
    public double getRate() {
        
        return -1;
    }
}
