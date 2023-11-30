package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * @author ZhaoQingJiang
 */
public class FileWatcher {

	private static Logger logger = LoggerFactory.getLogger(FileWatcher.class);
	private Path directory;
	private WatchService service;
	private List<Whistle> whistles;
	private Map<Path, Boolean> cache;
	
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
		this.cache = newConcurrentHashMap();
		buildCache(directory);
		this.whistles = Collections.synchronizedList(newArrayList());
	}
	
	private void buildCache(Path path) {
		this.cache.put(path, isFile(path));
		if (isDirectory(path)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
	            Iterator<Path> iterator = stream.iterator();
	            while (iterator.hasNext()) {
	            	buildCache((Path) iterator.next());
	            }
	        } catch (IOException e) {
	        	threx(IIOException::new, e);
	        }
		}
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
					try {
						whistle(event);
					} catch (Throwable e) {
						logger.error("", e);
					}
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
			boolean isFile = isFile(path);
			if (kind == ENTRY_CREATE) {
				notify(isFile, path, whistle::onFileCreated, whistle::onDirectoryCreated);
				this.cache.put(path, isFile(path));
			} else if (kind == ENTRY_DELETE) {
				isFile = this.cache.get(path);
				notify(isFile, path, whistle::onFileDeteled, whistle::onDirectoryDeleted);
				this.cache.remove(path);
			} else if (kind == ENTRY_MODIFY) {
				notify(isFile, path, whistle::onFileModified, whistle::onDirectoryModified);
			}
		}
	}
	
	private void notify(boolean isFile, Path path,
			Consumer<Path> onFile, Consumer<Path> onDirectory) {
		if (isFile) {
			onFile.accept(path);
		} else {
			onDirectory.accept(path);
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
