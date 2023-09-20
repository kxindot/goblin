package com.kxindot.goblin.system.shell;

import static com.kxindot.goblin.Objects.LF;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.system.OS.isWindows;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;

/**
 * 命令行工具
 * 
 * @author ZhaoQingJiang
 */
public interface Cmdline {
	
	/**
	 * 创建一个命令行实例.
	 * @return Cmdline
	 */
	public static Cmdline create() {
		return new CmdlinImpl(null);
	}
	
	/**
	 * 查询命令是否存在.
	 * @param cmd 命令
	 * @return boolean
	 */
	public static boolean exists(String cmd) {
		String arg = cmd;
		cmd = isWindows() ? "where" : "which";
		return Cmdline.create().cmd(cmd).arg(arg).execSync();
	}
	
	/**
	 * 查询指定命令{@code cmd}的绝对路径.
	 * <b>若命令不存在,返回null.</b>
	 * <pre>
	 * Unix		:	which cmd
	 * Windows	:	where cmd
	 * </pre>
	 * @param cmd 待查询命令
	 * @return String 命令绝对路径 - 命令不存在则返回null
	 */
	public static String which(String cmd) {
		String arg = cmd;
		cmd = isWindows() ? "where" : "which";
		return Cmdline.create().cmd(cmd).arg(arg).execOut();
	}
	
	/**
	 * 返回当前工作路径.
	 * <pre>
	 * Unix		:	pwd
	 * Windows	:	cd
	 * </pre>
	 * @return 当前工作路径
	 */
	public static String pwd() {
		String cmd = isWindows() ? "cd" : "pwd";
		String out = Cmdline.create().cmd(cmd).execOut();
		return isBlank(out) ? out : out.endsWith(LF) 
				? out.substring(0, out.length() - 1) : out;
	}
	
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
	
	/**
	 * 设置命令工作目录
	 * @param directory 目录
	 * @return Cmdline
	 */
	Cmdline workingDirectory(Path directory);
	
	/**
	 * 设置命令工作目录
	 * @param directory 目录
	 * @return Cmdline
	 */
	Cmdline workingDirectory(File directory);
	
	/**
	 * 运行命令并返回输出.
	 * @return 命令输出
	 */
	String execOut();
	
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
     * 异步运行命令
     */
    void execAsync();
	
	/**
	 * 异步运行命令,callback在命令运行前后会调用特定方法.
	 * @param callback
	 */
	void execAsync(CmdlineCallback callback);
}
