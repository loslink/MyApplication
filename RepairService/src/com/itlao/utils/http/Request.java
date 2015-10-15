package com.itlao.utils.http;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.itlao.utils.http.HttpConnectionUtil.HttpMethod;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.util.DialogUtil;

import android.app.ProgressDialog;
import android.content.Context;


public class Request {
	/**
	 *
	 * @param url
	 * @param params
	 * @param context
	 * @param requestHandler
	 * @param prompt  ��ʾ���
	 * @return
	 * @throws Exception
	 */
	public static Thread post(final String url,
			final Map<String, Object> params,RequestHandler requestHandler,String prompt) throws Exception {
		boolean postFile=false;
		for (Entry<String, Object> entry : params.entrySet()) {
			if( entry.getValue()!=null&&(entry.getValue() instanceof File)){
				postFile=true;
				break;
			}
		}
		Context context=requestHandler.getContext();
		if(prompt!=null&&prompt.trim().length()>0){
			ProgressDialog pdlg=DialogUtil.progressDlg(context, prompt);
			if(requestHandler!=null){
				requestHandler.setTipDlg(pdlg);
			}
		}
		NetCallback callback=new DefaultCallback(requestHandler){};
		if(!postFile){
			return HttpConnectionUtil.call(url, params, HttpMethod.POST, callback);
		}else{
			return HttpConnectionUtil.asyncUploadConnect(url, params, callback);
		}
	}

	public static Thread post(final String url,
			final Map<String, Object> params,NetCallback callback) throws Exception {
		{
			boolean postFile=false;
			for (Entry<String, Object> entry : params.entrySet()) {
				if( entry.getValue()!=null&&(entry.getValue() instanceof File)){
					postFile=true;
					break;
				}
			}
			if(!postFile){
				return HttpConnectionUtil.call(url, params, HttpMethod.POST, callback);
			}else{
				return HttpConnectionUtil.asyncUploadConnect(url, params, callback);
			}
		}
	}
	
	/**
	 *
	 * @param url
	 * @param params
	 * @param context
	 * @param requestHandler
	 * @param prompt  ��ʾ���
	 * @return
	 * @throws Exception
	 */
	public static Thread postFile(final String url,
			final Map<String, Object> params,RequestHandler requestHandler,String prompt) throws Exception {
		Context context=requestHandler.getContext();
		if(prompt!=null&&prompt.trim().length()>0){
			ProgressDialog pdlg=DialogUtil.progressDlg(context, prompt);
			if(requestHandler!=null){
				requestHandler.setTipDlg(pdlg);
			}
		}
		NetCallback callback=new DefaultCallback(requestHandler){};
		return HttpConnectionUtil.asyncUploadConnect(url, params, callback);
	}

}
