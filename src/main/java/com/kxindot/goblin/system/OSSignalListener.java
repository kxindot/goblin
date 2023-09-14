package com.kxindot.goblin.system;

/**
 * @author ZhaoQingJiang
 */
@FunctionalInterface
public interface OSSignalListener {
    
    void notify(OSSignal signal);
    
}
