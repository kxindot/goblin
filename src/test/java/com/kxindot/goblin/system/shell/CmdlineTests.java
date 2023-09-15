package com.kxindot.goblin.system.shell;

import java.io.ByteArrayOutputStream;

/**
 * @author ZhaoQingJiang
 */
public class CmdlineTests {

	
	public static void main(String[] args) {
		boolean result = Cmdline.create().cmd("which pwd").execSync();
		System.out.println("result: " + result);
	}
	
	
}
