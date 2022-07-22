package com.kxindot.goblin.logger;

import static com.kxindot.goblin.Objects.stringFormat;

import com.kxindot.goblin.EnumValue;

/**
 * 日志级别枚举类型
 * @author ZhaoQingJiang
 */
public enum Level implements EnumValue<Integer> {

    ERROR(40, "ERROR"), 
    WARN(30, "WARN"), 
    INFO(20, "INFO"), 
    DEBUG(10, "DEBUG"), 
    TRACE(0, "TRACE");
    
    private Integer value;
    private String keyword;
    
    private Level(Integer value, String keyword) {
        this.value = value;
        this.keyword = keyword;
    }
    
    @Override
    public Integer value() {
        return value;
    }
    
    public String keyword() {
        return keyword;
    }
    
    @Override
    public String toString() {
        return stringFormat("%s[%s|%s]", getClass().getSimpleName(), keyword, value);
    }
}
