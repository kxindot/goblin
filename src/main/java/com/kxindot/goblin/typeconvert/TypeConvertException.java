package com.kxindot.goblin.typeconvert;

/**
 * @author ZhaoQingJiang
 */
public class TypeConvertException extends Exception {

    private static final long serialVersionUID = 5239884956967805816L;

    private Object sourceValue;
    private Class<?> targetType;
    
    public TypeConvertException(String message) {
        super(message);
    }

    @SuppressWarnings("rawtypes")
    public TypeConvertException(TypeConverter converter, 
            Object sourceValue, Class<?> targetType, Throwable cause) {
        super(createMessage(converter, sourceValue, targetType), cause);
        this.sourceValue = sourceValue;
        this.targetType = targetType;
    }
    
    public Object getSourceValue() {
        return sourceValue;
    }

    public Class<?> getTargetType() {
        return targetType;
    }
    
    @SuppressWarnings("rawtypes")
    private static String createMessage(TypeConverter converter, 
            Object sourceValue, Class<?> targetType) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type convert error, converter ");
        sb.append(converter.getClass().getSimpleName());
        sb.append(" can't convert value ");
        sb.append(sourceValue);
        sb.append(" to ");
        sb.append(targetType.getClass());
        return sb.toString();
    }
}
