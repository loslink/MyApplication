package com.itlao.utils.http;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.itlao.utils.util.StringUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;



/**
 * 默认回调接口实现
 * @author huanghuasheng
 *
 */
public class DefaultCallback implements NetBinaryCallback {
	public Handler handler;
	public DefaultCallback(Handler handler) {
		super();
		this.handler = handler;
	}

	private Map requestObject=null;
	public DefaultCallback() {
		super();
		this.requestObject=new HashMap();
	}

	public DefaultCallback(Map requestObject) {
		super();
		this.requestObject = requestObject;
	}
	/**
	 * 解压缩字符串
	 * @param bytes
	 * @return
	 */
	protected String uncompress(byte[] bytes){
		try {
			return StringUtil.uncompress(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Object execute(String response) {
		Log.i("response",response);
		Message msg=new Message();
		msg.obj=response;
		handler.sendMessage(msg);
		return null;
	}
	@Override
	public Object execute(byte[] bytes) {
		try {
			return this.execute(new String(bytes,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object onError(Map request, int errorCode, String error) {
//		if(activity!=null){
//			Toast.makeText(activity, "网络错误，请检查网络", 6000).show();
//		}
		Message msg=new Message();
		msg.arg1=errorCode;
		handler.sendMessage(msg);

		Log.i("error:",error);
		return null;
	}

	@Override
	public Object onTimeout(Map request, int statusCode) {
//		if(activity!=null){
//			Toast.makeText(activity, "连接超时,请重试.", 6000).show();
//		}
		Message msg=new Message();
		msg.arg1=statusCode;
		handler.sendMessage(msg);
		return null;
	}

	public Map getRequestObject() {
		return requestObject;
	}

	public void setRequestObject(Map requestObject) {
		this.requestObject = requestObject;
	}

}
