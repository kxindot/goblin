package com.kxindot.goblin.system.shell;

import java.util.Arrays;
import java.util.List;

public class CmdShell extends Shell {
	/**
	 * Create an instance of CmdShell.
	 */
	public CmdShell() {
		setShellCommand("cmd.exe");
		setQuotedExecutableEnabled(true);
		setShellArgs(new String[] { "/X", "/C" });
	}

	/**
	 * <p>
	 * Specific implementation that quotes all the command line.
	 * </p>
	 * <p>
	 * Workaround for http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6468220
	 * </p>
	 * <p>
	 * From cmd.exe /? output:
	 * </p>
	 * 
	 * <pre>
	 *      If /C or /K is specified, then the remainder of the command line after
	 *      the switch is processed as a command line, where the following logic is
	 *      used to process quote (&quot;) characters:
	 *
	 *      1.  If all of the following conditions are met, then quote characters
	 *      on the command line are preserved:
	 *
	 *      - no /S switch
	 *      - exactly two quote characters
	 *      - no special characters between the two quote characters,
	 *      where special is one of: &amp;&lt;&gt;()@&circ;|
	 *      - there are one or more whitespace characters between the
	 *      the two quote characters
	 *      - the string between the two quote characters is the name
	 *      of an executable file.
	 *
	 *      2.  Otherwise, old behavior is to see if the first character is
	 *      a quote character and if so, strip the leading character and
	 *      remove the last quote character on the command line, preserving
	 *      any text after the last quote character.
	 * </pre>
	 * <p>
	 * Always quoting the entire command line, regardless of these conditions
	 * appears to make Windows processes invoke successfully.
	 * </p>
	 * 
	 * @param executable The executable.
	 * @param arguments  The arguments for the executable.
	 * @return The resulting command line.
	 */
	public List<String> getCommandLine(String executable, String... arguments) {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		sb.append(super.getCommandLine(executable, arguments).get(0));
		sb.append('"');

		return Arrays.asList(sb.toString());
	}
}
