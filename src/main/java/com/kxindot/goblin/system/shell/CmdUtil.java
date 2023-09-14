package com.kxindot.goblin.system.shell;

import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Throws.threx;

import java.util.List;
import java.util.StringTokenizer;

/**
 * @author ZhaoQingJiang
 */
public abstract class CmdUtil {

	
	
	public static String[] translate(String command) {
		if (isBlank(command)) {
			return new String[0];
		}
		final int normal = 0;
		final int inQuote = 1;
		final int inDoubleQuote = 2;
		boolean inEscape = false;
		int state = normal;
		final StringTokenizer tok = new StringTokenizer(command, "\"\' \\", true);
		List<String> tokens = newArrayList();
		StringBuilder current = new StringBuilder();

		while (tok.hasMoreTokens()) {
			String nextTok = tok.nextToken();
			switch (state) {
			case inQuote:
				if ("\'".equals(nextTok)) {
					if (inEscape) {
						current.append(nextTok);
						inEscape = false;
					} else {
						state = normal;
					}
				} else {
					current.append(nextTok);
					inEscape = "\\".equals(nextTok);
				}
				break;
			case inDoubleQuote:
				if ("\"".equals(nextTok)) {
					if (inEscape) {
						current.append(nextTok);
						inEscape = false;
					} else {
						state = normal;
					}
				} else {
					current.append(nextTok);
					inEscape = "\\".equals(nextTok);
				}
				break;
			default:
				if ("\'".equals(nextTok)) {
					if (inEscape) {
						inEscape = false;
						current.append(nextTok);
					} else {
						state = inQuote;
					}
				} else if ("\"".equals(nextTok)) {
					if (inEscape) {
						inEscape = false;
						current.append(nextTok);
					} else {
						state = inDoubleQuote;
					}
				} else if (" ".equals(nextTok)) {
					if (current.length() != 0) {
						tokens.add(current.toString());
						current.setLength(0);
					}
				} else {
					current.append(nextTok);
					inEscape = "\\".equals(nextTok);
				}
				break;
			}
		}

		if (current.length() != 0) {
			tokens.add(current.toString());
		}
		if ((state == inQuote) || (state == inDoubleQuote)) {
			threx(CmdlineException::new, "命令不合法: ", command);
		}
		return tokens.toArray(new String[tokens.size()]);
	}
	
}
