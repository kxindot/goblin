package com.kxindot.goblin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class RegexTests {

    @Test
    public void test0() {
        String str = "a=123_a=567_a=cvb";
        String reg = "a=\\d+";
        Matcher matcher = Pattern.compile(reg).matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
    
    @Test
    public void test1() {
        String str = "2022-08-11 12:23:45";
        String reg = "\\d{4}";
        Matcher matcher = Pattern.compile(reg).matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
    
    
    
    
    
//    public static class ShutdownHandler
}
