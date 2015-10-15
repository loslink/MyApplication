package com.itlao.utils.http.handler;

import java.util.List;

import com.itlao.utils.http.NetCallback;
import com.itlao.utils.util.DialogUtil;
import com.itlao.utils.util.JsonUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;



public abstract class RequestHandler<T> extends Handler {
	private Context context;
	private Dialog tipDlg;
	private boolean openToast = true;
	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}
	public Dialog getTipDlg() {
		return tipDlg;
	}


	public void setTipDlg(Dialog tipDlg) {
		this.tipDlg = tipDlg;
	}


	public RequestHandler(Context context) {
		super();
		this.context = context;
	}


	public RequestHandler(Context context, Dialog tip) {
		super();
		this.context = context;
		this.tipDlg = tip;
	}


	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
	}

	/**
	 * handleString\
		handleObject\
		handleList\
		同时只处理一个 **/
	public abstract void handleString(String response)throws  Exception;

	public abstract void handleObject(T object)throws  Exception;

	public abstract void handleList(List<T> list)throws  Exception;

	/**
	 * 发生错误返回false
	 * @param msg
	 * @return
	 */
	protected boolean handleError(Message msg){
		if(tipDlg!=null){
			tipDlg.dismiss();
		}
		String response=(String) msg.obj;
		try {
			if(response!=null&&response.contains("code")&&response.contains("msg")&&response.contains("error")){
				RequestError error = JsonUtil.json2Object(response,
						RequestError.class);
				if(error!=null&&error.isError()){
					DialogUtil.showDialog(this.getContext(), error.getMsg(), false);
				}
			}
		} catch (Exception e) {
		}
		if(msg.arg1==NetCallback.STATUS_ERROR){
			if(openToast) Toast.makeText(context, "网络错误，请检查网络", 6000).show();
			return false;
		}else if(msg.arg1==NetCallback.STATUS_TIMEOUT){
			if(openToast) Toast.makeText(context, "连接超时,请重试.", 6000).show();
			return false;
		}else return true;
	}
	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		// TODO Auto-generated method stub
		return super.sendMessageAtTime(msg, uptimeMillis);
	}

	public void destroy(){
		if(tipDlg!=null){
			tipDlg.dismiss();
		}
	}


	/**
	 * @return the openToast
	 */
	public boolean isOpenToast() {
		return openToast;
	}


	/**
	 * @param openToast the openToast to set
	 */
	public void setOpenToast(boolean openToast) {
		this.openToast = openToast;
	}

}
