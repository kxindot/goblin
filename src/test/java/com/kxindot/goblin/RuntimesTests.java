package com.kxindot.goblin;

public class RuntimesTests {

    public static void main(String[] args) {
        
        System.out.println("主线程睡眠3分钟!");
        try {
            Thread.sleep(3 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    
}
