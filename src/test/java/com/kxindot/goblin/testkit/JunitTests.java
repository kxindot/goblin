package com.kxindot.goblin.testkit;

import static com.kxindot.goblin.Objects.stringRepeat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * @author ZhaoQingJiang
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class JunitTests extends LogTests {
    
    private String ba = stringRepeat(">", 30);
    private String aa = stringRepeat("<", 30);
//    private String abe = stringRepeat("-", 30);

    @BeforeAll
    public void beforeAll() {
        logger.info(ba + getClass().getSimpleName() + ba);
    }
    
    @AfterAll
    public void afterAll() {
        logger.info(aa + getClass().getSimpleName() + aa);
    }
    
    @BeforeEach
    public void beforeEach() {
        
    }
    
    @AfterEach
    public void afterEach() {
        
    }
    
}
