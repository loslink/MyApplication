package com.itlao.utils.http.handler;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;

import com.itlao.utils.util.JsonUtil;
import com.itlao.utils.util.mappings.JSONObjectToObject;

public abstract class RequestObjectHandler<T> extends RequestHandler {


	public RequestObjectHandler(Context context) {
		super(context);
	}


	public RequestObjectHandler(Context context, Dialog tip) {
		super(context, tip);
	}


	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
	}








	@Override
	public void handleString(String response) throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void handleList(List list) throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void handleMessage(Message msg) {
		if(handleError(msg)){
			String response=(String) msg.obj;
//			Log.i("response:",response);
						try {
							Type t = this.getClass().getGenericSuperclass();
							Class clazz = JSONObjectToObject.getClazz(t, 0);
							T object = (T) JsonUtil
									.json2Object(response, clazz);
							this.handleObject(object);
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		// TODO Auto-generated method stub
		return super.sendMessageAtTime(msg, uptimeMillis);
	}

}
