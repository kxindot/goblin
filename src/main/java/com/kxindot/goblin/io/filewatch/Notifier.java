package com.kxindot.goblin.io.filewatch;

import java.nio.file.Path;

/**
 * 文件夹监听接口.
 * 
 * @author ZhaoQingJiang
 */
public interface Notifier {

	/**
	 * 被监听文件夹中有新文件创建,则此方法将被调用.
	 * 
	 * @param file 新建的文件
	 */
	void onFileCreated(Path file);
	
	/**
	 * 被监听文件夹中有文件更新,则此方法将被调用.
	 * 
	 * @param file 更新的文件
	 */
	void onFileModified(Path file);
	
	/**
	 * 被监听文件夹中有文件删除,则此方法将被调用.
	 * 
	 * @param file 删除的文件
	 */
	void onFileDeteled(Path file);
	
	/**
	 * 被监听文件夹中有新文件夹创建,则此方法将被调用.
	 * 
	 * @param directory 新建的文件夹
	 */
	void onDirectoryCreated(Path directory);
	
	/**
	 * 被监听文件夹中有文件夹更新,则此方法将被调用.
	 * 
	 * @param directory 更新的文件夹
	 */
	void onDirectoryModified(Path directory);
	
	/**
	 * 被监听文件夹中有文件夹删除,则此方法将被调用.
	 * 
	 * @param directory 删除的文件夹
	 */
	void onDirectoryDeleted(Path directory);
	
}
