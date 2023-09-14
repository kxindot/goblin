package com.kxindot.goblin.system.shell;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

/**
 * 命令行
 * 
 * @author ZhaoQingJiang
 */
public interface Cmdline {
	
	/**
	 * 设置待运行命令
	 * @param cmd 命令
	 * @return Cmdline
	 */
	Cmdline cmd(String cmd);
	
	/**
	 * 设置待运行命令参数
	 * @param arg 命令参数
	 * @return Cmdline
	 */
	Cmdline arg(String arg);
	
	/**
	 * 设置待运行命令参数(取文件路径)
	 * @param file 命令参数,取文件路径
	 * @return Cmdline
	 */
	Cmdline arg(File file);
	
	/**
	 * 批量设置待运行命令参数
	 * @param args 命令参数
	 * @return Cmdline
	 */
	Cmdline args(String... args);
	
	Cmdline workingDirectory(Path directory);
	
	Cmdline workingDirectory(File directory);
	
	/**
	 * 同步运行命令,会阻塞当前线程直到命令线程运行完成,返回命令是否运行成功.
	 * @return 命令是否运行成功
	 */
	boolean execSync();
	
	/**
	 * 同步运行命令,将命令输出写入writer,会阻塞当前线程直到命令线程运行完成,返回命令是否运行成功.
	 * @param writer Writer
	 * @return 命令是否运行成功
	 */
	boolean execSync(Writer writer);
	
	/**
	 * 同步运行命令,将命令输出写入输出流,会阻塞当前线程直到命令线程运行完成,返回命令是否运行成功.
	 * @param out OutputStream
	 * @return 命令是否运行成功
	 */
	boolean execSync(OutputStream out);
	
	/**
	 * 异步运行命令,callback在命令运行前后会调用特定方法.
	 * @param callback
	 */
	void execAsync(CmdlineCallback callback);
	
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
}
