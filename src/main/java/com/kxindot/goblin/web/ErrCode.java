package com.kxindot.goblin.web;

import static com.kxindot.goblin.Objects.isAnyBlank;
import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isEqual;
import static com.kxindot.goblin.Objects.isNotBlank;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newConcurrentHashMap;
import static com.kxindot.goblin.Throws.threx;

import java.util.Map;

/**
 * @author ZhaoQingJiang
 */
public class ErrCode implements Code {
    
    private static final long serialVersionUID = 3294481963945460194L;
    
    /** 请求成功 */
    public static final Code OK = of("200", "OK");
    /** 不合法的请求参数 */
    public static final Code Invalid_Argument = of("400", "不合法的请求参数");
    /** 用户未授权,禁止访问 */
    public static final Code Unauthorized = of("401", "用户未授权,禁止访问!");
    /** 禁止访问 */
    public static final Code Forbidden = of("403", "禁止访问!");
    /** 请求资源不存在! */
    public static final Code Not_Found = of("404", "请求资源不存在!");
    /** 不合法的请求方法! */
    public static final Code Method_Not_Allowed = of("405", "不合法的请求方法!");
    /** 不支持的媒体格式! */
    public static final Code Unsupported_Media_Type = of("415", "不支持的媒体格式!");
    /** 服务器未知异常 */
    public static final Code System_Error = of("500", "服务器繁忙,请稍后重试!");

    
    /** Code缓存集合 */
    private static Map<String, Code> CODES;
    
    /**
     * 获取Code对象
     * @param code String
     * @return Code
     */
    public static Code valueOf(String code) {
        return isBlank(code) ? null : CODES.get(code);
    }
    
    /**
     * 获取Code对象
     * @param code String
     * @param message String
     * @return Code
     */
    public static Code valueOf(String code, String message) {
        Code errCode = null;
        if (isNotBlank(code)) {
            errCode = CODES.get(code);
            if (errCode != null
                    && isNotBlank(message)
                    && !isEqual(message, errCode.getMessage())) {
                errCode = new ErrCode();
                ((ErrCode) errCode).setCode(code);
                ((ErrCode) errCode).setMessage(message);
            }
        }
        return errCode;
    }
    
    /**
     * 生成Code(响应码)实例
     * @param code String
     * @param message String
     * @return Code
     */
    protected static Code of(String code, String message) {
        if (isAnyBlank(code, message)) {
        	threx(IllegalArgumentException::new, "响应码或响应码信息不能为null或空字符串!");
        }
        if (isNull(CODES)) {
            CODES = newConcurrentHashMap();
        }
        if (CODES.containsKey(code)) {
        	threx(IllegalArgumentException::new, "响应码%s已存在, 不能重复定义!", code);
        }
        ErrCode errCode = new ErrCode();
        errCode.setCode(code);
        errCode.setMessage(message);
        CODES.put(code, errCode);
        return errCode;
    }
    
    
    /** 响应码 */
    private String code;
    /** 响应码信息 */
    private String message;
    
    protected ErrCode() {}
    
    public ErrCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    void setCode(String code) {
        this.code = code;
    }

    void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (null != obj 
                && obj instanceof Code) {
            return isEqual(this.code, ((Code) obj).getCode());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.join("|", code, message);
    }
}
