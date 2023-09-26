package com.kxindot.goblin.compile;

import static com.kxindot.goblin.Classes.Package_Separator;
import static com.kxindot.goblin.Classes.Path_Separator;
import static com.kxindot.goblin.Classes.toPathPattern;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.resource.Resources.isDirectory;
import static com.kxindot.goblin.resource.Resources.isFile;
import static com.kxindot.goblin.resource.Resources.load;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Arrays;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * @author zhaoqingjiang
 */
public class JavaDynamicFile implements JavaFileObject {
    
    private String name;
    private String simpleName;
    private String packageName;
    protected final URI uri;
    protected final Kind kind;
    private boolean immutable = true;
    private ByteArrayOutputStream stream;
    
    protected JavaDynamicFile(String className, Kind kind) {
        this(className, uri(toPathPattern(className), kind), kind);
        this.immutable = false;
    }
    
    protected JavaDynamicFile(String className, CharSequence codes, Kind kind) {
        this(className, codes.toString().getBytes(), kind);
    }
    
    protected JavaDynamicFile(String className, byte[] codes, Kind kind) {
        this(className, new ByteArrayInputStream(codes), kind);
    }
    
    protected JavaDynamicFile(String className, InputStream codes, Kind kind) {
        this(className, uri(toPathPattern(className), kind), kind);
        load(stream = new ByteArrayOutputStream()).read(codes).close();
    }
    
    protected JavaDynamicFile(URI uri, Kind kind) {
        this(getName(uri), uri, kind);
    }
    
    protected JavaDynamicFile(String className, URI uri, Kind kind) {
        this(requireNotBlank(className), getName(uri), uri, kind);
    }
    
    private JavaDynamicFile(String className, String name, URI uri, Kind kind) {
        this.uri = uri;
        this.name = name;
        this.kind = checkKind(kind);
        this.simpleName = getSimpleName(className);
        this.packageName = getPackageName(className);
    }
    
    @Override
    public URI toUri() {
        return uri;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public String getSimpleClassName() {
        return simpleName;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public String inferBinaryName() {
        return isBlank(packageName) ? simpleName 
                : String.join(Package_Separator, packageName, simpleName);
    }
    
    @Override
    public Kind getKind() {
        return kind;
    }
    
    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        String baseName = simpleName + kind.extension;
        return kind.equals(getKind())
            && (baseName.equals(getName())
                || getName().endsWith("/" + baseName));
    }
    
    @Override
    public InputStream openInputStream() throws IOException {
        return stream == null ? uri.toURL().openStream() 
                : new ByteArrayInputStream(stream.toByteArray());
    }
    
    public byte[] getByteContent() {
        return stream == null ? load(uri).readBytes() : stream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        if (immutable) {
            throw new UnsupportedOperationException();
        }
        immutable = true;
        return stream = new ByteArrayOutputStream();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        CharSequence charContent = getCharContent(ignoreEncodingErrors);
        if (charContent == null)
            throw new UnsupportedOperationException();
        if (charContent instanceof CharBuffer) {
            CharBuffer buffer = (CharBuffer) charContent;
            if (buffer.hasArray())
                return new CharArrayReader(buffer.array());
        }
        return new StringReader(charContent.toString());
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return stream == null ? load(uri).readString() : new String(stream.toByteArray());
    }
    
    @Override
    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long getLastModified() {return 0L;}

    @Override
    public boolean delete() {return false;}

    @Override
    public NestingKind getNestingKind() {return null;}

    @Override
    public Modifier getAccessLevel() {return null;}
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + toUri() + "]";
    }
    
    /**
     * Resolve file {@link Kind}
     * @param fileName
     * @return Kind
     */
    public static Kind ofKind(String fileName) {
        return Arrays.stream(Kind.values())
                .filter(e -> fileName.endsWith(e.extension))
                .findFirst().orElse(Kind.OTHER);
    }
    
    /**
     * Create URI
     */
    private static URI uri(String className, Kind kind) {
        return URI.create(addExtension(className, kind));
    }
    
    /**
     * Add extension
     */
    private static String addExtension(String name, Kind kind) {
        return name.endsWith(kind.extension) ? name : name + kind.extension;
    }
    
    /**
     * Get a name from URI
     */
    private static String getName(URI uri) {
        if (isDirectory(uri)) {
            throw new IllegalArgumentException("URI denote a directory not a file : " + uri);
        }
        String name = uri.getPath();
        if (name == null) {
            name = uri.getSchemeSpecificPart();
            requireNotBlank(name, "Invalid URI: " + uri);
        }
        if (isFile(uri)) {
            String exs = name;
            if (exs.contains(Path_Separator)) {
                exs = exs.substring(exs.lastIndexOf(Path_Separator) + 1);
            }
            if (exs.contains(Package_Separator)) {
                exs = exs.substring(exs.lastIndexOf(Package_Separator));
                if (isBlank(exs) 
                        || !SOURCE.extension.equals(exs)
                        || !CLASS.extension.equals(exs)) {
                    throw new IllegalArgumentException("Bad file exstension: " + name);
                }
            }
        }
        return name;
    }
    
    /**
     * Get simple class name
     */
    private static String getSimpleName(String className) {
        String simpleName = className;
        if (simpleName.contains(Path_Separator)) {
            simpleName = simpleName.substring(simpleName.lastIndexOf(Path_Separator) + 1);
        }
        if (simpleName.startsWith(Package_Separator)) {
            simpleName = simpleName.substring(1);
        }
        if (simpleName.contains(Package_Separator)) {
            simpleName = simpleName.substring(simpleName.lastIndexOf(Package_Separator) + 1);
        }
        return simpleName;
    }
    
    /**
     * Get package name
     */
    private static String getPackageName(String className) {
        String pkg = className;
        if (pkg.contains(Path_Separator)) {
            pkg = pkg.substring(0, pkg.lastIndexOf(Path_Separator)).replaceAll(Path_Separator, Package_Separator);
        }
        if (pkg.contains(Package_Separator)) {
            pkg = pkg.substring(0, pkg.lastIndexOf(Package_Separator));
        }
        return pkg;
    }
    
    /**
     * Check file kind
     */
    private static Kind checkKind(Kind kind) {
        if (kind != SOURCE && kind != CLASS) {
            throw new IllegalArgumentException("Only accept java source or class!");
        }
        return kind;
    }

}
