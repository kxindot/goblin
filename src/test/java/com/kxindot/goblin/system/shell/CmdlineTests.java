package com.kxindot.goblin.system.shell;

/**
 * @author ZhaoQingJiang
 */
public class CmdlineTests {

	
	public static void main(String[] args) {
		System.out.printf("which node: %s\n", Cmdline.which("node"));
		System.out.printf("which java: %s\n", Cmdline.which("java"));
		System.out.printf("which mvn: %s\n", Cmdline.which("mvn"));
		System.out.printf("which pwd: %s\n", Cmdline.which("pwd"));
		System.out.printf("pwd: %s\n", Cmdline.pwd());
	}
	
	
}
