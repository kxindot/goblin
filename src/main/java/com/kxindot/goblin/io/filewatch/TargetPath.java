package com.kxindot.goblin.io.filewatch;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author ZhaoQingJiang
 */
public class TargetPath {

	private Path target;
	
	private boolean recursive;
	
	private Collection<Notifier> notifiers;
	
	private Pattern nameRegex;
	
	private Set<FileWatchEvent> events;
	
}
