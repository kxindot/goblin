package com.kxindot.goblin.compile;

import static com.kxindot.goblin.Classes.Package_Separator;
import static com.kxindot.goblin.Classes.Path_Separator;
import static com.kxindot.goblin.Classes.isPackaged;
import static com.kxindot.goblin.Objects.EMP;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Resources.Jar_Entry_Sep;
import static com.kxindot.goblin.Resources.Jar_Extension;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.getSimpleFileName;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.isJarFile;
import static com.kxindot.goblin.Resources.isJavaClassFile;
import static com.kxindot.goblin.Resources.listFile;
import static com.kxindot.goblin.Resources.loadJarResources;
import static com.kxindot.goblin.Resources.loadResources;
import static com.kxindot.goblin.compile.JavaDynamicClassLoader.classLoader;
import static com.kxindot.goblin.compile.JavaDynamicClassLoader.defaultClassLoader;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.tools.StandardLocation.CLASS_PATH;
import static javax.tools.StandardLocation.SOURCE_PATH;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

import com.kxindot.goblin.Resources.JarResourceLoader;

/**
 * Java动态编译文件管理器
 * @author ZhaoQingJiang
 */
public class JavaDynamicFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    
    private static DependencyFilter filter = new DependencyFilter();
    private final JavaDynamicClassLoader classLoader;
    private final Map<String, Map<String, JavaFileObject>> dependencies;

    public JavaDynamicFileManager(JavaFileManager fileManager) {
        this(fileManager, defaultClassLoader());
    }

    public JavaDynamicFileManager(JavaFileManager fileManager, ClassLoader classLoader) {
        super(fileManager);
        if (classLoader == null)
            classLoader = defaultClassLoader();
        this.classLoader = classLoader instanceof JavaDynamicClassLoader ? 
                (JavaDynamicClassLoader) classLoader : classLoader(classLoader);
        this.dependencies = newConcurrentHashMap();
    }
    
    /**
     * Add compile dependency
     * @param path Path
     */
    public void addDependency(Path path) {
        requireNotNull(path);
        if (!exists(path)) {
            throw new IllegalArgumentException(path.getFileName().toString() + " not exists!");
        } else if (isFile(path) 
                && (!isJavaClassFile(path) 
                        || !isJarFile(path))) {
            throw new IllegalArgumentException("Expect class file or jar file: " + path.getFileName().toString());
        }
        addDependencies(path);
    }
    
    /**
     * Add compile dependency
     * @param file JavaFileObject
     */
    public void addDependency(JavaFileObject file) {
        String packageName = EMP;
        String relativeName = file.getName();
        if (file instanceof JavaDynamicFile) {
            JavaDynamicFile jd = JavaDynamicFile.class.cast(file);
            packageName = jd.getPackageName();
            relativeName = jd.getSimpleClassName();
        }
        addDependency(packageName, relativeName, file);
    }
    
    /**
     * Add compile dependency
     * @param relativeName String
     * @param file JavaFileObject
     */
    public void addDependency(String relativeName, JavaFileObject file) {
        String packageName = EMP;
        if (file instanceof JavaDynamicFile) {
            packageName = JavaDynamicFile.class.cast(file).getPackageName();
        }
        addDependency(packageName, relativeName, file);
    }
    
    /**
     * Add compile dependency
     * @param packageName String
     * @param relativeName String
     * @param file JavaFileObject
     */
    public void addDependency(String packageName, String relativeName, JavaFileObject file) {
        requireNotNull(file);
        requireNotBlank(relativeName);
        Map<String, JavaFileObject> deps = getDependencies(packageName);
        JavaFileObject old = deps.get(relativeName);
        if (old != null 
                && (old.getKind() == CLASS 
                && file.getKind() == SOURCE)) {
            return;
        }
        deps.put(relativeName, file);
    }
    
    /**
     * Get dependent java file
     * @param packageName String
     * @param relativeName String
     * @return JavaFileObject
     */
    public JavaFileObject getDependency(String packageName, String relativeName) {
        if (packageName == null) {
            packageName = EMP;
        }
        requireNotBlank(relativeName);
        return getDependencies(packageName).get(relativeName);
    }
    
    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
    
    @Override
    public FileObject getFileForInput(Location location, 
            String packageName, String relativeName) throws IOException {
        FileObject file = getDependency(location, packageName, relativeName);
        if (file == null) {
            file = super.getFileForInput(location, packageName, relativeName);
        }
        return file;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, 
            String className, Kind kind, FileObject sibling) throws IOException {
        if (location == CLASS_OUTPUT) {
            JavaDynamicFile file = new JavaDynamicFile(className, kind);
            classLoader.register(file);
            return file;
        }
        return super.getJavaFileForOutput(location, className, kind, sibling);
    }
    
    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof JavaDynamicFile) {
            return JavaDynamicFile.class.cast(file).inferBinaryName();
        }
        return super.inferBinaryName(location, file);
    }
    
    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
            throws IOException {
        List<JavaFileObject> list = newArrayList(super.list(location, packageName, kinds, recurse));
        if (location == SOURCE_PATH && kinds.contains(SOURCE)) {
            list.addAll(getDependencies(location, packageName));
        } else if (location == CLASS_PATH && kinds.contains(CLASS)) {
            list.addAll(getDependencies(location, packageName));
            list.addAll(classLoader.getRegisteredFiles());
            if (isPackaged(classLoader)) {
                JarResourceLoaderImpl loader = new JarResourceLoaderImpl(packageName, CLASS);
                Collection<JavaFileObject> resources = loadResources(packageName, loader, classLoader);
                list.addAll(resources);
            }
        }
        return list;
    }
    
    /**
     * Recurse add dependency
     */
    private void addDependencies(Path path) {
        if (isDirectory(path)) {
            List<Path> paths = listFile(path, filter, true);
            paths.forEach(e -> addDependencies(e));
        } else if (isJavaClassFile(path)) {
            String className = getSimpleFileName(path.getFileName().toString());
            addDependency(new JavaDynamicFile(className, path.toUri(), CLASS));
        } else if (isJarFile(path)) {
            Collection<JavaFileObject> resources = 
                    loadJarResources(path, new JarResourceLoaderImpl(null, CLASS));
            resources.forEach(e -> addDependency(e));
        }
    }
    
    /**
     * Get dependent java file
     */
    private JavaFileObject getDependency(Location location, String packageName, String relativeName) {
        JavaFileObject file = getDependency(packageName, relativeName);
        if (file != null) {
            Kind kind = file.getKind();
            if ((location == SOURCE_PATH && kind == SOURCE)
                    || (location == CLASS_PATH && kind == CLASS)) {
                return file;
            }
        }
        return null;
    }
    
    /**
     * Get dependencies by package name
     */
    private Map<String, JavaFileObject> getDependencies(String packageName) {
        Map<String, JavaFileObject> map = dependencies.get(packageName);
        if (map == null) {
            map = newConcurrentHashMap();
            dependencies.put(packageName, map);
        }
        return map;
    }
    
    /**
     * Get dependencies by package name
     */
    private List<JavaFileObject> getDependencies(Location location, String packageName) {
        return getDependencies(packageName).values().stream().filter(e -> {
            Kind kind = e.getKind();
            return (location == SOURCE_PATH && kind == SOURCE)
                    || (location == CLASS_PATH && kind == CLASS);
        }).collect(Collectors.toList());
    }
    

    /**
     * @author zhaoqingjiang
     */
    static class DependencyFilter implements Filter<Path> {

        @Override
        public boolean accept(Path path) throws IOException {
            return isJavaClassFile(path) || isJarFile(path);
        }
    }
    
    /**
     * @author zhaoqingjiang
     */
    static class JarResourceLoaderImpl implements JarResourceLoader<JavaFileObject> {
        
        private Kind kind;
        private String packageName;
        
        JarResourceLoaderImpl(String packageName, Kind kind) {
            this.kind = kind;
            this.packageName = packageName;
        }

        @Override
        public boolean fileEntry(URL url, JarFile file, JarEntry entry, String jarPath, String packageName,
                String fileName, String fileExtension, Collection<JavaFileObject> collector) throws Exception {
            if (kind.extension.equals(fileExtension) 
                    && (this.packageName == null 
                    || this.packageName.equals(packageName))) {
                String sep = jarPath.endsWith(Jar_Extension) ? Jar_Entry_Sep : Path_Separator;
                URI uri = URI.create(String.join(sep, jarPath, entry.getName()));
                JavaDynamicFile jd = new JavaDynamicFile(String.join(Package_Separator, packageName, fileName), uri, kind);
                collector.add(jd);
            }
            return true;
        }
        
    }
    
}
