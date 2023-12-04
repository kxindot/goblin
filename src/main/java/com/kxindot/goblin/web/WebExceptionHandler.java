package com.kxindot.goblin.web;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.kxindot.goblin.Objects;
import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;

/**
 * Web-API通用异常处理器
 * 
 * @author ZhaoQingJiang
 */
@Order(0)
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)  
public abstract class WebExceptionHandler<R> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Web异常处理.
	 * 
	 * @param ex WebException
	 * @return Response
	 */
	@ExceptionHandler(WebException.class)
	public R webException(WebException ex) {
		String message = ex.getMessage();
		logger.error(message, ex);
		return response(ex.getCode(), message, ex, getStackMessage(ex));
	}
	
	/**
	 * 是否开启异常堆栈信息输出,
	 * 若开启{@link #response(Code, String, Throwable, String)}方法最后一个入参将输入异常堆栈信息
	 * ,反之则总是输入null.
	 * 
	 * @return boolean
	 */
	protected abstract boolean enableStackMessage();
	
	/**
	 * 生成响应.
	 * 
	 * @param code 响应码
	 * @param errMsg 错误信息
	 * @param ex Throwable
	 * @param stackMessage 异常堆栈信息
	 * @return R
	 */
	protected abstract R response(Code code, String errMsg, Throwable ex, String stackMessage);
	
	/**
	 * 不合法参数异常处理<br>
	 * <pre>
	 * 1. 请求参数为空或请求参数不可解析
	 * 2. 参数绑定及参数校验异常
	 * </pre>
	 * 
	 * @param ex Exception
	 * @return ResponseEntity
	 */
    @ExceptionHandler({HttpMessageNotReadableException.class, 
    	MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, 
    	MissingServletRequestPartException.class, MissingServletRequestParameterException.class})  
    public R invalidArgument(Exception ex) {
		String errMsg = null;
		if (ex instanceof MethodArgumentNotValidException) {
			errMsg = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage();
		}
		if (Objects.isBlank(errMsg)) {
			errMsg = ex.getMessage();
		}
		log(ex, "请求参数异常: {}", errMsg);
		return response(ErrCode.Invalid_Argument, errMsg, ex, getStackMessage(ex));
    }
    
    /**
	 * 请求资源不存在异常处理.
	 * 
     * @param ex NoHandlerFoundException
     * @return R
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R apiNotFound(NoHandlerFoundException ex) {
    	return response(ErrCode.Not_Found, ex.getMessage(), ex, getStackMessage(ex));
    }
    
	/**
	 * 不合法的HTTP方法.
	 * 
	 * @param ex HttpRequestMethodNotSupportedException
	 * @return R
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R invalidRequest(HttpRequestMethodNotSupportedException ex) {
		String errMsg = "不支持的HTTP方法: " + ((HttpRequestMethodNotSupportedException) ex).getMethod();
		log(ex, "请求异常: {}", errMsg);
		return response(ErrCode.Method_Not_Allowed, errMsg, ex, getStackMessage(ex));
	}
	
	/**
	 * 不支持的媒体格式.
	 * 
	 * @param ex HttpMediaTypeNotSupportedException
	 * @return R
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public R invalidRequest(HttpMediaTypeNotSupportedException ex) {
		String errMsg = "不支持的媒体类型: " + ((HttpMediaTypeNotSupportedException) ex).getContentType();
		log(ex, "请求异常: {}", errMsg);
		return response(ErrCode.Unsupported_Media_Type, errMsg, ex, getStackMessage(ex));
	}
	
	/**
	 * 服务器未知异常处理.
	 * 
	 * @param ex Exception
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	public R failure(Exception ex) {
		String errMsg = ex.getMessage();
		logger.error("服务器未知异常: {}", errMsg, ex);
		return response(ErrCode.System_Error, errMsg, ex, getStackMessage(ex));
	}
	
	/**
	 * 获取异常堆栈信息
	 */
	private String getStackMessage(Throwable ex) {
		return enableStackMessage() ? Objects.toString(ex) : null;
	}
	
	/**
	 * 日志打印
	 */
	private void log(Throwable ex, String message, Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, args, ex);
		} else {
			logger.info(message, args);
		}
	}
	
}
