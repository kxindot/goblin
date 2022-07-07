package com.kxindot.goblin.compile;

import static com.kxindot.goblin.Classes.getAvailableClassLoader;
import static com.kxindot.goblin.IO.newIORuntimeException;
import static com.kxindot.goblin.IO.readBytes;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Objects.requireNotNull;
import static javax.tools.JavaFileObject.Kind.CLASS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.tools.JavaFileObject;

/**
 * @author zhaoqingjiang
 */
public class JavaDynamicClassLoader extends ClassLoader {

    private static Map<String, JavaFileObject> classes = newConcurrentHashMap();
    private static JavaDynamicClassLoader classLoader = new JavaDynamicClassLoader();
    
    /**
     * Return default JavaDynamicClassLoader
     * @return JavaDynamicClassLoader
     */
    public static JavaDynamicClassLoader defaultClassLoader() {return classLoader;}
    
    /**
     * Create JavaDynamicClassLoader
     * @param classLoader parent ClassLoader
     * @return JavaDynamicClassLoader
     */
    public static JavaDynamicClassLoader classLoader(ClassLoader classLoader) {
        requireNotNull(classLoader, "classLoader == null");
        return new JavaDynamicClassLoader(classLoader);
    }
    
    
    private JavaDynamicClassLoader() {
        super(getAvailableClassLoader());
    }

    private JavaDynamicClassLoader(ClassLoader parent) {
        super(parent);
    }
    
    /**
     * Return all registered class files
     */
    public Collection<JavaFileObject> getRegisteredFiles() {
        return newArrayList(classes.values());
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = findClassBytes(name);
        return bytes == null ? super.findClass(name) 
                : defineClass(name, bytes, 0, bytes.length);
    }
    
    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(CLASS.extension)) {
            name = name.substring(0, name.lastIndexOf(CLASS.extension));
            byte[] bytes = findClassBytes(name);
            if (bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
        }
        return super.getResourceAsStream(name);
    }
    
    /**
     * register class file to JavaDynamicClassLoader
     */
    protected void register(JavaFileObject file) {
        if (CLASS != file.getKind()) {
            throw new IllegalArgumentException("Invalid Java File Kind : " + file.getKind());
        }
        String name = file.getName();
        if (name.endsWith(CLASS.extension)) {
            name = name.substring(0, name.lastIndexOf(CLASS.extension));
        }
        classes.put(name, file);
    }
    
    /**
     * find registered class byte-code
     */
    private byte[] findClassBytes(String name) {
        byte[] bytes = null;
        if (classes.containsKey(name)) {
            JavaFileObject file = classes.get(name);
            if (file instanceof JavaDynamicFile) {
                bytes = JavaDynamicFile.class.cast(file).getByteContent();
            } else {
                try (InputStream in = file.openInputStream()) {
                    bytes = readBytes(in);
                } catch (IOException e) {
                    throw newIORuntimeException(e);
                }
            }
        }
        return bytes;
    }
    
}
