package com.itlao.utils.http.handler;

/**
 * String error = "{\"code\":\"error\",\"success\":false,\"error\":true,\"msg\":\"" + errorMsg + "\"}";
 * <br/>
 * @author hhs
 *
 */
public class RequestError {
	String code;
	boolean success;
	boolean error;
	String msg;
	
	public RequestError() {
		super();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
