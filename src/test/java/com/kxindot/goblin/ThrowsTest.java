package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Throws.silentThrex;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThrowsTest {

	private static Logger logger = LoggerFactory.getLogger(ThrowsTest.class);
	
	public static void main(String[] args) {
		try {
			test1();
		} catch (Exception e) {
			logger.error("测试", e);
		}
		silentThrex(IOException::new);
	}
	
	private static void test1() {
		test3(null);
		try {
			test2();
		} catch (IOException e) {
			logger.error("测试", e);
			silentThrex(e, "异常测试");
		}
	}
    private static void test2() throws IOException {
    	throw new IOException();
    }
    
    
    private static void test3(String p1) {
    	requireNotBlank(p1, "参数不能为空");
    }
    
}
