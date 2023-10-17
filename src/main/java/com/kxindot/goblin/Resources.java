package com.kxindot.goblin;

import static com.kxindot.goblin.Classes.Package_Separator;
import static com.kxindot.goblin.Classes.Path_Separator;
import static com.kxindot.goblin.Classes.getAvailableClassLoader;
import static com.kxindot.goblin.Classes.toPackagePattern;
import static com.kxindot.goblin.Classes.toPathPattern;
import static com.kxindot.goblin.Objects.Colon;
import static com.kxindot.goblin.Objects.Dot;
import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.Exclamation;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;
import static java.io.File.separator;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;

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
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.kxindot.goblin.exception.RuntimeException;
import com.kxindot.goblin.io.IO;
import com.kxindot.goblin.io.IOInput;
import com.kxindot.goblin.io.IOOutput;
import com.kxindot.goblin.io.IOReader;
import com.kxindot.goblin.io.IOWriter;

/**
 * 
 * 
 * @author ZhaoQingJiang
 */
public class Resources {
    
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
            throw new ResourceLoadException(e, 
                    "Error occurred when loading resources from URL : %s", url);
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
            throw new ResourceLoadException(e, 
                    "Error occurred when loading resources from jar : %s", url);
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
     * Parse and get a file simple name.<br>
     * e.g: input "/Users/Jack/Documents/Test.txt", then output "Test".
     * @param fileName String
     * @return String
     */
    public static String getSimpleFileName(String fileName) {
        if (fileName.contains(File.separator)) {
            fileName = fileName.substring(fileName.lastIndexOf(Path_Separator) + 1);
        }
        if (fileName.contains(Dot)) {
            fileName = fileName.substring(0, fileName.lastIndexOf(Dot));
        }
        return fileName;
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
    	return load(IO.open(path)).readString();
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

        public ResourceLoadException(Throwable cause, String message, Object... args) {
            super(cause, message, args);
        }

        public ResourceLoadException(Throwable cause, String message) {
            super(cause, message);
        }

        public ResourceLoadException(Throwable cause) {
            super(cause);
        }
    }
    
}
