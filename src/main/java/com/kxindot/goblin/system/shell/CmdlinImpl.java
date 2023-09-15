package com.kxindot.goblin.system.shell;

import static com.kxindot.goblin.Objects.Comma;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.stringJoinWith;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.load;
import static com.kxindot.goblin.Resources.readString;
import static com.kxindot.goblin.Throws.isWrapperException;
import static com.kxindot.goblin.Throws.threx;
import static com.kxindot.goblin.Throws.unwrapperException;
import static com.kxindot.goblin.system.OS.OSFamily.WINDOWS;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;
import com.kxindot.goblin.system.OS;

/**
 * @author ZhaoQingJiang
 */
public class CmdlinImpl implements Cmdline {
    
	private final List<CmdArg> args;
	private final Map<String, String> envs;
	private final Shell shell;

	CmdlinImpl(Shell shell) {
		this.args = synchronizedList(newArrayList());
		this.envs = synchronizedMap(newLinkedHashMap());
		this.shell = isNull(shell) ? getDefaultShell() : shell;
		this.envs.putAll(System.getenv());
	}

	private Shell getDefaultShell() {
		return OS.isFamily(WINDOWS.value()) ? new CmdShell() : new BourneShell();
	}

	@Override
	public Cmdline cmd(String cmd) {
		requireNotBlank(cmd);
		String[] arr = CmdUtil.translate(cmd);
		if (isNotEmpty(arr)) {
			args.clear();
			shell.setExecutable(arr[0]);
			for (int i = 1; i < arr.length; i++) {
				arg(arr[i]);
			}
		}
		return this;
	}

	@Override
	public Cmdline arg(File file) {
		CmdArg arg = new CmdArg();
		arg.setFile(file);
		args.add(arg);
		return this;
	}

	@Override
	public Cmdline arg(String arg) {
		CmdArg cmdArg = new CmdArg();
		cmdArg.setValue(arg);
		args.add(cmdArg);
		return this;
	}

	@Override
	public Cmdline args(String... args) {
		requireNotNull(args);
		if (isNotEmpty(args)) {
			for (int i = 0; i < args.length; i++) {
				CmdArg arg = new CmdArg();
				arg.setValue(args[i]);
				this.args.add(arg);
			}
		}
		return this;
	}

	@Override
	public Cmdline workingDirectory(Path directory) {
		shell.setWorkingDirectory(directory.toFile());
		return this;
	}
	
	@Override
	public Cmdline workingDirectory(File directory) {
		shell.setWorkingDirectory(directory);
		return this;
	}

	@Override
	public boolean execSync() {
		return execSync(System.out);
	}

	@Override
	public boolean execSync(Writer writer) {
		requireNotNull(writer);
		Process process = exec();
		load(writer).read(new InputStreamReader(process.getInputStream())).close();
		return isSuccess(process);
	}

	@Override
	public boolean execSync(OutputStream out) {
		requireNotNull(out);
		Process process = exec();
		load(out).read(process.getInputStream()).close();
		return isSuccess(process);
	}
	
	private boolean isSuccess(Process process) {
	    int exit = 0;
        try {
            exit = process.exitValue();
        } catch (IllegalThreadStateException e) {
            try {
                process.waitFor();
            } catch (InterruptedException e1) {
                //ignore
            }
            exit = process.exitValue();
        }
        if (exit == 127) {
            threx(CmdlineException::new, "命令不存在: %s", shell.getExecutable());
        }
        return exit == 0;
	}
	
	@Override
	public void execAsync() {
	    execAsync(callback);
	}

	@Override
	public void execAsync(CmdlineCallback callback) {
        callback.beforeProcess(getCmdline(), envs);
        Throwable ex = null;
        String output = null;
	    try {
	        Process process = exec();
            output = readString(process.getInputStream());
        } catch (Throwable e) {
            if (isWrapperException(e)) {
                e = unwrapperException(e);
            }
            ex = e;
        }
	    callback.afterProcess(output, ex);
	}

	private Process exec() {
		Process process = null;
		String[] envs = getEnvs();
		File directory = shell.getWorkingDirectory();
		String[] cmdArray = getCmdArray();
		try {
			if (isNull(directory)) {
				process = Runtime.getRuntime().exec(cmdArray, envs);
			} else if (!exists(directory)) {
				threx(CmdlineException::new, "%s不存在!", directory.getPath());
			} else if (!isDirectory(directory)) {
				threx(CmdlineException::new, "%s不是一个文件夹!", directory.getPath());
			} else {
				process = Runtime.getRuntime().exec(cmdArray, envs, directory);
			}
		} catch (IOException e) {
			threx(CmdlineException::new, e, "运行命令异常!");
		}
		return process;
	}
	
	private String[] getEnvs() {
		List<String> list = newArrayList();
		this.envs.forEach((k, v) -> {
			if (v != null) {
				list.add(k + "=" + v);
			}
		});
		return list.toArray(new String[0]);
	}
	
	private String getCmdline() {
	    return stringJoinWith(" ", getCmdArray());
	}
	
	private String[] getCmdArray() {
		List<String> cmdline = shell.getShellCommandLine(getArgs());
		return cmdline.toArray(new String[0]);
	}
	
	public String[] getArgs() {
		return getArgs(false);
	}
	
	public String[] getArgs(boolean mask) {
		List<String> list = newArrayList();
		for (CmdArg arg : args) {
			String[] parts = arg.getParts();
			if (isNull(parts)) {
				continue;
			} else if (mask && arg.isMask() && parts.length > 0) {
				String[] copy = new String[parts.length];
				Arrays.fill(copy, "*****");
				parts = copy;
			}
			Collections.addAll(list, parts);
		}
		return list.toArray(new String[0]);
	}
	
    private static Logger logger = LoggerFactory.getLogger(CmdlinImpl.class);

    private static CmdlineCallback callback = new CmdlineCallback() {
        
        public void beforeProcess(String cmd, Map<String,String> envs) {
            if (logger.isDebugEnabled()) {
                logger.debug("运行命令: {}", cmd);
                List<String> list = newArrayList();
                envs.forEach((k, v) -> list.add(k + "=" + v));
                logger.debug("运行环境变量: {}", stringJoinWith(Comma, list));
            }
        };
        
        @Override
        public void afterProcess(String output, Throwable ex) {
            if (ex != null) {
                logger.info("命令运行异常!", ex);
            } else {
                logger.info("命令运行结果: {}", output);
            }
        }
    };

}
