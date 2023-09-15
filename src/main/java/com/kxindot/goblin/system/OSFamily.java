package com.kxindot.goblin.system;

import static com.kxindot.goblin.Objects.newUnmodifiableSet;

import java.util.Set;

import com.kxindot.goblin.EnumValue;

public enum OSFamily implements EnumValue<String> {

	WINDOWS("windows"), WIN9X("win9x"), NT("winnt"),

	OS2("os/2"), NETWARE("netware"), DOS("dos"),

	MAC("mac"), TANDEM("tandem"), UNIX("unix"),

	OPENVMS("openvms"), ZOS("z/os"), OS400("os/400");

	String val;

	private OSFamily(String val) {
		this.val = val;
	}

	public String value() {
		return val;
	}

	public static Set<OSFamily> families() {
		return newUnmodifiableSet(OSFamily.class.getEnumConstants());
	}
}