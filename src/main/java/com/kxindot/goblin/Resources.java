package com.kxindot.goblin;

import static com.kxindot.goblin.Classes.Package_Separator;
import static com.kxindot.goblin.Classes.Path_Separator;
import static com.kxindot.goblin.Classes.getAvailableClassLoader;
import static com.kxindot.goblin.Classes.toPackagePattern;
import static com.kxindot.goblin.Classes.toPathPattern;
import static com.kxindot.goblin.Objects.Backslash;
import static com.kxindot.goblin.Objects.Colon;
import static com.kxindot.goblin.Objects.Dot;
import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.Exclamation;
import static com.kxindot.goblin.Objects.Hyphen;
import static com.kxindot.goblin.Objects.Slash;
import static com.kxindot.goblin.Objects.containsAny;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.stringRemove;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;
import static java.io.File.separator;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import com.kxindot.goblin.io.IIOException;
import com.kxindot.goblin.io.IO;
import com.kxindot.goblin.io.IOInput;
import com.kxindot.goblin.io.IOOutput;
import com.kxindot.goblin.io.IOReader;
import com.kxindot.goblin.io.IOWriter;
import com.kxindot.goblin.io.file.FileCopier;

/**
 * 
 * 
 * @author ZhaoQingJiang
 */
public class Resources {
	
	/**
     * 文件分隔符
     */
    public static final String FILE_SEPERATOR = File.separator;
    
    /**
     * Unix文件分隔符
     */
    public static final String UNIX_SEPERATOR = Slash;
    
    /**
     * Windows文件分隔符
     */
    public static final String WINDOWS_SEPERATOR = Backslash;
    
    /**
     * jar包URL协议头: jar
     */
    public static final String URL_Protocol_Jar = "jar";
    /**
     * 文件URL协议头: file
     */
    public static final String URL_Protocol_File = "file";
    /**
     * jar包后缀: .jar
     */
    public static final String Jar_Extension = ".jar";
    /**
     * jar包URL分隔符: !/
     */
    public static final String Jar_Entry_Sep = "!/";
    /**
     * 当前用户主目录
     */
    public static final String User_Home = System.getProperty("user.home");

    /**
     * Load resources with {@link ClassLoader}.
     * If the given ClassLoader is null, 
     * will use {@link Classes#getAvailableClassLoader} method to get one.
     * If there is no ClassLoader can use, throw {@link NullPointerException}.
     * 
     * @see #loadResources(String, ResourceLoader, ClassLoader)
     * @see #loadResources(String, ResourceLoaderAdapter, ClassLoader)
     * @param packageName String
     * @param loader {@code JarResourceLoader<T>}
     * @param classLoader ClassLoader
     * @return {@code Collection<T>} - If no resource was found, return a collection with size == 0
     * @throws NullPointerException If can't find a ClassLoader
     * @throws IORuntimeException An I/O error occurred when ClassLoader search resources
     * @throws ResourceLoadException When {@link JarResourceLoader} throw an uncaught Exception
     */
    public static <T> Collection<T> loadResources(String packageName, 
            JarResourceLoader<T> loader, ClassLoader classLoader) {
        return loadResources(packageName, (ResourceLoader<Collection<T>>) loader, classLoader);
    }
    
    /**
     * Load resources with {@link ClassLoader}.
     * If the given ClassLoader is null, 
     * will use {@link Classes#getAvailableClassLoader} method to get one.
     * If there is no ClassLoader can use, throw {@link NullPointerException}.
     * 
     * @see #loadResources(String, ResourceLoader, ClassLoader)
     * @see #loadResources(String, JarResourceLoader, ClassLoader)
     * @param packageName String
     * @param loader {@code ResourceLoaderAdapter<T>}
     * @param classLoader ClassLoader
     * @return {@code Collection<T>} - If no resource was found, return a collection with size == 0
     * @throws NullPointerException If can't find a ClassLoader
     * @throws IORuntimeException An I/O error occurred when ClassLoader search resources
     * @throws ResourceLoadException When {@link ResourceLoaderAdapter} throw an uncaught Exception
     */
    public static <T> Collection<T> loadResources(String packageName, 
            ResourceLoaderAdapter<T> loader, ClassLoader classLoader) {
        return loadResources(packageName, (ResourceLoader<Collection<T>>) loader, classLoader);
    }
    
    /**
     * Load resources with {@link ClassLoader}.
     * If the given ClassLoader is null, 
     * will use {@link Classes#getAvailableClassLoader} method to get one.
     * If there is no ClassLoader can use, throw {@link NullPointerException}.
     * 
     * @see #loadResources(String, ResourceLoaderAdapter, ClassLoader)
     * @see #loadResources(String, JarResourceLoader, ClassLoader)
     * @param packageName String
     * @param loader {@code ResourceLoader<T>}
     * @param classLoader ClassLoader
     * @return {@code Collection<E>} - If no resource was found, return a collection with size == 0
     * @throws NullPointerException If can't find a ClassLoader
     * @throws IORuntimeException An I/O error occurred when ClassLoader search resources
     * @throws ResourceLoadException When {@link ResourceLoader} throw an uncaught Exception
     */
    @SuppressWarnings("unchecked")
    public static <E, T extends Collection<E>> T loadResources(String packageName, 
            ResourceLoader<T> loader, ClassLoader classLoader) {
        requireNotNull(loader, "loader == null");
        packageName = requireNotNull(packageName, "packageName == null").trim();
        if (isNotBlank(packageName) && packageName.contains(Package_Separator)) {
            packageName = toPathPattern(packageName);
        }
        if (classLoader == null) {
            classLoader = getAvailableClassLoader();
        }
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(packageName);
        } catch (IOException e) {
            silentThrex(e);
        }
        URL url = null;
        Collection<E> c = newHashSet();
        try {
            while (resources.hasMoreElements()) {
                url = (URL) resources.nextElement();
                T r = loader.load(url);
                if (isNotEmpty(r)) {
                    c.addAll(r);
                }
            }
        } catch (Exception e) {
            throw new ResourceLoadException( 
                    "Error occurred when loading resources from URL : " + url, e);
        }
        return (T) c;
    }
    
    /**
     * 加载Jar中的资源
     * @param path Jar路径
     * @param loader 自定义加载器
     * @return {@code Collection<T>}
     */
    public static <T> Collection<T> loadJarResources(String path, JarLoader<T> loader) {
        return loadJarResources(Paths.get(path), loader);
    }
    
    /**
     * 加载Jar中的资源
     * @param file Jar文件
     * @param loader 自定义加载器
     * @return {@code Collection<T>}
     */
    public static <T> Collection<T> loadJarResources(File file, JarLoader<T> loader) {
        return loadJarResources(file.toPath(), loader);
    }
    
    /**
     * 加载Jar中的资源
     * @param path Jar文件
     * @param loader 自定义加载器
     * @return {@code Collection<T>}
     */
    public static <T> Collection<T> loadJarResources(Path path, JarLoader<T> loader) {
        return loadJarResources(path.toUri(), loader);
    }
    
    /**
     * 加载Jar中的资源
     * @param uri URI
     * @param loader 自定义加载器
     * @return {@code Collection<T>}
     */
    public static <T> Collection<T> loadJarResources(URI uri, JarLoader<T> loader) {
        URL url;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new ResourceLoadException(e);
        }
        return loadJarResources(url, loader);
    }
    
    /**
     * 加载Jar包中的资源
     * @param url URL
     * @param loader 自定义加载器
     * @return {@code Collection<T>}
     */
    public static <T> Collection<T> loadJarResources(URL url, JarLoader<T> loader) {
        requireNotNull(loader);
        String protocol = url.getProtocol();
        if (!URL_Protocol_Jar.equals(protocol)) {
            if (URL_Protocol_File.equals(protocol)
                    && url.toExternalForm().endsWith(Jar_Extension)) {
                try {
                    url = new URL(URL_Protocol_Jar + Colon + url.toExternalForm() + Jar_Entry_Sep);
                } catch (MalformedURLException e) {
                    throw new ResourceLoadException(e);
                }
            } else {
                throw new IllegalArgumentException("Input not represent a jar!");
            }
        }
        
        try {
            JarFile file = JarURLConnection.class.cast(url.openConnection()).getJarFile();
            return loadJarResources(url, file, loader);
        } catch (Exception e) {
            throw new ResourceLoadException(
                    "Error occurred when loading resources from jar : " + url, e);
        }
    }
    
    /**
     * 加载Jar包中的资源
     */
    private static <T> Collection<T> loadJarResources(URL url, JarFile file, JarLoader<T> loader) throws Exception {
        Collection<T> c = newHashSet();
        String form = url.toExternalForm();
        String path = form.substring(0, form.lastIndexOf(Exclamation));
        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String pkg = EMP;
            String exs = EMP;
            String name = entry.getName();
            if (name.contains(Package_Separator)) {
                exs = name.substring(name.lastIndexOf(Package_Separator));
                name = name.substring(0, name.lastIndexOf(Package_Separator));
            }
            if (name.contains(Path_Separator)) {
                pkg = toPackagePattern(name.substring(0, name.lastIndexOf(Path_Separator)));
                name = name.substring(name.lastIndexOf(Path_Separator) + 1);
            }
            boolean nb = entry.isDirectory() 
                    ? loader.directoryEntry(url, file, entry, path, pkg, c)
                            : loader.fileEntry(url, file, entry, path, pkg, name, exs, c);
            if (!nb) {
                break;
            }
        }
        return c;
    }
    
    /**
     * 获取随机UUID.
     * 
     * @return String
     */
    public static String UUID() {
    	return UUID(false);
    }
    
    /**
     * 获取随机UUID.
     * 
     * @param keepHyphen 是否保留分割线.
     * @return String
     */
    public static String UUID(boolean keepHyphen) {
    	String uuid = java.util.UUID.randomUUID().toString();
    	return keepHyphen ? uuid : stringRemove(uuid, Hyphen);
    }
    
    /**
     * 判断文件/文件夹是否存在
     * @param file File
     * @return boolean
     */
    public static boolean exists(File file) {
        return file != null && file.exists();
    }
    
    /**
     * 判断文件/文件夹是否存在
     * @param path Path
     * @return boolean
     */
    public static boolean exists(Path path) {
        return path != null && Files.exists(path);
    }
    
    /**
     * 判断指定路径上是否是文件
     * @param filePath String
     * @return boolean
     */
    public static boolean isFile(String filePath) {
        return isNotBlank(filePath) && isFile(Paths.get(filePath));
    }
    
    /**
     * 判断是否是文件
     * @param file File
     * @return boolean
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }
    
    /**
     * 判断是否是文件
     * @param path Path
     * @return boolean
     */
    public static boolean isFile(Path path) {
        return path != null && Files.isRegularFile(path);
    }
    
    /**
     * 判断{@link URI}是否指向一个文件
     * @param uri URI
     * @return boolean
     */
    public static boolean isFile(URI uri) {
        if (uri != null) {
            if (isNotBlank(uri.getScheme())) {
                try {
                    return isFile(Paths.get(uri));
                } catch (Throwable e) {
                    // do nothing
                }
            }
            return isFile(uri.getPath());
        }
        return false;
    }
    
    public static boolean isFile(JarEntry entry) {
        return entry != null && !isDirectory(entry);
    }
    
    public static boolean isDirectory(String directory) {
        return isNotBlank(directory) && isDirectory(Paths.get(directory));
    }
    
    public static boolean isDirectory(File directory) {
        return directory != null && directory.isDirectory();
    }
    
    public static boolean isDirectory(URI uri) {
        if (uri != null) {
            if (isNotBlank(uri.getScheme())) {
                try {
                    return isDirectory(Paths.get(uri));
                } catch (Throwable e) {
                    // do nothing
                }
            }
            return isDirectory(uri.getPath());
        }
        return false;
    }
    
    public static boolean isDirectory(JarEntry entry) {
        return entry != null && entry.isDirectory();
    }
    
    public static boolean isDirectory(Path directory) {
        return directory != null && Files.isDirectory(directory);
    }
    
    public static boolean isJarFile(String jarPath) {
        return isFile(jarPath) && jarPath.endsWith(Jar_Extension);
    }
    
    public static boolean isJarFile(File file) {
        return isFile(file) && file.getName().endsWith(Jar_Extension);
    }
    
    public static boolean isJarFile(Path path) {
        return isFile(path) && path.getFileName().toString().endsWith(Jar_Extension);
    }
    
    public static boolean isJarFile(URI uri) {
        try {
            return isJarFile(uri.toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid uri: " +  uri, e);
        }
    }
    
    public static boolean isJarFile(URL url) {
        return url != null
                && (URL_Protocol_Jar.equals(url.getProtocol())
                || (URL_Protocol_File.equals(url.getProtocol())
                        && url.toExternalForm().endsWith(Jar_Extension)));
    }
    
    public static boolean isJavaSourceFile(File file) {
        return isFile(file) && file.getName().endsWith(SOURCE.extension);
    }
    
    public static boolean isJavaSourceFile(Path path) {
        return isFile(path) && path.getFileName().toString().endsWith(SOURCE.extension);
    }
    
    public static boolean isJavaClassFile(File file) {
        return isFile(file) && file.getName().endsWith(CLASS.extension);
    }
    
    public static boolean isJavaClassFile(Path path) {
        return isFile(path) && path.getFileName().toString().endsWith(CLASS.extension);
    }
    
    /**
     * 校验是否文件。若文件不存在或其不是文件则抛出{@link IllegalArgumentException}异常。
     * 
     * @param file Path
     * @throws IllegalArgumentException 若文件不存在或其不是文件，则抛出此异常。
     */
    public static Path checkFile(Path file) {
    	if (!exists(file)) {
    		threx(IllegalArgumentException::new, "文件不存在：%s", file);
		} else if (!isFile(file)) {
			threx(IllegalArgumentException::new, "指定路径上不是文件：%s", file);
		}
    	return file;
    }
    
    /**
     * 校验是否目录。若目录不存在或其不是目录则抛出{@link IllegalArgumentException}异常。
     * 
     * @param directory Path
     * @throws IllegalArgumentException 若目录不存在或其不是目录，则抛出此异常。
     */
    public static Path checkDirectory(Path directory) {
    	if (!exists(directory)) {
    		threx(IllegalArgumentException::new, "目录不存在：%s", directory);
		} else if (!isDirectory(directory)) {
    		threx(IllegalArgumentException::new, "指定路径上不是目录：%s", directory);
		}
    	return directory;
    }
    
    /**
     * 获取不带扩展名的文件名，例如：
     * <pre>
     * D:\\Documents\\Test.txt : Test
     * /Users/Jack/Documents/Test.txt : Test
     * </pre>
     * 
     * @param file 文件路径
     * @return 不带扩展名的文件名
     * @since 1.1.8
     */
    public static String getFileNameWithoutExt(Path file) {
    	return isDirectory(file) ? file.getFileName().toString() 
    			: isFile(file) ? getFileNameWithoutExt(file.getFileName().toString()) : null;
    }
    
    /**
     * 获取不带扩展名的文件名，例如：
     * <pre>
     * D:\\Documents\\Test.txt : Test
     * /Users/Jack/Documents/Test.txt : Test
     * </pre>
     * 
     * @param file 文件
     * @return 不带扩展名的文件名
     * @since 1.1.8
     */
    public static String getFileNameWithoutExt(File file) {
    	return isDirectory(file) ? file.getName() 
    			: isFile(file) ? getFileNameWithoutExt(file.getName()) : null;
    }
    
    /**
     * 获取不带扩展名的文件名，例如：
     * <pre>
     * Test.txt : Test
     * D:\\Documents\\Test.txt : Test
     * /Users/Jack/Documents/Test.txt : Test
     * </pre>
     * 
     * @param fileName 文件名
     * @return 不带扩展名的文件名
     * @since 1.1.8
     */
    public static String getFileNameWithoutExt(String fileName) {
    	if (isNull(fileName)) {
			return null;
		}
    	int i = fileName.lastIndexOf(FILE_SEPERATOR);
    	if (i != -1) {
			fileName = fileName.substring(i + 1);
		}
    	i = fileName.lastIndexOf(Dot);
        return i == -1 ? fileName : fileName.substring(0, i);
    }
    
    /**
     * 获取文件扩展名，例如：“.txt”。
     * 
     * @param file 文件路径
     * @return 文件扩展名
     * @since 1.1.8
     */
    public static String getFileExt(Path file) {
    	return isFile(file) ? getFileExt(file.getFileName().toString()) : null;
    }
    
    /**
     * 获取文件扩展名，例如：“.txt”。
     * 
     * @param file 文件
     * @return 文件扩展名
     * @since 1.1.8
     */
    public static String getFileExt(File file) {
    	return isFile(file) ? getFileExt(file.getName()) : null;
    }
    
    /**
     * 获取文件扩展名，例如：“.txt”。
     * 
     * @param fileName 文件名
     * @return 文件扩展名
     * @since 1.1.8
     */
    public static String getFileExt(String fileName) {
    	if (isNull(fileName)) {
			return null;
		}
    	int i = fileName.lastIndexOf(Dot);
    	if (i == -1) {
			return EMP;
		}
    	String ext = fileName.substring(i);
    	return containsAny(ext, UNIX_SEPERATOR, WINDOWS_SEPERATOR) ? EMP : ext;
    }
    
    /**
     * 获取不带"."的文件扩展名，例如：“txt”。
     * 
     * @param file 文件路径
     * @return 不带"."的文件扩展名
     * @since 1.1.8
     */
    public static String getFileExtWithoutDot(Path file) {
    	return isFile(file) ? getFileExt(file.getFileName().toString()) : null;
    }
    
    /**
     * 获取不带"."的文件扩展名，例如：“txt”。
     * 
     * @param file 文件
     * @return 不带"."的文件扩展名
     * @since 1.1.8
     */
    public static String getFileExtWithoutDot(File file) {
    	return isFile(file) ? getFileExt(file.getName()) : null;
    }
    
    /**
     * 获取不带"."的文件扩展名，例如：“txt”。
     * 
     * @param file 文件
     * @return 不带"."的文件扩展名
     * @since 1.1.8
     */
    public static String getFileExtWithoutDot(String fileName) {
    	if (isNull(fileName)) {
			return null;
		}
    	int i = fileName.lastIndexOf(Dot);
    	if (i == -1) {
			return EMP;
		}
    	String ext = fileName.substring(i + 1);
    	return containsAny(ext, UNIX_SEPERATOR, WINDOWS_SEPERATOR) ? EMP : ext;
    }
    
    public static List<File> listFile(File file) {
        return listFile(file, null);
    }
    
    public static List<File> listFile(File file, FileFilter filter) {
        return listFile(file, filter, false);
    }
    
    public static List<File> listFile(File file, FileFilter filter, boolean recurse) {
        List<File> list = newArrayList();
        File[] files = file.listFiles();
        if (files == null 
                && (filter == null 
                || filter.accept(file))) {
            list.add(file);
            return list;
        }
        for (File f : files) {
            if (recurse && f.isDirectory()) {
                list.addAll(listFile(f, filter, recurse));
            } else if (filter == null || filter.accept(f)) {
                list.add(f);
            }
        }
        return list;
    }
    
    
    public static List<Path> listFile(String path) {
        return listFile(path, null, false);
    }
    
    public static List<Path> listFile(String path, boolean recurse) {
        return listFile(path, null, recurse);
    }
    
    public static List<Path> listFile(String path, Filter<Path> filter) {
        return listFile(path, filter, false);
    }
    
    
    public static List<Path> listFile(String path, Filter<Path> filter, boolean recurse) {
        return listFile(Paths.get(path), filter, recurse);
    }
    
    
    public static List<Path> listFile(URI uri) {
        return listFile(uri, null, false);
    }
    
    
    public static List<Path> listFile(URI uri, boolean recurse) {
        return listFile(uri, null, recurse);
    }
    
    
    public static List<Path> listFile(URI uri, Filter<Path> filter) {
        return listFile(uri, filter, false);
    }
    
    
    public static List<Path> listFile(URI uri, Filter<Path> filter, boolean recurse) {
        return listFile(Paths.get(uri), filter, recurse);
    }
    
    
    public static List<Path> listFile(Path path) {
        return listFile(path, null, false);
    }
    
    
    public static List<Path> listFile(Path path, boolean recurse) {
        return listFile(path, null, recurse);
    }
    
    
    public static List<Path> listFile(Path path, Filter<Path> filter) {
        return listFile(path, filter, false);
    }
    
    
    public static List<Path> listFile(Path path, Filter<Path> filter, boolean recurse) {
        List<Path> list = newArrayList();
        if (isFile(path)) {
            try {
                if (filter == null 
                        || filter.accept(path)) {
                    list.add(path);
                }
            } catch (IOException ex) {
                silentThrex(ex);
            }
            return list;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            Iterator<Path> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Path e = (Path) iterator.next();
                if (recurse && isDirectory(e)) {
                    list.addAll(listFile(e, filter, recurse));
                } else if (filter == null || filter.accept(e)) {
                    list.add(e);
                }
            }
        } catch (IOException e) {
            silentThrex(e);
        }
        return list;
    }
    
    /**
     * 创建文件.
     * 若文件已存在或其父文件夹不存在或其不是文件而是文件夹,则抛出异常.
     * 
     * @param file 文件
     * @return Path
     */
    public static Path mkFile(String file) {
        return mkFile(Paths.get(requireNotBlank(file)));
    }
    
    /**
     * 创建文件.
     * 若文件已存在或其父文件夹不存在或其不是文件而是文件夹,则抛出异常.
     * 
     * @param file 文件
     * @return File
     */
    public static File mkFile(File file) {
        return mkFile(file.toPath()).toFile();
    }
    
    /**
     * 创建文件.
     * 若文件已存在或其父文件夹不存在或其不是文件而是文件夹,则抛出异常.
     * 
     * @param file 文件
     * @return Path
     */
    public static Path mkFile(Path file) {
        if (isDirectory(requireNotNull(file))) {
            threx(IllegalArgumentException::new, "%s不是一个文件,是一个文件夹!", file);
        }
        try {
            Files.createFile(file);
        } catch (IOException e) {
            silentThrex(e);
        }
        return file;
    }
    
    /**
     * 在指定文件夹下创建文件.
     * 若文件已存在或指定文件夹不存在,则抛出异常.
     * 
     * @param directory 文件夹
     * @param filename 文件名
     * @return Path
     */
    public static Path mkFile(String directory, String filename) {
        return mkFile(Paths.get(requireNotBlank(directory)), filename);
    }
    
    /**
     * 在指定文件夹下创建文件.
     * 若文件已存在或指定文件夹不存在,则抛出异常.
     * 
     * @param directory 文件夹
     * @param filename 文件名
     * @return File
     */
    public static File mkFile(File directory, String filename) {
        return mkFile(directory.toPath(), filename).toFile();
    }
    
    /**
     * 在指定文件夹下创建文件.
     * 若文件已存在或指定文件夹不存在,则抛出异常.
     * 
     * @param directory 文件夹
     * @param filename 文件名
     * @return Path
     */
    public static Path mkFile(Path directory, String filename) {
        if (filename.contains(separator)) {
            threx(IllegalArgumentException::new, "%s不是一个合法文件名!", filename);
        } else if (!isDirectory(directory)) {
            threx(IllegalArgumentException::new, "%s不是文件夹!", directory);
        }
        Path file = Paths.get(requireNotBlank(filename));
        file = directory.resolve(file);
        try {
            Files.createFile(file);
        } catch (IOException e) {
            silentThrex(e);
        }
        return file;
    }
    
    
    /**
     * 创建文件夹.
     * 若文件夹已存在则会抛出异常.
     * 若此文件夹的父级文件夹不存在也会抛出异常.
     * 
     * @param dir 文件夹路径
     * @return Path
     */
    public static Path mkDir(String dir) {
        return mkDir(Paths.get(requireNotBlank(dir)));
    }
    
    /**
     * 创建文件夹.
     * 若文件夹已存在则会抛出异常.
     * 若此文件夹的父级文件夹不存在也会抛出异常.
     * 
     * @param dir 文件夹
     * @return File
     */
    public static File mkDir(File dir) {
        return mkDir(dir.toPath()).toFile();
    }
    
    /**
     * 创建文件夹.
     * 若文件夹已存在则会抛出异常.
     * 若此文件夹的父级文件夹不存在也会抛出异常.
     * 
     * @param dir 文件夹
     * @return Path
     */
    public static Path mkDir(Path dir) {
        requireNotNull(dir);
        try {
            Files.createDirectory(dir);
        } catch (IOException e) {
            silentThrex(e);
        }
        return dir;
    }
    
    /**
     * 创建文件夹.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param dir 文件夹路径
     * @return Path
     */
    public static Path mkDirs(String dir) {
        return mkDir(Paths.get(requireNotBlank(dir)));
    }
    
    /**
     * 创建文件夹.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param dir 文件夹
     * @return File
     */
    public static File mkDirs(File dir) {
        return mkDir(dir.toPath()).toFile();
    }
    
    /**
     * 创建文件夹.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param dir 文件夹
     * @return Path
     */
    public static Path mkDirs(Path dir) {
        requireNotNull(dir);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            silentThrex(e);
        }
        return dir;
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child的父文件夹不是parent,则抛出异常.
     * 若子文件夹已存在,或其父文件夹不存在,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return Path
     */
    public static Path mkSubdir(String parent, String child) {
        return mkSubdir(Paths.get(requireNotBlank(parent)), child);
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child的父文件夹不是parent,则抛出异常.
     * 若子文件夹已存在,或其父文件夹不存在,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return File
     */
    public static File mkSubdir(File parent, String child) {
        return mkSubdir(parent.toPath(), child).toFile();
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child的父文件夹不是parent,则抛出异常.
     * 若子文件夹已存在,或其父文件夹不存在,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return Path
     */
    public static Path mkSubdir(Path parent, String child) {
        Path path = Paths.get(requireNotBlank(child));
        if (path.isAbsolute() && parent.equals(path.getParent())) {
            threx(IllegalArgumentException::new, "%s不是%s的父文件夹!", parent, child);
        }
        return mkDir(parent.resolve(child));
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child不包含parent的所有路径,则抛出异常.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此子文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return Path
     */
    public static Path mkSubdirs(String parent, String child) {
        return mkSubdirs(Paths.get(requireNotBlank(parent)), child);
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child不包含parent的所有路径,则抛出异常.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此子文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return File
     */
    public static File mkSubdirs(File parent, String child) {
        return mkSubdirs(parent.toPath(), child).toFile();
    }
    
    /**
     * 创建子文件夹.
     * 若child是绝对路径且child不包含parent的所有路径,则抛出异常.
     * 若其父文件夹不存在,则会创建其父文件夹,以此类推.
     * 若此子文件夹路径上的文件已存在且不是一个文件夹,则抛出异常.
     * 
     * @param parent 父文件夹
     * @param child 子文件夹
     * @return Path
     */
    public static Path mkSubdirs(Path parent, String child) {
        Path path = Paths.get(requireNotBlank(child));
        if (path.isAbsolute() && !path.startsWith(parent)) {
            threx(IllegalArgumentException::new, "%s不是%s的子目录!", child, parent);
        }
        return mkDirs(parent.resolve(child));
    }
    
    /**
     * 文件或目录复制。若出现相同文件则直接覆盖。
     * <pre>
	 * 若source与target都是文件，则直接复制，返回target；
	 * 若source是文件，target是目录，则将source复制至target目录内，返回复制后的文件；
	 * 若source与target都是目录，则将source目录整体复制到target目录内，返回复制后的目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录复制过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
     * 
     * @see #copy(Path, Path, boolean)
     * @see FileCopier#copy(boolean, boolean)
     * @param source Path
     * @param target Path
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path copy(Path source, Path target) {
    	return copy(source, target, true);
    }
    
    /**
     * 文件或目录复制。
     * <pre>
     * 若source与target都是文件，则直接复制，返回target；
     * 若source是文件，target是目录，则将source复制至target目录内，返回复制后的文件；
     * 若source与target都是目录，则将source目录整体复制到target目录内，返回复制后的目录；
     * </pre>
     * 异常情况：
     * <pre>
     * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
     * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
     * 若source是目录且target是文件时抛出{@link IIOException}；
     * 若source是目录且target也是目录，在目录复制过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
     * </pre>
     * 
     * @see FileCopier#copy(boolean, boolean)
     * @param source Path
     * @param target Path
     * @param override boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path copy(Path source, Path target, boolean override) {
    	return new FileCopier(source, target).copy(false, override);
    }
    
    /**
     * 文件复制或目录内容复制。若出现相同文件则直接覆盖。
     * <pre>
	 * 若source与target都是文件，则直接复制，返回target；
	 * 若source是文件，target是目录，则将source复制至target目录内，返回复制后的文件；
	 * 若source与target都是目录，则将source目录内所有文件及目录复制到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录复制过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
     * 
     * @see #copyContent(Path, Path, boolean)
     * @see FileCopier#copy(boolean, boolean)
     * @param source Path
     * @param target Path
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path copyContent(Path source, Path target) {
    	return copyContent(source, target, true);
    }
    
    /**
     * 文件复制或目录内容复制。
     * <pre>
	 * 若source与target都是文件，则直接复制，返回target；
	 * 若source是文件，target是目录，则将source复制至target目录内，返回复制后的文件；
	 * 若source与target都是目录，则将source目录内所有文件及目录复制到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录复制过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
     * 
     * @see FileCopier#copy(boolean, boolean)
     * @param source Path
     * @param target Path
     * @param override boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path copyContent(Path source, Path target, boolean override) {
    	return new FileCopier(source, target).copy(true, override);
    }
    
    /**
	 * 移动文件或目录。若出现相同文件则直接覆盖。
	 * <pre>
	 * 若source与target都是文件，则直接移动，返回target；
	 * 若source是文件，target是目录，则将source移动至target目录内，返回移动后的文件；
	 * 若source与target都是目录，则将source目录整体移动到target目录内，返回移动后的目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录移动过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see #move(Path, Path, boolean)
	 * @see FileCopier#move(boolean, boolean)
	 * @param source Path
     * @param target Path
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static Path move(Path source, Path target) {
    	return move(source, target, true);
    }
    
    /**
	 * 移动文件或目录。
	 * <pre>
	 * 若source与target都是文件，则直接移动，返回target；
	 * 若source是文件，target是目录，则将source移动至target目录内，返回移动后的文件；
	 * 若source与target都是目录，则将source目录整体移动到target目录内，返回移动后的目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录移动过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see FileCopier#move(boolean, boolean)
	 * @param source Path
     * @param target Path
     * @param override boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static Path move(Path source, Path target, boolean override) {
    	return new FileCopier(source, target).move(false, override);
    }
    
    /**
	 * 移动文件或目录内容。若出现相同文件则直接覆盖。
	 * <pre>
	 * 若source与target都是文件，则直接移动，返回target；
	 * 若source是文件，target是目录，则将source移动至target目录内，返回移动后的文件；
	 * 若source与target都是目录，则将source目录内所有文件及目录移动到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录移动过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see #moveContent(Path, Path, boolean)
	 * @see FileCopier#move(boolean, boolean)
	 * @param source Path
     * @param target Path
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static Path moveContent(Path source, Path target) {
    	return moveContent(source, target, true);
    }
    
    /**
	 * 移动文件或目录内容。
	 * <pre>
	 * 若source与target都是文件，则直接移动，返回target；
	 * 若source是文件，target是目录，则将source移动至target目录内，返回移动后的文件；
	 * 若source与target都是目录，则将source目录内所有文件及目录移动到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
	 * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录移动过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see FileCopier#move(boolean, boolean)
	 * @param source Path
     * @param target Path
     * @param override boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static Path moveContent(Path source, Path target, boolean override) {
    	return new FileCopier(source, target).move(true, override);
    }
    
    
    /**
     * 文件或目录复制。API说明文档请查看{@link #copy(Path, Path)}。
     * 
     * @see #copy(Path, Path)
     * @param source File
     * @param target File
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static File copy(File source, File target) {
    	return copy(source.toPath(), target.toPath()).toFile();
    }
    
    /**
     * 文件或目录复制。API说明文档请查看{@link #copy(Path, Path, boolean)}。
     * 
     * @see #copy(Path, Path, boolean)
     * @param source Path
     * @param target Path
     * @param override boolean
     * @return Path
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static File copy(File source, File target, boolean override) {
    	return copy(source.toPath(), target.toPath(), override).toFile();
    }
    
    /**
     * 文件复制或目录内容复制。API说明文档请查看{@link #copyContent(Path, Path)}。
     * 
     * @see #copyContent(Path, Path)
     * @param source File
     * @param target File
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static File copyContent(File source, File target) {
    	return copyContent(source.toPath(), target.toPath()).toFile();
    }
    
    /**
     * 文件复制或目录内容复制。API说明文档请查看{@link #copyContent(Path, Path, boolean)}。
     * 
     * @see #copyContent(Path, Path, boolean)
     * @param source File
     * @param target File
     * @param override boolean
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 复制过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static File copyContent(File source, File target, boolean override) {
    	return copyContent(source.toPath(), target.toPath(), override).toFile();
    }
    
    /**
	 * 移动文件或目录。API说明文档请查看{@link #move(Path, Path)}。
	 * 
	 * @see #move(Path, Path)
	 * @see FileCopier#move(boolean, boolean)
	 * @param source File
     * @param target File
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static File move(File source, File target) {
    	return move(source.toPath(), target.toPath()).toFile();
    }
    
    /**
	 * 移动文件或目录。API说明文档请查看{@link #move(Path, Path, boolean)}。
	 * 
	 * @see #move(Path, Path, boolean)
	 * @param source File
     * @param target File
     * @param override boolean
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static File move(File source, File target, boolean override) {
    	return move(source.toPath(), target.toPath(), override).toFile();
    }
    
    /**
	 * 移动文件或目录内容。API说明文档请查看{@link #moveContent(Path, Path)}。
	 * 
	 * @see #moveContent(Path, Path)
	 * @param source File
     * @param target File
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static File moveContent(File source, File target) {
    	return moveContent(source.toPath(), target.toPath()).toFile();
    }
    
    /**
	 * 移动文件或目录内容。API说明文档请查看{@link #moveContent(Path, Path, boolean)}。
	 * 
	 * @see #moveContent(Path, Path, boolean)
	 * @param source File
     * @param target File
     * @param override boolean
     * @return File
     * @throws NullPointerException source或target等于null时将抛出此异常
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 * @since 1.1.9
	 */
    public static File moveContent(File source, File target, boolean override) {
    	return moveContent(source.toPath(), target.toPath(), override).toFile();
    }
    
    /**
     * 重命名文件或目录，返回重命名后的文件或目录。
     * <pre>
     * 若path等于null或不存在，亦或newName等于null或空字符串则抛出{@link IllegalArgumentException}；
     * 若重命名文件或目录已存在则抛出{@link IIOException}；
     * </pre>
     * 
     * @see #rename(Path, String, boolean)
     * @param path Path 
     * @param newName String 
     * @return Path 
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.8
     */
    public static Path rename(Path path, String newName) {
    	return rename(path, newName, false);
    }
    
    /**
     * 重命名文件或目录，返回重命名后的文件或目录。
     * <pre>
     * 若path等于null或不存在，亦或newName等于null或空字符串则抛出{@link IllegalArgumentException}；
     * 当deleteExisting等于false时，若重命名文件或目录已存在则抛出{@link IIOException}；
	 * 当deleteExisting等于true，若重命名文件或目录已存在，则先将其删除再进行重命名；
     * </pre>
     * 
     * @see FileCopier#rename(boolean)
     * @param path Path 
     * @param newName String 
     * @param deleteExisting boolean 
     * @return Path 
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.8
     */
    public static Path rename(Path path, String newName, boolean deleteExisting) {
    	return new FileCopier(path, path.resolveSibling(newName)).rename(deleteExisting);
    }
    
    /**
     * 重命名文件或目录。API说明文档请查看{@link #rename(Path, String)}。
     * 
     * @see #rename(Path, String)
     * @param file File 
     * @param newName String 
     * @return File 
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.8
     */
    public static File rename(File file, String newName) {
    	return rename(file.toPath(), newName).toFile();
    }
    
    /**
     * 重命名文件或目录。API说明文档请查看{@link #rename(Path, String, boolean)}。
     * 
     * @see #rename(Path, String, boolean)
     * @param file File 
     * @param newName String 
     * @param deleteExisting boolean 
     * @return File 
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.8
     */
    public static File rename(File file, String newName, boolean deleteExisting) {
    	return rename(file.toPath(), newName, deleteExisting).toFile();
    }
    
    /**
     * 将文件或目录移动至指定目录下重命名，返回重命名后的文件或目录。
     * <pre>
     * 若source等于null或不存在，或directory等于null，亦或newName等于null或空字符串则抛出{@link IllegalArgumentException}；
     * 若重命名文件或目录已存在则抛出{@link IIOException}；
     * </pre>
     * 例如：
     * <pre>
     * rename(/root/sourcePath, /root/data, "sourceNewName") -> /root/data/sourceNewName 
     * </pre>
     * 
     * @see FileCopier#rename(boolean)
     * @param source Path
     * @param directory Path
     * @param newName String
     * @param deleteExisting boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path rename(Path source, Path directory, String newName) {
    	return rename(source, directory, newName, false);
    }
    
    /**
     * 将文件或目录移动至指定目录下重命名，返回重命名后的文件或目录。
     * <pre>
     * 若source等于null或不存在，或directory等于null，亦或newName等于null或空字符串则抛出{@link IllegalArgumentException}；
     * 当deleteExisting等于false时，若重命名文件或目录已存在则抛出{@link IIOException}；
	 * 当deleteExisting等于true，若重命名文件或目录已存在，则先将其删除再进行重命名；
     * </pre>
     * 例如：
     * <pre>
     * rename(/root/sourcePath, /root/data, "sourceNewName"...) -> /root/data/sourceNewName 
     * </pre>
     * 
     * @see FileCopier#rename(boolean)
     * @param source Path
     * @param directory Path
     * @param newName String
     * @param deleteExisting boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path rename(Path source, Path directory, String newName, boolean deleteExisting) {
    	Path target = requireNotNull(directory, "directory不能等于null").resolve(requireNotBlank(newName, "新文件名不能等于null或空字符串"));
    	return new FileCopier(source, target).rename(deleteExisting);
    }
    
    /**
     * 重命名文件或目录。
     * <pre>
     * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
     * </pre>
     * 
     * @param source Path
     * @param target Path
     * @param deleteExisting boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path rename(Path source, Path target) {
    	return rename(source, target, false);
    }
    
    /**
     * 重命名文件或目录。
     * <pre>
     * 若source或target任意一个对象等于null，或者source不存在时抛出{@link IllegalArgumentException}；
     * 当deleteExisting等于false时，若target已存在则抛出{@link IIOException}；
	 * 当deleteExisting等于true，若target已存在，则先将其删除再进行重命名；
     * </pre>
     * 
     * @param source Path
     * @param target Path
     * @param deleteExisting boolean
     * @return Path
     * @throws IllegalArgumentException 参数不合法时将抛出此异常
     * @throws IIOException 重命名过程中出现任何问题将抛出此异常
     * @since 1.1.9
     */
    public static Path rename(Path source, Path target, boolean deleteExisting) {
    	return new FileCopier(source, target).rename(deleteExisting);
    }
    
    /**
     * 删除文件或文件夹.若不存在,不做任何操作;
     * 若存在且是文件夹,则会删除此文件夹及此文件夹下的所有文件和文件夹.
     * 
     * @param file File
     */
    public static void deleteIfExists(File file) {
    	deleteIfExists(file.toPath());
    }
    
    /**
     * 删除文件或文件夹.若不存在,不做任何操作;
     * 若存在且是文件夹,则会删除此文件夹及此文件夹下的所有文件和文件夹.
     * 
     * @param path Path
     */
    public static void deleteIfExists(Path path) {
    	if (isFile(path)) {
    		delete(path);
		} else if (isDirectory(path)) {
			Stream<Path> list = null;
			try {
				list = Files.list(path);
			} catch (IOException e) {
				silentThrex(e);
			}
			Iterator<Path> iterator = list.iterator();
			while (iterator.hasNext()) {
				deleteIfExists((Path) iterator.next());
			}
			delete(path);
			list.close();
		}
    }
    
    /**
     * 删除文件.
     * 
     * @see Files#delete(Path)
     * @param path Path
     */
    public static void delete(Path path) {
    	try {
    		Files.delete(path);
    	} catch (IOException e) {
    		silentThrex(e, "delete file %s failed!", path);
    	}
    }
    
    /**
     * 获取文件夹遍历器.递归遍历此文件夹下的所有文件及文件夹.
     * 
     * @param directory Path
     * @param consumer {@code Consumer<Path>}
     */
    public static void iterate(Path directory, Consumer<Path> consumer) {
    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
    		Iterator<Path> iterator = stream.iterator();
    		while (iterator.hasNext()) {
				consumer.accept((Path) iterator.next());
			}
        } catch (IOException e) {
        	threx(IIOException::new, e);
        }
    }
    
    /**
     * 尝试将{@link URL}对象转换为{@link Path}对象。
     * 若{@link URL}对象不符合{@link URI}标准，则抛出异常。
     * 
     * @param url URL
     * @return Path
     */
    public static Path toPath(URL url) {
    	Path path = null;
    	try {
			path = Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			silentThrex(e);
		}
    	return path;
    }
    
    /**
     * 尝试将{@link Path}对象转换为{@link URL}对象。
     * 若{@link Path}对象不符合{@link URL}标准，则抛出异常。
     * 
	 * @param path Path
	 * @return URL
	 */
	public static URL toURL(Path path) {
		URL url = null;
		try {
			url = path.toUri().toURL();
		} catch (MalformedURLException e) {
			silentThrex(e);
		}
		return url;
	}
    
    
    /**************************************************IO**************************************************/
    
    
    public static IOInput load(URI uri) {
    	return load(IO.open(uri));
    }

    
    public static IOInput load(URL url) {
    	return load(IO.open(url));
    }
    
    
    public static IOInput load(InputStream in) {
    	return IO.input(in);
    }
    
    
    public static IOReader load(Reader reader) {
    	return IO.input(reader);
    }
    
    
    public static IOOutput load(OutputStream out) {
    	return IO.output(out);
    }
    
    
    public static IOWriter load(Writer writer) {
    	return IO.output(writer);
    }
    
    
    public static byte[] readByte(InputStream in) {
    	return load(in).readBytes();
    }
    
    
    public static String readString(Path path) {
    	return load(IO.openInputStream(path)).readString();
    }
    
    
    public static String readString(InputStream in) {
    	return load(in).readString();
    }
    
    
    public static String readString(Reader reader) {
    	return load(reader).read();
    }
    
    
    public static void writeAndClose(Writer writer, byte[] content) {
    	load(writer).write(content).close();
    }
    
    
    public static void writeAndClose(Writer writer, CharSequence content) {
    	load(writer).write(content).close();
    }
    
    
    public static void writeAndClose(OutputStream out, byte[] content) {
    	load(out).write(content).close();
    }
    
    
    public static void writeAndClose(OutputStream out, CharSequence content) {
    	load(out).write(content).close();
    }
    
    
    /**************************************************ClassLoader**************************************************/
    
    /**
     * 尝试关闭{@link ClassLoader}。
	 * 若指定classLoader是{@link URLClassLoader}类型，则调用{@link URLClassLoader#close}方法，否则不做任何操作。
     * 
     * @param classLoader ClassLoader
     */
    public static void closeClassLoader(ClassLoader classLoader) {
    	if (classLoader instanceof Closeable) {
			IO.close(Closeable.class.cast(classLoader));
		}
    }
    
    
    /**
     * @author zhaoqingjiang
     */
    @FunctionalInterface
    public interface ResourceLoader<T> {
        T load(URL url) throws Exception;
    }
    
    
    /**
     * @author zhaoqingjiang
     */
    public interface ResourceLoaderAdapter<T> extends ResourceLoader<Collection<T>> {

        @Override
        default Collection<T> load(URL url) throws Exception {
            String protocol = url.getProtocol();
            if (URL_Protocol_File.equals(protocol)) {
                return loadFromFile(url, Paths.get(url.toURI()));
            } else if (URL_Protocol_Jar.equals(protocol)) {
                Collection<T> c = null;
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                if (conn != null) {
                    JarFile file = conn.getJarFile();
                    if (file != null) {
                        c = loadFromJarFile(url, file);
                    }
                }
                return c;
            } else {
                return loadFromProtocol(protocol, url);
            }
        }
        
        Collection<T> loadFromFile(URL url, Path path) throws Exception;
        
        Collection<T> loadFromJarFile(URL url, JarFile file) throws Exception;
        
        default Collection<T> loadFromProtocol(String protocol, URL url) throws Exception {return null;}
    }
    
    
    /**
     * @author zhaoqingjiang
     */
    public interface JarLoader<T> {
        /**
         * Iterate JarFile when meet a JarEntry type is a file.
         * If this method return false, means break the JarFile iteration 
         * and return the resources in collector.Return true, the loop continue 
         * until all file or directory iterate-over.
         * 
         * @param url URL
         * @param file JarFile
         * @param entry JarEntry
         * @param jarPath String
         * @param packageName String
         * @param fileName String
         * @param fileExtension String
         * @param collector {@code Collection<T>}
         * @return boolean - return false will break the iteration
         * @throws Exception When Exception occurred
         */
        boolean fileEntry(URL url, JarFile file, JarEntry entry, String jarPath, 
                String packageName, String fileName, String fileExtension, Collection<T> collector) throws Exception;
    
        /**
         * Iterate JarFile when meet a JarEntry type is a directory.
         * If this method return false, means break the JarFile iteration 
         * and return the resources in collector.Return true, the loop continue 
         * until all file or directory iterate-over.
         * 
         * @param url URL
         * @param file JarFile
         * @param entry JarEntry
         * @param jarPath String
         * @param packageName String
         * @param collector {@code Collection<T>}
         * @return boolean - return false will break the iteration
         * @throws Exception When Exception occurred
         */
        default boolean directoryEntry(URL url, JarFile file, JarEntry entry, 
                String jarPath, String packageName, Collection<T> collector) throws Exception {
            return true;
        }
    }
    
    
    /**
     * @author zhaoqingjiang
     */
    public interface JarResourceLoader<T> extends ResourceLoaderAdapter<T>, JarLoader<T> {
        
        @Override
        default Collection<T> loadFromFile(URL url, Path path) throws Exception {
            return null;
        }
        
        @Override
        default Collection<T> loadFromJarFile(URL url, JarFile file) throws Exception {
            return loadJarResources(url, file, this);
        }
        
    }
    
    
    /**
     * @author zhaoqingjiang
     */
    public static class ResourceLoadException extends RuntimeException {

        private static final long serialVersionUID = -2197349031983803955L;

		protected ResourceLoadException(String message, Throwable cause) {
			super(message, cause);
		}

		protected ResourceLoadException(String message) {
			super(message);
		}

		protected ResourceLoadException(Throwable cause) {
			super(cause);
		}
    }
    
}
