package com.kxindot.goblin.system.shell;

import java.util.Map;

/**
 * 命令行异步运行回调接口
 * @author ZhaoQingJiang
 */
@FunctionalInterface
public interface CmdlineCallback {
	
	/**
	 * 命令运行前回调
	 * @param cmd 命令(包含参数)
	 * @param envs 环境参数
	 */
	default void beforeProcess(String cmd, Map<String, String> envs) {}
	
	/**
	 * 命令运行后回调
	 * @param exit 命令退出码
	 * @param output 命令输出
	 */
	void afterProcess(int exit, String output);
	
}