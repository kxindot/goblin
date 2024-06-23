package com.kxindot.goblin.io.filewatch;

import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Objects.unmodifiableEmptySet;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.iterate;
import static com.kxindot.goblin.Throws.silentThrex;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.util.Collections.synchronizedSet;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * TODO 健壮的文件监听机制
 * 
 * @author ZhaoQingJiang
 */
public class DefaultFileWatcher implements FileWatcher {

	private static final Kind<?>[] DEFULAT_EVENTS = new Kind<?>[] {ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE};
	private static DefaultFileWatcher INSTANCE;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private volatile boolean monitoring = false;
//	private ThreadExecutor watcherThread;
//	private ThreadExecutor notifierThreads;
	private WatchService watchService;
	private Map<Path, WatchPath> paths;
	private Set<Notifier> notifiers;
	
	private DefaultFileWatcher() {
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			silentThrex(e, "创建文件夹监听服务异常!");
		}
		paths = newConcurrentHashMap();
		notifiers = synchronizedSet(newHashSet());
		
	}
	
	public static synchronized FileWatcher instance() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new DefaultFileWatcher());
	}
	
	
	@Override
	public void watch(Path directory, boolean recursive) {
		watch(directory, recursive, unmodifiableEmptySet());
	}

	@Override
	public void watch(Path directory, boolean recursive, Notifier... notifiers) {
		watch(directory, recursive, newHashSet(notifiers));
	}

	@Override
	public void watch(Path directory, boolean recursive, Collection<Notifier> notifiers) {
		watch(directory, recursive, newHashSet(notifiers));
	}
	
	private void watch(Path directory, boolean recursive, Set<Notifier> notifiers) {
		requireNotNull(directory, "directory == null");
		requireTrue(exists(directory), "%s does't exits!", directory);
		requireTrue(isDirectory(directory), "%s is not a directory!", directory);
		register(directory, null, recursive, notifiers, DEFULAT_EVENTS);
	}
	
	private WatchPath register(Path path, WatchDirectory parent, 
			boolean recursive, Set<Notifier> notifiers, Kind<?>... kinds) {
		WatchPath wp;
		if (isFile(path)) {
			wp = new WatchFile(path);
		} else {
			wp = new WatchDirectory(path);
			if (recursive) {
				iterate(path, sub -> register(sub, (WatchDirectory) wp, recursive, notifiers, kinds));
			}
		}
		wp.startWatch(kinds);
		wp.setNotifiers(notifiers);
		if (isNotNull(parent)) {
			parent.addSubPath(wp);
		}
		return wp;
	}

	@Override
	public List<Notifier> getNotifiers(Path directory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notifier> getGlobalNotifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlobalNotifier(Notifier notifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGlobalNotifier(Notifier... notifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNotifier(Path directory, Notifier notifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNotifier(Path directory, Notifier... notifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNotifiers(Path directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGlobalNotifier(Notifier notifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGlobalNotifier(Notifier... notifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGlobalNotifiers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopWatch(Path directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopWatch(Path... directories) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopWatchAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @author ZhaoQingJiang
	 */
	abstract class WatchPath {
		
		Path path;
		Set<Notifier> notifiers;
		
		WatchPath(Path path) {
			this.path = path;
		}
		
		void setNotifiers(Set<Notifier> notifiers) {
			if (isNotEmpty(notifiers)) {
				this.notifiers = synchronizedSet(notifiers);
			}
		}
		
		void addNotifier(Notifier notifier) {
			if (isNull(this.notifiers)) {
				this.notifiers = synchronizedSet(newHashSet());
			}
			this.notifiers.add(notifier);
		}
		
		void removeNotifier(Notifier notifier) {
			if (isNotEmpty(this.notifiers)) {
				this.notifiers.remove(notifier);
			}
		}
		
		void removeNotifiers() {
			if (isNotEmpty(this.notifiers)) {
				this.notifiers.clear();
				this.notifiers = null;
			}
		}
		
		abstract void startWatch(Kind<?>... kinds);
		
		abstract void stopWatch();
		
	}
	
	
	/**
	 * @author ZhaoQingJiang
	 */
	class WatchFile extends WatchPath {

		WatchFile(Path path) {
			super(path);
		}

		@Override
		void startWatch(Kind<?>... kinds) {}

		@Override
		void stopWatch() {}
		
	}
	
	/**
	 * @author ZhaoQingJiang
	 */
	class WatchDirectory extends WatchPath {
		
		WatchKey key;
		Map<Path, WatchPath> subPaths;
		
		WatchDirectory(Path path) {
			super(path);
		}
		
		void addSubPath(WatchPath path) {
//			subPaths = defaultIfNull(subPaths, Objects::newConcurrentHashMap);
			subPaths.put(path.path, path);
		}
		
		@Override
		void setNotifiers(Set<Notifier> notifiers) {
			super.setNotifiers(notifiers);
			if (isNotEmpty(subPaths)) {
				subPaths.values().forEach(e -> e.setNotifiers(notifiers));
			}
		}
		
		@Override
		void addNotifier(Notifier notifier) {
			super.addNotifier(notifier);
			if (isNotEmpty(subPaths)) {
				subPaths.values().forEach(e -> e.addNotifier(notifier));
			}
		}
		
		@Override
		void removeNotifier(Notifier notifier) {
			super.removeNotifier(notifier);
			if (isNotEmpty(subPaths)) {
				subPaths.values().forEach(e -> e.removeNotifier(notifier));
			}
		}
		
		@Override
		void removeNotifiers() {
			super.removeNotifiers();
			if (isNotEmpty(subPaths)) {
				subPaths.values().forEach(e -> e.removeNotifiers());
			}
		}

		@Override
		void startWatch(Kind<?>... kinds) {
			try {
				path.register(watchService, kinds);
			} catch (IOException e) {
				logger.error("文件夹注册监听失败: {}", path, e);
			}
		}
		
		void stopWatch() {
			key.cancel();
			if (isNotEmpty(this.notifiers)) {
				this.notifiers.clear();
				this.notifiers = null;
			}
			if (isNotEmpty(subPaths)) {
				subPaths.values().forEach(e -> stopWatch());
			}
		}
		
	}
	
}
