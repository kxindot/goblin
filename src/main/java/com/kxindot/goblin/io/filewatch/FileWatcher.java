package com.kxindot.goblin.io.filewatch;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * recursive
 * regex
 * event
 * 
 * @author ZhaoQingJiang
 */
public interface FileWatcher {

	/**
	 * 开始监听指定目录。
	 * 
	 * @param directory
	 */
	default void watch(Path directory) {
		watch(directory, false);
	}
	
	void watch(Path directory, boolean recursive);

	default void watch(Path directory, Notifier... notifiers) {
		watch(directory, false, notifiers);
	}
	
	default void watch(Path directory, Collection<Notifier> notifiers) {
		watch(directory, false, notifiers);
	}
	
	void watch(Path directory, boolean recursive, Notifier... notifiers);
	
	void watch(Path directory, boolean recursive, Collection<Notifier> notifiers);
	
	/**
	 * 
	 * 
	 * @param directory
	 * @return
	 */
	List<Notifier> getNotifiers(Path directory);
	
	/**
	 * 
	 * 
	 * @return
	 */
	List<Notifier> getGlobalNotifiers();
	
	/**
	 * 
	 * 
	 * @param notifier
	 */
	void setGlobalNotifier(Notifier notifier);
	
	/**
	 * 
	 * 
	 * @param notifiers
	 */
	void setGlobalNotifier(Notifier... notifiers);
	
	/**
	 * 
	 * 
	 * @param directory
	 * @param notifier
	 */
	void removeNotifier(Path directory, Notifier notifier);
	
	/**
	 * 
	 * 
	 * @param directory
	 * @param notifiers
	 */
	void removeNotifier(Path directory, Notifier... notifiers);
	
	/**
	 * 
	 * 
	 * @param directory
	 */
	void removeNotifiers(Path directory);
	
	/**
	 * 
	 * 
	 * @param notifier
	 */
	void removeGlobalNotifier(Notifier notifier);

	/**
	 * 
	 * 
	 * @param notifiers
	 */
	void removeGlobalNotifier(Notifier... notifiers);
	
	/**
	 * 
	 */
	void removeGlobalNotifiers();
	
	/**
	 * 
	 * 
	 * @param directories
	 */
	void stopWatch(Path directory);

	/**
	 * 
	 * 
	 * @param directories
	 */
	void stopWatch(Path... directories);
	
	/**
	 * 
	 */
	void stopWatchAll();
	
	/**
	 * 
	 */
	void close();
	
}
