package com.itlao.utils.http;

import java.util.Map;

public interface NetCallback extends HttpConnectionCallback<Object>{
	public static final int STATUS_TIMEOUT=7;
	public static final int STATUS_ERROR=4;
	/**
	 * 锟斤拷锟斤拷锟斤拷锟?
	 * 	锟斤拷锟斤拷:
	 * public Object onError(Map request, int errorCode, String error) {
					 Message message = getVersionHandler.obtainMessage();
					  Bundle b=message.getData();
					  b.putInt("statusCode", errorCode);
					  getVersionHandler.sendMessage(message);
					return null;
				}
	 */
	public Object onError(Map<String,String> request,int errorCode,String error);
	/**
	 * 锟斤拷时
	 *
				public Object onTimeout(Map request,int statusCode) {
					 Message message = getVersionHandler.obtainMessage();
					  Bundle b=message.getData();
					  b.putInt("statusCode", statusCode);
					  Log.i("timeout","timeout>"+statusCode);
					  getVersionHandler.sendMessage(message);
					return null;
				}
	 */
	public Object onTimeout(Map<String, String> request,int statusCode);
}
