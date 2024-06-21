package com.kxindot.goblin.io.filewatch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

/**
 * 监听事件：
 * <pre>
 * 文件创建事件
 * 
 * </pre>
 * 新建文件、更新文件、删除文件、新建文件夹、更新文件夹、删除文件夹
 * 
 * @author ZhaoQingJiang
 */
public enum FileWatchEvent {

	FILE_CREATE(ENTRY_CREATE),
	
	FILE_MODIFY(ENTRY_MODIFY),
	
	FILE_DELETE(ENTRY_DELETE),
	
	DIRECTORY_CREATE(ENTRY_CREATE),
	
	DIRECTORY_MODIFY(ENTRY_MODIFY),
	
	DIRECTORY_DELETE(ENTRY_DELETE);
	
	private Kind<Path> kind;

	private FileWatchEvent(Kind<Path> kind) {
		this.kind = kind;
	}
	
	public Kind<Path> getKind() {
		return kind;
	}
	
	
	public static FileWatchEvent getEvent(Kind<Path> kind) {
		
		return null;
	}
	
}
