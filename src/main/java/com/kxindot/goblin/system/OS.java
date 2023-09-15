package com.kxindot.goblin.system;

import static com.kxindot.goblin.system.OSFamily.DOS;
import static com.kxindot.goblin.system.OSFamily.MAC;
import static com.kxindot.goblin.system.OSFamily.NETWARE;
import static com.kxindot.goblin.system.OSFamily.NT;
import static com.kxindot.goblin.system.OSFamily.OPENVMS;
import static com.kxindot.goblin.system.OSFamily.OS2;
import static com.kxindot.goblin.system.OSFamily.OS400;
import static com.kxindot.goblin.system.OSFamily.TANDEM;
import static com.kxindot.goblin.system.OSFamily.UNIX;
import static com.kxindot.goblin.system.OSFamily.WIN9X;
import static com.kxindot.goblin.system.OSFamily.WINDOWS;
import static com.kxindot.goblin.system.OSFamily.ZOS;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZhaoQingJiang
 */
public class OS {

	public static final String NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

	public static final String ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

	public static final String VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);

	public static final String PATH_SEPARATOR = System.getProperty("path.separator").toLowerCase(Locale.ENGLISH);

	public static final String LINE_SEPARATOR = System.getProperty("line.separator").toLowerCase(Locale.ENGLISH);

	public static final OSFamily FAMILY = getFamily();

	private static final Set<String> FAMILIES = OSFamily.families().stream().map(e -> e.value()).collect(Collectors.toSet());

	
	public static boolean isValidFamily(String family) {
		return FAMILIES.contains(family);
	}
	
	public static boolean isFamily(String family) {
		return isOS(family, null, null, null);
	}

	public static boolean isName(String name) {
		return isOS(null, name, null, null);
	}

	public static boolean isArch(String arch) {
		return isOS(null, null, arch, null);
	}

	public static boolean isVersion(String version) {
		return isOS(null, null, null, version);
	}

	private static boolean isOS(String family, String name, String arch, String version) {
		boolean retValue = false;
		if (family != null || name != null || arch != null || version != null) {

			boolean isFamily = true;
			boolean isName = true;
			boolean isArch = true;
			boolean isVersion = true;

			if (family != null) {
				boolean isWindows = NAME.contains(WINDOWS.val);
				boolean is9x = false;
				boolean isNT = false;
				if (isWindows) {
					is9x = NAME.contains("95") || NAME.contains("98") || NAME.contains("me") || NAME.contains("ce");
					isNT = !is9x;
				}
				if (family.equals(WINDOWS.val)) {
					isFamily = isWindows;
				} else if (family.equals(WIN9X.val)) {
					isFamily = isWindows && is9x;
				} else if (family.equals(NT.val)) {
					isFamily = isWindows && isNT;
				} else if (family.equals(OS2.val)) {
					isFamily = NAME.contains(OS2.val);
				} else if (family.equals(NETWARE.val)) {
					isFamily = NAME.contains(NETWARE.val);
				} else if (family.equals(DOS.val)) {
					isFamily = PATH_SEPARATOR.equals(";") && !isFamily(NETWARE.val);
				} else if (family.equals(MAC.val)) {
					isFamily = NAME.contains(MAC.val) || NAME.contains("darwin");
				} else if (family.equals(TANDEM.val)) {
					isFamily = NAME.contains("nonstop_kernel");
				} else if (family.equals(UNIX.val)) {
					isFamily = PATH_SEPARATOR.equals(":") && !isFamily(OPENVMS.val)
					        && (!isFamily(MAC.val) || NAME.endsWith("x") || NAME.contains("darwin"));
				} else if (family.equals(ZOS.val)) {
					isFamily = NAME.contains(ZOS.val) || NAME.contains("os/390");
				} else if (family.equals(OS400.val)) {
					isFamily = NAME.contains(OS400.val);
				} else if (family.equals(OPENVMS.val)) {
					isFamily = NAME.contains(OPENVMS.val);
				} else {
					isFamily = NAME.contains(family.toLowerCase(Locale.US));
				}
			}
			if (name != null) {
				isName = name.equals(NAME);
			}
			if (arch != null) {
				isArch = arch.equals(ARCH);
			}
			if (version != null) {
				isVersion = version.equals(VERSION);
			}
			retValue = isFamily && isName && isArch && isVersion;
		}
		return retValue;
	}
	
	private static OSFamily getFamily() {
		Set<OSFamily> families = OSFamily.families();
		for (OSFamily family : families) {
			if (isFamily(family.val)) {
				return family;
			}
		}
		return null;
	}

}
