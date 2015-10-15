package com.itlao.utils.http.handler;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;

import com.itlao.utils.util.JsonUtil;
import com.itlao.utils.util.mappings.JSONObjectToObject;


public  class HttpConnetHandler<T> extends RequestHandler {


	public HttpConnetHandler(Context context) {
		super(context);
	}


	public HttpConnetHandler(Context context, Dialog tip) {
		super(context, tip);
	}


	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
	}



	@Override
	public void handleString(String response) throws  Exception{
		throw new Exception("unhanded");
	}


	@Override
	public void handleObject(Object object)throws  Exception {
		throw new Exception("unhanded");

	}


	@Override
	public void handleList(List list) throws  Exception{
		throw new Exception("unhanded");

	}


	@Override
	public void handleMessage(Message msg) {
		if(handleError(msg)){
			String response=(String) msg.obj;
			try {
				this.handleString(response);
			} catch (Exception e) {
				if("unhanded".equals(e.getMessage())){
					try {
						JSONObject jo=new JSONObject(response);
						TypeVariable[] types = this.getClass()
								.getTypeParameters();
						Type[] ts=this.getClass().getGenericInterfaces();
						Type t=this.getClass().getGenericSuperclass();
						Class clazz=JSONObjectToObject.getClazz(t, 0);
						T object=(T) JsonUtil.json2Object(response, clazz);
						this.handleObject(object);
					} catch (Exception e2) {
							List list=new ArrayList();
							try {
								Type t = this.getClass().getGenericSuperclass();
								Class clazz = JSONObjectToObject.getClazz(t, 0);
								list=JsonUtil.readList(response, clazz);
							} catch (Exception e3) {
								e3.printStackTrace();
							}
							try {
								this.handleList(list);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
					}
				}
			}
		}
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		// TODO Auto-generated method stub
		return super.sendMessageAtTime(msg, uptimeMillis);
	}

}
