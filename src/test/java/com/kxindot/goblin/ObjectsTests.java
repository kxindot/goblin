package com.kxindot.goblin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoQingJiang
 */
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class ObjectsTests {
    
    @BeforeAll
    void beforeTest() {
        log.info("---------------------------------Test Start---------------------------------");
    }
    
    @AfterAll
    void afterTest() {
        log.info("---------------------------------Test End---------------------------------");
    }

    @Test
    void arrayTest() {
        Object[] objs = new Object[] {"1", "123", null, 123};
        log.info("Object Array = {}, contains null : {}", objs, Objects.contains(objs, null));
        log.info("isInstance : {}", Comparable.class.isInstance(null));
    }
    
}
