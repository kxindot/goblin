package com.kxindot.goblin.misc;

/**
 * @author ZhaoQingJiang
 */
@FunctionalInterface
public interface OSSignalListener {
    
    void notify(OSSignal signal);
    
}
