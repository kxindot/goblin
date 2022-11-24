package com.kxindot.goblin;

import com.kxindot.goblin.misc.Exit;
import com.kxindot.goblin.misc.OSSignal;

public class ExitTests {

    public static void main(String[] args) {
        Exit.bindSignal(OSSignal.INT, -2);
        Exit.bindSyncHook((s, a) -> System.out.printf("sync hook 1 : %s | %d\n", s, a), 
                (s, a) -> System.out.printf("sync hook 2 : %s | %d\n", s, a),
                (s, a) -> System.out.printf("sync hook 3 : %s | %d\n", s, a));
        Exit.bindAsyncHook((s, a) -> System.out.printf("async hook 1 : %s | %d\n", s, a));
        Exit.bindAsyncHook((s, a) -> System.out.printf("async hook 2 : %s | %d\n", s, a));
        Exit.bindAsyncHook((s, a) -> System.out.printf("async hook 3 : %s | %d\n", s, a));
        
        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
