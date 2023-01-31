package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.newArrayList;

import java.util.Arrays;
import java.util.List;

public class ArrayTests {

    public static void main(String[] args) {
        List<String> a = newArrayList("1", "2", "3", "4", "5");
        String[] b = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
        System.err.printf("Before copy a List : %s\n", a);
        System.err.printf("Before copy b array : %s\n", Arrays.toString(b));
        a.toArray(b);
        System.err.printf("After copy b array : %s\n", Arrays.toString(b));
        System.err.printf("a.toArray : %s\n", Arrays.toString(a.toArray(new String[0])));
    }
    
}
