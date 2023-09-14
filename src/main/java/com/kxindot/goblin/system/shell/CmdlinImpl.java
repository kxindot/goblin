package com.kxindot.goblin.system.shell;

import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.system.OS.OSFamily.WINDOWS;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execSync(Writer writer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execSync(OutputStream out) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execAsync(CmdlineCallback callback) {
		// TODO Auto-generated method stub

	}

	private Process exec() {

		
		return null;
	}

}
