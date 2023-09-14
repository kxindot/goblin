package com.kxindot.goblin.system.shell;

import java.io.File;

/**
 * @author ZhaoQingJiang
 */
public class CmdArg {
	
	private boolean mask;
	private String[] parts;
	
	public boolean isMask() {
		return mask;
	}
	
	public String[] getParts() {
		return parts;
	}
	
	public void setValue(String value) {
		if (value != null) {
			parts = new String[] {value};
		}
	}
	
	public void setLine(String line) {
		if (line != null) {
			parts = CmdUtil.translate(line);
		}
	}
	
	public void setFile(File file) {
		parts = new String[] {file.getAbsolutePath()};
	}
	
	public void setMask(boolean mask) {
		this.mask = mask;
	}
	
}