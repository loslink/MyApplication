package com.itlao.utils.http.handler;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;

public abstract class RequestStringHandler<T> extends RequestHandler {


	public RequestStringHandler(Context context) {
		super(context);
	}


	public RequestStringHandler(Context context, Dialog tip) {
		super(context, tip);
	}


	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
	}





	@Override
	public void handleObject(Object object)throws  Exception {
		throw new Exception("unhanded");

	}




	@Override
	public void handleList(List list) throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void handleMessage(Message msg) {
		if(handleError(msg)){
			String response=(String) msg.obj;
			try {
				this.handleString(response);
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
