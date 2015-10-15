package com.itlao.utils.http;

import java.util.Map;

/**
 * ���ض�������ݻصｿ
 * @author huanghuasheng
 *
 */
public interface NetBinaryCallback extends NetCallback{
	public Object execute(byte[] bytes);
	/**
	 * ������ｿ
	 * 	����:		
	 * public Object onError(Map request, int errorCode, String error) {
					 Message message = getVersionHandler.obtainMessage();
					  Bundle b=message.getData();
					  b.putInt("statusCode", errorCode);
					  getVersionHandler.sendMessage(message);
					return null;
				}
	 */
	@Override
	public Object onError(Map request,int errorCode,String error);
	/**
	 * ��ʱ
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
	@Override
	public Object onTimeout(Map request,int statusCode);
}
