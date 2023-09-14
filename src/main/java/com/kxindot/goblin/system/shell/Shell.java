package com.kxindot.goblin.system.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shell {

	private static final char[] DEFAULT_QUOTING_TRIGGER_CHARS = { ' ' };

	private String shellCommand;
	private final List<String> shellArgs = new ArrayList<String>();
	private boolean quotedArgumentsEnabled = true;
	private boolean unconditionalQuoting = false;
	private String executable;
	private String workingDir;
	private boolean quotedExecutableEnabled = true;
	private boolean singleQuotedArgumentEscaped = false;
	private boolean singleQuotedExecutableEscaped = false;
	private char argQuoteDelimiter = '\"';
	private char exeQuoteDelimiter = '\"';

	/**
	 * Set the command to execute the shell (e.g. COMMAND.COM, /bin/bash,...).
	 *
	 * @param shellCommand the command
	 */
	void setShellCommand(String shellCommand) {
		this.shellCommand = shellCommand;
	}

	/**
	 * Get the command to execute the shell.
	 *
	 * @return the command
	 */
	String getShellCommand() {
		return shellCommand;
	}

	/**
	 * Set the shell arguments when calling a command line (not the executable
	 * arguments) (e.g. /X /C for CMD.EXE).
	 *
	 * @param shellArgs the arguments to the shell
	 */
	void setShellArgs(String[] shellArgs) {
		this.shellArgs.clear();
		this.shellArgs.addAll(Arrays.asList(shellArgs));
	}

	/**
	 * Get the shell arguments
	 *
	 * @return the arguments
	 */
	String[] getShellArgs() {
		if (shellArgs.isEmpty()) {
			return null;
		} else {
			return shellArgs.toArray(new String[0]);
		}
	}

	protected String quoteOneItem(String inputString, boolean isExecutable) {
		char[] escapeChars = getEscapeChars(isSingleQuotedExecutableEscaped(), isDoubleQuotedExecutableEscaped());
		return quoteAndEscape(inputString,
		        isExecutable ? getExecutableQuoteDelimiter() : getArgumentQuoteDelimiter(), escapeChars,
		        getQuotingTriggerChars(), '\\', unconditionalQuoting);
	}

	/**
	 * Get the command line for the provided executable and arguments in this shell
	 *
	 * @param executableParameter executable that the shell has to call
	 * @param argumentsParameter  arguments for the executable, not the shell
	 * @return list with one String object with executable and arguments quoted as
	 *         needed
	 */
	List<String> getCommandLine(String executableParameter, String... argumentsParameter) {
		return getRawCommandLine(executableParameter, argumentsParameter);
	}

	/**
	 * @param executableParameter Executable
	 * @param argumentsParameter  the arguments for the executable
	 * @return the list on command line
	 */
	List<String> getRawCommandLine(String executableParameter, String... argumentsParameter) {
		List<String> commandLine = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		if (executableParameter != null) {
			String preamble = getExecutionPreamble();
			if (preamble != null) {
				sb.append(preamble);
			}

			if (isQuotedExecutableEnabled()) {
				sb.append(quoteOneItem(executableParameter, true));
			} else {
				sb.append(executableParameter);
			}
		}
		for (String argument : argumentsParameter) {
			if (sb.length() > 0) {
				sb.append(' ');
			}

			if (isQuotedArgumentsEnabled()) {
				sb.append(quoteOneItem(argument, false));
			} else {
				sb.append(argument);
			}
		}

		commandLine.add(sb.toString());

		return commandLine;
	}

	char[] getQuotingTriggerChars() {
		return DEFAULT_QUOTING_TRIGGER_CHARS;
	}

	String getExecutionPreamble() {
		return null;
	}

	char[] getEscapeChars(boolean includeSingleQuote, boolean includeDoubleQuote) {
		StringBuilder buf = new StringBuilder(2);
		if (includeSingleQuote) {
			buf.append('\'');
		}

		if (includeDoubleQuote) {
			buf.append('\"');
		}

		char[] result = new char[buf.length()];
		buf.getChars(0, buf.length(), result, 0);

		return result;
	}

	/**
	 * @return false in all cases
	 */
	protected boolean isDoubleQuotedArgumentEscaped() {
		return false;
	}

	/**
	 * @return {@link #singleQuotedArgumentEscaped}
	 */
	protected boolean isSingleQuotedArgumentEscaped() {
		return singleQuotedArgumentEscaped;
	}

	boolean isDoubleQuotedExecutableEscaped() {
		return false;
	}

	boolean isSingleQuotedExecutableEscaped() {
		return singleQuotedExecutableEscaped;
	}

	/**
	 * @param argQuoteDelimiterParameter {@link #argQuoteDelimiter}
	 */
	void setArgumentQuoteDelimiter(char argQuoteDelimiterParameter) {
		this.argQuoteDelimiter = argQuoteDelimiterParameter;
	}

	char getArgumentQuoteDelimiter() {
		return argQuoteDelimiter;
	}

	/**
	 * @param exeQuoteDelimiterParameter {@link #exeQuoteDelimiter}
	 */
	void setExecutableQuoteDelimiter(char exeQuoteDelimiterParameter) {
		this.exeQuoteDelimiter = exeQuoteDelimiterParameter;
	}

	char getExecutableQuoteDelimiter() {
		return exeQuoteDelimiter;
	}

	/**
	 * Get the full command line to execute, including shell command, shell
	 * arguments, executable and executable arguments
	 *
	 * @param arguments arguments for the executable, not the shell
	 * @return List of String objects, whose array version is suitable to be used as
	 *         argument of Runtime.getRuntime().exec()
	 */
	public List<String> getShellCommandLine(String... arguments) {

		List<String> commandLine = new ArrayList<>();

		if (getShellCommand() != null) {
			commandLine.add(getShellCommand());
		}

		if (getShellArgs() != null) {
			commandLine.addAll(getShellArgsList());
		}

		commandLine.addAll(getCommandLine(executable, arguments));

		return commandLine;

	}

	List<String> getShellArgsList() {
		return shellArgs;
	}

	/**
	 * @param quotedArgumentsEnabled {@link #quotedArgumentsEnabled}
	 */
	public void setQuotedArgumentsEnabled(boolean quotedArgumentsEnabled) {
		this.quotedArgumentsEnabled = quotedArgumentsEnabled;
	}

	boolean isQuotedArgumentsEnabled() {
		return quotedArgumentsEnabled;
	}

	void setQuotedExecutableEnabled(boolean quotedExecutableEnabled) {
		this.quotedExecutableEnabled = quotedExecutableEnabled;
	}

	boolean isQuotedExecutableEnabled() {
		return quotedExecutableEnabled;
	}

	/**
	 * Sets the executable to run.
	 * 
	 * @param executable The executable.
	 */
	public void setExecutable(String executable) {
		if ((executable == null) || (executable.length() == 0)) {
			return;
		}
		this.executable = executable.replace('/', File.separatorChar).replace('\\', File.separatorChar);
	}

	/**
	 * @return The executable.
	 */
	public String getExecutable() {
		return executable;
	}

	/**
	 * Sets execution directory.
	 * 
	 * @param path The path which should be used as working directory.
	 */
	public void setWorkingDirectory(String path) {
		if (path != null) {
			this.workingDir = path;
		}
	}

	/**
	 * Sets execution directory.
	 * 
	 * @param workingDirectory the working directory
	 */
	public void setWorkingDirectory(File workingDirectory) {
		if (workingDirectory != null) {
			this.workingDir = workingDirectory.getAbsolutePath();
		}
	}

	/**
	 * @return the working directory
	 */
	public File getWorkingDirectory() {
		return workingDir == null ? null : new File(workingDir);
	}

	String getWorkingDirectoryAsString() {
		return workingDir;
	}

	/** {@inheritDoc} */
	public Object clone() {
		throw new RuntimeException("Do we ever clone this?");
		/*
		 * Shell shell = new Shell(); shell.setExecutable( getExecutable() );
		 * shell.setWorkingDirectory( getWorkingDirectory() ); shell.setShellArgs(
		 * getShellArgs() ); return shell;
		 */
	}

	void setSingleQuotedArgumentEscaped(boolean singleQuotedArgumentEscaped) {
		this.singleQuotedArgumentEscaped = singleQuotedArgumentEscaped;
	}

	void setSingleQuotedExecutableEscaped(boolean singleQuotedExecutableEscaped) {
		this.singleQuotedExecutableEscaped = singleQuotedExecutableEscaped;
	}

	public boolean isUnconditionalQuoting() {
		return unconditionalQuoting;
	}

	public void setUnconditionalQuoting(boolean unconditionalQuoting) {
		this.unconditionalQuoting = unconditionalQuoting;
	}

	public static String quoteAndEscape(String source, char quoteChar, final char[] escapedChars,
	        final char[] quotingTriggers, char escapeChar, boolean force) {
		if (source == null) {
			return null;
		}

		if (!force && source.startsWith(Character.toString(quoteChar))
		        && source.endsWith(Character.toString(quoteChar))) {
			return source;
		}

		String escaped = escape(source, escapedChars, escapeChar);

		boolean quote = false;
		if (force) {
			quote = true;
		} else if (!escaped.equals(source)) {
			quote = true;
		} else {
			for (char quotingTrigger : quotingTriggers) {
				if (escaped.indexOf(quotingTrigger) > -1) {
					quote = true;
					break;
				}
			}
		}

		if (quote) {
			return quoteChar + escaped + quoteChar;
		}

		return escaped;
	}

	public static String escape(String source, final char[] escapedChars, char escapeChar) {
		if (source == null) {
			return null;
		}

		char[] eqc = new char[escapedChars.length];
		System.arraycopy(escapedChars, 0, eqc, 0, escapedChars.length);
		Arrays.sort(eqc);

		StringBuilder buffer = new StringBuilder(source.length());

		for (int i = 0; i < source.length(); i++) {
			final char c = source.charAt(i);
			int result = Arrays.binarySearch(eqc, c);

			if (result > -1) {
				buffer.append(escapeChar);
			}
			buffer.append(c);
		}

		return buffer.toString();
	}
}
