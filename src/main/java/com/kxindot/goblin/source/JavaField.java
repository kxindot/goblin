package com.kxindot.goblin.source;

import static com.kxindot.goblin.Classes.getPackageName;
import static com.kxindot.goblin.Objects.decapitalize;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.toArray;

import java.util.Set;

/**
 * Java域字段模板接口
 * 
 * @author ZhaoQingJiang
 */
public class JavaField extends Source {

    private static final long serialVersionUID = -5424977955109492147L;
    private static final String Template = "%s %s %s;";
    private String pkg;
    private String className;
    private String fieldName;
    private Set<JavaModifier> modifiers;
    private Set<JavaAnnotation> annotations;
    
    
    public JavaField() {
        super();
        this.modifiers = newHashSet();
        this.annotations = newHashSet();
    }
    
    
    public String getType() {
        return isBlank(pkg) ? className 
                : joinWithDot(pkg, className);
    }
    
    
    public String getName() {
        return fieldName;
    }
    
    
    public int getModifier() {
        return modifiers.isEmpty() ? 0 
                : modifiers.stream().map(JavaModifier::value).reduce((a, b) -> a | b).get();
    }
    
    
    public JavaAnnotation[] getAnnotations() {
        return toArray(annotations, JavaAnnotation[].class);
    }
    
    
    public void setType(Class<?> type) {
        setType(type, (Class<?>) null);
    }
    
    
    public void setType(Class<?> type, Class<?>... paramterizedTypes) {
        this.pkg = getPackageName(type);
        this.className = type.getSimpleName();
        if (fieldName == null) {
            this.fieldName = decapitalize(className);
        }
        
    }
    
    
    public void setType(String className) {
        
    }
    
    
    public void setType(String packageName, String className) {
        
    }
    
    
    public void setName(String name) {
        
    }
    
    
    public void setModifier(JavaModifier... mods) {
        
    }
    
    
    public void setAnnotation(JavaAnnotation... annotations) {
        
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
