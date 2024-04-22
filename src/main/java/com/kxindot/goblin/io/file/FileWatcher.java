package com.kxindot.goblin.io.file;

import static com.kxindot.goblin.Objects.*;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * 用于监视指定目录下文件的创建、删除和修改事件
 * <p>
 *      该类是可以支持递归监听子目录，也可以只监听当前目录，可以设置最大深度，规则如下：
 *      <ul>
 *          <li>如果只监听当前目录，则当前目录有事件触发不会向上冒泡到父级,默认策略</li>
 *          <li>如果直接删除了目录，则只会触发一个目录的删除事件，不会继续向下递归，默认策略</li>
 *          <li>如果设置了最大深度，则只监听深度小于等于最大深度的目录</li>
 *          <li>如果是目录或文件重命名，则会有一个命名前文件的删除和命名后文件的创建事件</li>
 *     </ul>
 *     该类支持close，可以关闭监听器，释放资源，但不会抛出CloseWatchServiceException异常
 *<p>
 * @author ZhaoQingJiang
 */
public class FileWatcher implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcher.class);

    private static final String FILE_WATCHER = "FileWatcher";
    // 监听目录
    private final Path dir;
    // 是否递归监听
    private final boolean recursive;
    // 最大深度
    private final int maxDepth;
    // 是否只监听当前目录
    private final boolean watchCurrentDirOnly;
    // 监听服务
    private final WatchService watchService;
    // 监听器
    private Whistle whistle;
    private final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();

    /**
     * 创建一个文件监听器
     *
     * @param dir
     *         监听的目录
     * @param recursive
     *         是否递归监听
     * @param maxDepth
     *         最大深度
     * @param watchCurrentDirOnly
     *         是否只监听当前目录
     * @throws IOException
     */
    FileWatcher(Path dir, boolean recursive, int maxDepth, boolean watchCurrentDirOnly) throws IOException {
        this.dir = dir;
        this.watchService = FileSystems.getDefault().newWatchService();
        this.recursive = recursive;
        this.maxDepth = maxDepth;
        this.watchCurrentDirOnly = watchCurrentDirOnly;
    }

    /**
     * 创建一个文件监听器
     *
     * @param dir 监听的目录
     * @throws IOException
     */
    FileWatcher(Path dir) throws IOException {
        this(dir, true, 5, true);
    }

    /**
     * 监听指定目录下的变化
     *
     * @throws IOException 监听异常
     */
    public void watch() throws IOException {
        requireTrue(exists(dir), "File: %s is not exist!", dir.toAbsolutePath());
        requireTrue(isDirectory(dir), "File: %s is not a directory!", dir.toAbsolutePath());
        if (recursive) {
            LOGGER.info("Scanning {} ...", dir);
            registerAll(dir);
            LOGGER.info("Register recursive in {} done", dir);
        } else {
            register(dir);
            LOGGER.info("Register in {} done", dir);
        }
        Thread thread = new Thread(this, FILE_WATCHER);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 注册监听
     *
     * @param dir 待注册的目录
     * @throws IOException 注册异常
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        Path prev = keys.get(key);
        if (prev == null) {
            LOGGER.info("register: {}", dir);
        } else {
            if (!dir.equals(prev)) {
                LOGGER.info("update: {} -> {}", prev, dir);
            }
        }
        keys.put(key, dir);
    }

    /**
     * 注册目录下包括所有子目录的监听
     *
     * @param directory 待注册的目录
     * @throws IOException 注册异常
     */
    private void registerAll(final Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs) throws IOException {
                if (depth(directory, dir) > maxDepth) {
                    LOGGER.warn("Max depth reached: {}", directory);
                    return FileVisitResult.TERMINATE;
                }
                register(directory);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 计算目录深度
     *
     * @param subDir 子目录
     * @param dir 目录
     * @return 深度
     */
    private int depth(Path subDir, Path dir) {
        int depth = 0;
        try {
            if (Files.isSameFile(subDir, dir)) {
                return depth;
            }
            depth = 1;
            Path parent = subDir.getParent();
            while (parent != null && !Files.isSameFile(parent, dir)) {
                depth++;
                parent = parent.getParent();
            }
        } catch (IOException e) {
            LOGGER.error("Error while comparing paths: {} and {}", subDir, dir, e);
        }
        return depth;
    }

    @Override
    public void run() {
        while (!keys.isEmpty()) {
            WatchKey key;
            try {
                // 获取监听key，并阻塞
                key = watchService.take();
            } catch (InterruptedException e) {
                // 重置中断状态
                Thread.currentThread().interrupt();
                LOGGER.error("Error while polling events for path: {}", dir.toAbsolutePath(), e);
                return;
            } catch (ClosedWatchServiceException e) {
                LOGGER.error("WatchService is closed!", e);
                return;
            }
            Path dirInMap = keys.get(key);
            if (dirInMap == null) {
                LOGGER.error("WatchKey not recognized!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                // 获取事件触发的路径
                Path path = dirInMap.resolve(watchEvent.context());
                LOGGER.info("Event: {} -> {}", event.kind().name(), path);
                if (whistle == null) {
                    //客户端未注册监听器
                    LOGGER.warn("no one is listening to file changes.");
                    return;
                } else {
                    //触发已注册的监听器
                    whistle(dirInMap, path, event);
                }
            }
            // 重置key并且移除无效的key
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
            }
        }
    }

    /**
     * 触发监听器
     *
     * @param path 监听的目录
     * @param pathWatchable 事件触发的路径
     * @param event 事件
     */
    private void whistle(Path path, Path pathWatchable, WatchEvent<?> event) {
        Kind<?> kind = event.kind();
        switch (kind.name()) {
        case "ENTRY_CREATE":
            whistle.onCreated(pathWatchable);
            // 如果是目录，且递归监听，则注册监听
            if (recursive) {
                try {
                    if (Files.isDirectory(pathWatchable, NOFOLLOW_LINKS)) {
                        registerAll(pathWatchable);
                    }
                } catch (IOException e) {
                    LOGGER.error("Error while registering directory: {}", pathWatchable, e);
                }
            }
            break;
        case "ENTRY_DELETE":
            whistle.onDeleted(pathWatchable);
            break;
        case "ENTRY_MODIFY":
            // 如果因为子目录变更向上冒泡为父目录的变更，并且设置为只监听当前目录，则忽略
            if (noNeedToWhistle(path, pathWatchable)) {
                LOGGER.info("Ignore the modify event: {}", pathWatchable);
                break;
            }
            whistle.onModified(pathWatchable);
            break;
        default:
            LOGGER.warn("Unknown event kind: {}", kind);
            break;
        }

    }

    /**
     * 忽略监听触发
     *
     * @param path 文件路径
     * @param pathWatchable 事件触发路径
     * @return 是否忽略
     */
    private boolean noNeedToWhistle(Path path, Path pathWatchable) {
        // 如果只监听当前目录，且事件触发非当前目录
        if (watchCurrentDirOnly && path.compareTo(pathWatchable) != 0) {
            // 如果时间触发目录是当前目录的子目录，则忽略
            return Files.isDirectory(pathWatchable, NOFOLLOW_LINKS) && pathWatchable.startsWith(path);
        }
        return false;
    }

    /**
     * 关闭监听器
     */
    public void close() {
        try {
            watchService.close();
        } catch (IOException e) {
            LOGGER.error("Error while closing WatchService", e);
        }
    }

    /**
     * 注册监听器
     *
     * @param whistle 监听器
     * @return FileWatcher
     */
    public FileWatcher setWhistle(Whistle whistle) {
        this.whistle = whistle;
        return this;
    }

    /**
     * 移除监听器
     *
     * @return FileWatcher
     */
    public FileWatcher removeWhistle() {
        whistle = null;
        return this;
    }

    /**
     * 获取监听器
     *
     * @return Whistle
     */
    public Whistle getWhistle() {
        return whistle;
    }
}
