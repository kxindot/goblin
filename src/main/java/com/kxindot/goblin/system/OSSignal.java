package com.kxindot.goblin.system;

import static com.kxindot.goblin.Objects.defaultIfNull;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.kxindot.goblin.EnumValue;
import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author ZhaoQingJiang
 */
public class OSSignal implements EnumValue<Integer> {
    
    /**
     * 
     */
    public static final OSSignal HUP = forName("HUP");
    
    /**
     * 
     */
    public static final OSSignal INT = forName("INT");
    
    /**
     * 
     */
    public static final OSSignal QUIT = forName("QUIT");
    
    /**
     * 
     */
    public static final OSSignal ABRT = forName("ABRT");
    
    /**
     * 
     */
    public static final OSSignal ALRM = forName("ALRM");
    
    /**
     * 
     */
    public static final OSSignal TERM = forName("TERM");
    
    /**
     * 
     */
    public static final OSSignal USR1 = forName("USR1");
    
    /**
     * 
     */
    public static final OSSignal USR2 = forName("USR2");
    
    
    /**
     * 根据信号名称获取信号对象
     * @param name 信号名称
     * @return OSSignal
     */
    public static OSSignal forName(String name) {
        requireNotBlank(name);
        if (signals == null) {
            signals = newConcurrentHashMap();
        }
        OSSignal signal = defaultIfNull(signals.get(name), new OSSignal(name));
        signals.putIfAbsent(name, signal);
        return signal;
    }
    
    /**
     * 监听指定信号
     * @param listener 回调接口
     * @param signals 待监听信号
     */
    public static void listen(OSSignalListener listener, OSSignal... signals) {
        for (OSSignal signal : signals) {
            signal.listen(listener);
        }
    }
    
    private static SignalHandlerImpl handler = new SignalHandlerImpl();
    private static Map<String, OSSignal> signals;
    private Signal signal;

    OSSignal(String name) {
        this.signal = new Signal(name);
    }
    
    /**
     * 监听本信号
     * @param listener 回调接口
     */
    public void listen(OSSignalListener listener) {
        handler.addListener(this, requireNotNull(listener));
    }

    @Override
    public Integer value() {
        return signal.getNumber();
    }

    public String name() {
        return signal.getName();
    }

    protected Signal signal() {
        return signal;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <E extends EnumValue<Integer>> Collection<E> allValues() {
        return (Collection<E>) signals.values();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && obj instanceof OSSignal) {
            OSSignal oss = OSSignal.class.cast(obj);
            return oss.signal.equals(signal);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return signal.hashCode();
    }

    @Override
    public String toString() {
        return signal.toString();
    }
    
    
    /**
     * @author ZhaoQingJiang
     */
    private static class SignalHandlerImpl implements SignalHandler {
        
        static Logger logger = LoggerFactory.getLogger(SignalHandlerImpl.class);
        Map<Signal, Set<OSSignalListener>> listeners;
        
        private SignalHandlerImpl() {
            this.listeners = newConcurrentHashMap();
        }
        
        void addListener(OSSignal signal, OSSignalListener listener) {
            Signal sig = signal.signal();
            Set<OSSignalListener> listeners = this.listeners.get(sig);
            if (listeners == null) {
                listeners = newHashSet();
                this.listeners.put(sig, listeners);
            } else if (listeners.contains(listener)) {
                return;
            }
            listeners.add(listener);
            Signal.handle(sig, this);
        }

        @Override
        public void handle(Signal sig) {
            if (!this.listeners.containsKey(sig)) return;
            OSSignal signal = OSSignal.forName(sig.getName());
            Set<OSSignalListener> listeners = this.listeners.get(sig);
            for (OSSignalListener listener : listeners) {
                try {
                    listener.notify(signal);
                } catch (Exception e) {
                    logger.error("Signal notify error!", e);
                }
            }
        }
        
    }
    
    
}
