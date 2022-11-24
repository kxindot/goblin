package com.kxindot.goblin;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class KillSignalTests implements SignalHandler {

    @Override
    public void handle(Signal sig) {
        System.out.printf("Kill Signal : %s = %s\n", sig.getName(), sig.getNumber());
    }
    
    public static void main(String[] args) {
        KillSignalTests handler = new KillSignalTests();
        Signal s1 = new Signal("USR1");
        Signal s2 = new Signal("INT");
        Signal s3 = new Signal("USR2");
        Signal s4 = new Signal("ABRT");
        Signal s5 = new Signal("TERM");
        Signal.handle(s1, handler);
        Signal.handle(s2, handler);
        Signal.handle(s3, handler);
        Signal.handle(s4, handler);
        Signal.handle(s5, handler);
        
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
