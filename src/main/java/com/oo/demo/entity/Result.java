package com.oo.demo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *   接口返回数据格式
 * @author scott
 * @email jeecgos@163.com
 * @date  2019年1月19日
 */
@Data
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	private String message = "";

	/**
	 * 返回代码
	 */
	private Integer code = 200;
	
	/**
	 * 返回数据对象 data
	 */
	private T result;
	
	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	public Result() {
	}

    /**
     * 兼容VUE3版token失效不跳转登录页面
     * @param code
     * @param message
     */
	public Result(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Result<T> success(String message) {
		this.message = message;
		this.code = 200;
		this.success = true;
		return this;
	}



	public static<T> Result<T> OK() {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(200);
		return r;
	}


	public static<T> Result<T> OK(String msg) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(200);
		r.setMessage(msg);
		//Result OK(String msg)方法会造成兼容性问题 issues/I4IP3D
		r.setResult((T) msg);
		return r;
	}

	public static<T> Result<T> OK(T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(200);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> OK(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(200);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> error(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(false);
		r.setCode(500);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> error(String msg) {
		return error(500, msg);
	}


	public static<T> Result<T> error(int code, String msg) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}


}