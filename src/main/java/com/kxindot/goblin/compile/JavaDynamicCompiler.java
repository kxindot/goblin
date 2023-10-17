package com.kxindot.goblin.compile;

import static com.kxindot.goblin.Classes.Package_Separator;
import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.newHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Resources.isJavaSourceFile;
import static com.kxindot.goblin.Resources.listFile;
import static javax.tools.JavaFileObject.Kind.SOURCE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import com.kxindot.goblin.Resources;

import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

/**
 * 
 * @author zhaoqingjiang
 */
public interface JavaDynamicCompiler {
    
    /**
     * Create JavaDynamicCompiler
     * @return JavaDynamicCompiler
     */
    public static JavaDynamicCompiler compiler() {return compiler(null);}
    
    /**
     * Create JavaDynamicCompiler
     * @return JavaDynamicCompiler
     */
    public static JavaDynamicCompiler compiler(List<String> options) {return new JavaDynamicCompilerImpl(options);}

    
    /**
     * Add Java source file or file directory to compiler
     * @param source File
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(File source);
    
    /**
     * Add Java source file or file directory to compiler
     * @param source Path
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(Path source);
    
    /**
     * Add Java source to compiler
     * @param className String
     * @param sourceCode String
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(String className, String sourceCode);
    
    /**
     * Add Java source to compiler
     * @param packageName String
     * @param className String
     * @param sourceCode String
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(String packageName, String className, String sourceCode);
    
    /**
     * Add Java source to compiler<br>.
     * key is className, and value is source code.
     * @param sources {@code Map<String, String>}
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(Map<String, String> sources);
    
    /**
     * Add Java source to compiler<br>.
     * key is className, and value is source code.
     * @param packageName String
     * @param sources {@code Map<String, String>}
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addSource(String packageName, Map<String, String> sources);
    
    /**
     * Add dependent class file or file directory to compiler
     * @param path String
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addDependency(String path);
    
    /**
     * Add dependent class file or file directory to compiler
     * @param dependency File
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addDependency(File dependency);
    
    /**
     * Add dependent class file or file directory to compiler
     * @param dependency Path
     * @return JavaDynamicCompiler
     */
    JavaDynamicCompiler addDependency(Path dependency);
    
    /**
     * Dynamic compile Java source to byte-code<br>
     * Return compile result, key is className, value is compiled class
     * @return {@code Map<String, Class<?>>}
     */
    Map<String, Class<?>> compile();
    
    
    
    
    /**
     * @author zhaoqingjiang
     */
    static class JavaDynamicCompilerImpl implements JavaDynamicCompiler {
        
        private List<String> ops; 
        private JavaCompiler compiler;
        private JavaDynamicFileManager manager;
        private Map<String, JavaFileObject> sources;
        private DiagnosticListener<JavaFileObject> collector; 
        
        private JavaDynamicCompilerImpl(List<String> ops) {
            if (isNotEmpty(ops)) this.ops = ops;
            sources = newHashMap();
            collector = new DiagnosticCollector<>();
            compiler = ToolProvider.getSystemJavaCompiler();
            manager = new JavaDynamicFileManager(compiler.getStandardFileManager(collector, null, null));
        }
        
        @Override
        public JavaDynamicCompiler addSource(File source) {
            requireNotNull(source);
            if (!isJavaSourceFile(source)) {
                throw new IllegalArgumentException("Input source file is not a java source file : " + source.getName());
            }
            List<File> sources = listFile(source, Resources::isJavaSourceFile, true);
            if (isEmpty(sources)) {
                throw new IllegalArgumentException("Input source path not contains any java source file : " + source.getName());
            }
            sources.forEach(s -> {
                String name = s.getName();
                name = name.substring(0, name.lastIndexOf(SOURCE.extension));
                JavaFileObject file = new JavaDynamicFile(name, s.toURI(), SOURCE);
                this.sources.put(name, file);
            });
            return this;
        }

        @Override
        public JavaDynamicCompiler addSource(Path source) {
            return addSource(source.toFile());
        }

        @Override
        public JavaDynamicCompiler addSource(String className, String sourceCode) {
            return addSource(null, className, sourceCode);
        }

        @Override
        public JavaDynamicCompiler addSource(String packageName, String className, String sourceCode) {
            requireNotBlank(className);
            requireNotBlank(sourceCode);
            if (isNotBlank(packageName)) {
                className = String.join(Package_Separator, packageName, className);
            }
            JavaDynamicFile file = new JavaDynamicFile(className, sourceCode, SOURCE);
            this.sources.put(className, file);
            return this;
        }

        @Override
        public JavaDynamicCompiler addSource(Map<String, String> sources) {
            sources.forEach((k, v) -> addSource(k, v));
            return this;
        }

        @Override
        public JavaDynamicCompiler addSource(String packageName, Map<String, String> sources) {
            sources.forEach((k, v) -> addSource(packageName, k, v));
            return this;
        }

        @Override
        public JavaDynamicCompiler addDependency(String path) {
            return addDependency(Paths.get(path));
        }

        @Override
        public JavaDynamicCompiler addDependency(File dependency) {
            return addDependency(dependency.toPath());
        }

        @Override
        public JavaDynamicCompiler addDependency(Path dependency) {
            manager.addDependency(dependency);
            return this;
        }

        @Override
        public Map<String, Class<?>> compile() {
            if (isEmpty(sources)) {
                throw new JavaDynamicCompileException("No java source to be compile, please add some ~~");
            }
            try {
                CompilationTask task = compiler.getTask(null, manager, collector, ops, null, sources.values());
                if (!task.call()) {
                    throw new JavaDynamicCompileException(getDiagnosticMessage());
                }
            } finally {
                try {
                    manager.close();
                } catch (IOException e) {
                    throw new JavaDynamicCompileException(e, "Close JavaFileManager meet some problem");
                }
            }
            Map<String, Class<?>> map = newHashMap();
            try {
                for (String key : sources.keySet()) {
                    Class<?> cls = JavaDynamicClassLoader.defaultClassLoader().loadClass(key);
                    map.put(key, cls);
                }
            } catch (ClassNotFoundException e) {
                // 以防万一
                throw new JavaDynamicCompileException(e);
            }
            return map;
        }

        @SuppressWarnings("unchecked")
        private String getDiagnosticMessage() {
            List<Diagnostic<JavaFileObject>> list = DiagnosticCollector.class.cast(collector).getDiagnostics();
            if (isEmpty(list)) return null;
            StringBuilder builder = new StringBuilder();
            for (Diagnostic<JavaFileObject> diagnostic : list) {
                JavaFileObject source = diagnostic.getSource();
                if (source != null) {
                    builder.append(String.format("Class : %s\n", source.getName()));
                }
                builder.append(String.format("Error : %s\n\n", diagnostic.getMessage(null)));
            }
            return builder.toString();
        }
        
    }
    
    
}
