package com.kxindot.goblin.system;

import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * @author ZhaoQingJiang
 */
public class SystemExit {
    
    private static volatile int Current_Exit_Status;
    private static volatile OSSignal Current_Exit_Signal;
    private static ExitSignalListener listener;
    private static SyncExitHookThread syncHook;
    private static Map<ExitHook, AsyncExitHookThread> asyncHooks = newConcurrentHashMap();
    
    /**
     * 退出当前JVM进程.此方法是对{@link Runtime#exit(int)}的包装.
     * @see Runtime#exit(int)
     * @param status 进程退出状态码.0代表正常退出,否则视为非正常退出.
     */
    public static void exit(int status) {
        Runtime.getRuntime().exit(status);
    }
    
    /**
     * 绑定并监听指定名称的进程退出信号.
     * 当JVM进程监听到指定名称的信号时,JVM进程会调用{@link Runtime#exit(int)}退出,进程退出状态码默认为0.
     * @see #bindSignal(String, int)
     * @see #bindSignal(OSSignal)
     * @see #bindSignal(OSSignal, int)
     * @param name 信号名称
     * @throws IllegalArgumentException 当信号不存在,或信号已被OS或JVM占用时,会抛出该异常
     */
    public static void bindSignal(String name) {
        bindSignal(name, 0);
    }
    
    /**
     * 绑定并监听指定名称的进程退出信号.
     * 当JVM进程监听到指定名称的信号时,JVM进程会调用{@link Runtime#exit(int)}退出,进程退出状态码为{@code exitStat}.
     * @see #bindSignal(String, int)
     * @see #bindSignal(OSSignal)
     * @see #bindSignal(OSSignal, int)
     * @param name 信号名称
     * @param exitStat 进程退出状态码.0代表正常退出,否则视为非正常退出.
     * @throws IllegalArgumentException 当信号不存在,或信号已被OS或JVM占用时,会抛出该异常
     */
    public static void bindSignal(String name, int exitStat) {
        bindSignal(OSSignal.forName(name), exitStat);
    }
    
    /**
     * 绑定并监听指定的进程退出信号.
     * 当JVM进程监听到指定的信号时,JVM进程会调用{@link Runtime#exit(int)}退出,进程退出状态码默认为0.
     * @see OSSignal
     * @see #bindSignal(OSSignal, int)
     * @param signal 信号
     * @throws IllegalArgumentException 当信号已被OS或JVM占用时,会抛出该异常
     */
    public static void bindSignal(OSSignal signal) {
        bindSignal(signal, 0);
    }
    
    /**
     * 绑定并监听指定的进程退出信号.
     * 当JVM进程监听到指定的信号时,JVM进程会调用{@link Runtime#exit(int)}退出,进程退出状态码为{@code exitStat}.
     * @see OSSignal
     * @param signal 信号
     * @param exitStat 进程退出状态码.0代表正常退出,否则视为非正常退出.
     * @throws IllegalArgumentException 当信号已被OS或JVM占用时,会抛出该异常
     */
    public static void bindSignal(OSSignal signal, int exitStat) {
        requireNotNull(signal);
        if (listener == null) {
            listener = new ExitSignalListener();
        }
        boolean success = listener.addSignal(signal, exitStat);
        if (success) {
            signal.listen(listener);
        }
    }
    
    /**
     * 
     * @param hooks
     */
    public static synchronized void bindSyncHook(ExitHook... hooks) {
        requireNotNull(hooks);
        if (isEmpty(hooks)) return;
        if (syncHook == null) {
            syncHook = new SyncExitHookThread();
        }
        syncHook.addAll(newHashSet(hooks));
    }
    
    /**
     * 
     * @param hooks
     */
    public static void bindAsyncHook(ExitHook... hooks) {
        requireNotNull(hooks);
        if (isEmpty(hooks)) return;
        Arrays.stream(hooks).forEach(h -> asyncHooks.putIfAbsent(h, new AsyncExitHookThread(h)));
    }
    
    /**
     * 
     */
    protected static void bindHookThread(Thread thread) {
        Runtime.getRuntime().addShutdownHook(thread);
    }
    
    /**
     * 进程退出回调接口.
     * @author ZhaoQingJiang
     */
    @FunctionalInterface
    public interface ExitHook {
        
        /**
         * 回调优先级,数字越小优先级越高.
         * 只存在于绑定了多个同步回调时生效.
         * @see SystemExit#bindSyncHook(ExitHook...)
         * @return int 数字越小优先级越高
         */
        default int order() {
            return 0;
        }
        
        /**
         * 
         * @param signal 进程退出信号
         * @param status 进程退出状态码
         */
        void onExit(OSSignal signal, int status);
    }
    
    /**
     * @author ZhaoQingJiang
     */
    static class ExitSignalListener implements OSSignalListener {
        
        static Logger logger = LoggerFactory.getLogger(ExitSignalListener.class);
        Map<OSSignal, Integer> signals;
        
        ExitSignalListener() {
            this.signals = newConcurrentHashMap();
        }
        
        boolean addSignal(OSSignal signal, int status) {
            boolean exit = signals.containsKey(signal);
            signals.put(signal, status);
            return !exit;
        }

        @Override
        public void notify(OSSignal signal) {
            logger.info("监听到进程退出信号({}),JVM进程准备退出", signal.name());
            int status = signals.get(signal);
            Current_Exit_Signal = signal;
            Current_Exit_Status = status;
            if (isNotNull(syncHook)) {
                bindHookThread(syncHook);
            }
            if (isNotEmpty(asyncHooks)) {
                asyncHooks.values().forEach(t -> bindHookThread(t));
            }
            exit(status);
            logger.info("JVM进程正在退出...");
        }
        
    }
    
    /**
     * @author ZhaoQingJiang
     */
    static class AsyncExitHookThread extends Thread{

        static Logger log = LoggerFactory.getLogger(AsyncExitHookThread.class);
        static AtomicInteger index = new AtomicInteger(0);
        ExitHook hook;
        
        AsyncExitHookThread(ExitHook hook) {
            super("Async-Shutdown-Hook-Thread-" + index.incrementAndGet());
            this.hook = hook;
        }

        @Override
        public void run() {
            try {
                hook.onExit(Current_Exit_Signal, Current_Exit_Status);
            } catch (Throwable e) {
                log.error("JVM进程退出回调方法异常: {}"
                        , hook.getClass().getSimpleName(), e);
            }
        }
    }
    
    /**
     * @author ZhaoQingJiang
     */
    static class SyncExitHookThread extends Thread {
        
        static Logger log = LoggerFactory.getLogger(SyncExitHookThread.class);
        List<ExitHook> hooks;
        
        SyncExitHookThread() {
            super("Sync-Shutdown-Hook-Thread");
            this.hooks = newArrayList();
        }
        
        void addAll(Collection<ExitHook> hooks) {
            this.hooks.addAll(hooks);
            Collections.sort(this.hooks, (a, b) -> a.order() - b.order());
        }
        
        @Override
        public void run() {
            for (ExitHook hook : hooks) {
                try {
                    hook.onExit(Current_Exit_Signal, Current_Exit_Status);
                } catch (Throwable e) {
                    log.error("JVM进程退出回调方法异常: {}"
                            , hook.getClass().getSimpleName(), e);
                }
            }
        }
    }
    
}
