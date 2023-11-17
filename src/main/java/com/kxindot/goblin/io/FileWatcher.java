package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Throws.silentThrex;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.List;

/**
 * @author ZhaoQingJiang
 */
public class FileWatcher {

	private Path directory;
	private WatchService service;
	private List<Whistle> whistles;
	
	public FileWatcher(Path directory) {
		requireNotNull(directory, "directory == null");
		requireTrue(exists(directory), "%s does't exits!", directory);
		requireTrue(isDirectory(directory), "%s is not a directory!", directory);
		this.directory = directory;
		try {
			service = FileSystems.getDefault().newWatchService();
			directory.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e) {
			silentThrex(e);
		}
		this.whistles = Collections.synchronizedList(newArrayList());
	}
	
	public void add(Whistle whistle) {
		if (isNotNull(whistle)) {
			whistles.add(whistle);
		}
	}
	
	public void watch() {
		while (true) {
			WatchKey key;
			try {
				key = service.take();
			} catch (InterruptedException e) {
				return;
			}
			if (key.isValid()) {
				for (WatchEvent<?> event : key.pollEvents()) {
					whistle(event);
				}
			}
			if (!key.reset()) {
				return;
			}
		}
	}
	
	public void close() {
		try {
			service.close();
		} catch (IOException e) {
			silentThrex(e);
		}
	}
	
	
	private void whistle(WatchEvent<?> event) {
		if (whistles.isEmpty()) {return;}
		for (Whistle whistle : whistles) {
			Kind<?> kind = event.kind();
			Path path = directory.resolve((Path) event.context());
			if (isFile(path)) {
				if (kind == ENTRY_CREATE) {
					whistle.onFileCreated(path);
				} else if (kind == ENTRY_DELETE) {
					whistle.onFileDeteled(path);
				} else if (kind == ENTRY_MODIFY) {
					whistle.onFileModified(path);
				}
			} else if (isDirectory(path)) {
				if (kind == ENTRY_CREATE) {
					whistle.onDirectoryCreated(path);
				} else if (kind == ENTRY_DELETE) {
					whistle.onDirectoryDeleted(path);
				} else if (kind == ENTRY_MODIFY) {
					whistle.onDirectoryModified(path);
				}
			}
		}
	}
	
	
	public interface Whistle {
		
		default void onFileCreated(Path file) {}
		
		default void onFileModified(Path file) {}
		
		default void onFileDeteled(Path file) {}
		
		default void onDirectoryCreated(Path directory) {}
		
		default void onDirectoryModified(Path directory) {}
		
		default void onDirectoryDeleted(Path directory) {}
	}
	
}
