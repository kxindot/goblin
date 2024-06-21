package com.kxindot.goblin;

import static com.kxindot.goblin.Resources.rename;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.test.JunitTests;

/**
 * @author ZhaoQingJiang
 */
class ResourcesTest extends JunitTests {

	@Test
	void testRenamePathString() {
		fail("Not yet implemented");
	}

	@Test
	void testRenameFileString() {
		fail("Not yet implemented");
	}

	@Test
	void testRenamePathStringBoolean() {
		Path path = Paths.get("/Users/zhaoqingjiang/Temp/test");
		path = rename(path, null, true);
		println("rename path : {}", path);
	}

	@Test
	void testRenameFileStringBoolean() {
		fail("Not yet implemented");
	}

}
