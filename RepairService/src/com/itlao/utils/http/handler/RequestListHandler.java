package com.itlao.utils.http.handler;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;

import com.itlao.utils.util.JsonUtil;
import com.itlao.utils.util.mappings.JSONObjectToObject;

public abstract  class RequestListHandler<T> extends RequestHandler {


	public RequestListHandler(Context context) {
		super(context);
	}


	public RequestListHandler(Context context, Dialog tip) {
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
	public void handleMessage(Message msg) {
		if (handleError(msg)) {
			String response=(String) msg.obj;
//			List list = new ArrayList();
			List list = null;
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

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		// TODO Auto-generated method stub
		return super.sendMessageAtTime(msg, uptimeMillis);
	}

}
