package com.kxindot.goblin.web;

import java.io.Serializable;

/**
 * @author ZhaoQingJiang
 */
public interface Code extends Serializable {

    /**
     * 获取响应码
     * @return String
     */
    String getCode();
    
    /**
     * 获取响应码信息
     * @return String
     */
    String getMessage();
}
