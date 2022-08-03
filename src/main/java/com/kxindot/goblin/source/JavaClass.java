package com.kxindot.goblin.source;

import java.util.Collection;

public interface JavaClass {

    
    public static JavaClass newClass(String className) {
        
        return null;
    }
    
    
    public static JavaClass newClass(String packageName, String className) {
        
        return null;
    }
    
    
    
    
    
    void extend(Class<?> parent);
    
    
    void implement(Class<?> interfaze);
    
    
    void implementAll(Class<?>... interfazes);
    
    
    void setField(JavaField field);
    
    
    void setFields(JavaField... fields);
    
    
    void setFields(Collection<JavaField> fields);
    
    
    void setMethod(JavaMethod method);
    
    
    void setMethods(JavaMethod... methods);
    
    
    void setMethods(Collection<JavaMethod> methods);
    
    
    void setAnnotation(JavaAnnotation annotation);
    
    
    void setAnnotations(JavaAnnotation... annotations);
    
    
    void setAnnotations(Collection<JavaAnnotation> annotations);
}
