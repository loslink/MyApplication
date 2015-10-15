/**
 * @fileName SharedPreferencesForLogin.java[v1.0]
 * @classes com.eshine.mclient.util.SharedPreferencesForLogin
 * @author qiushuzhong 
 */
package com.itlao.utils.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * <p>-------------------------------------------------------------------</p>
 * <p>Date              Mender              Reason</p>
 * <p>2012-7-9         qiushuzhong            add</p>
 */
public class SharedPreferencesUtil {
	public static String STR_XMLFILE = "logdata";

	public static String STR_USERCODE = "USERCODE";
	public static String STR_USERNAME = "USERNAME";
	public static String STR_PASSWORD = "PASSWORD";
	public static String STR_LOGIN_STATE = "LOGINSTATE";
	
	public static String STATE_LOGIN = "LOGIN";
	public static String STATE_OUT = "LOGOUT";
	
	public static final String HAS_CLAIM = "HASCLAIM";
	
	public static final String CARRY_MACHINE = "CARRY_MACHINE";//Я���ͻ�
	public static final String CARRY_ON = "1";//Я���ͻ�
	public static final String CARRY_OFF = "0";//��Я���ͻ�
	
	public static final String GEN_ID = "GENID";
	public static final String GEN_STATE = "GENSTATE";
	
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Context context;

	public SharedPreferencesUtil(Context c, String name) {
		context = c;
		sp = context.getSharedPreferences(name, 0);
		editor = sp.edit();
	}
	  /**

	   * <p>----------------------------------------------------------------</p>
	   * <p>Date              Mender              Reason</p>
	   * <p>2012-7-9           qiushuzhong            </p>
	   */
	  public void putValue(String key, String value){
	   editor = sp.edit();
	   editor.putString(key, value);
	 
	   editor.commit();
	  }
	  /**
	 
	   * <p>----------------------------------------------------------------</p>
	   * <p>Date              Mender              Reason</p>
	   * <p>2012-7-9           qiushuzhong            </p>
	   */
	  public String getValue(String key){
	   return sp.getString(key, null);
	  }
	  
	  /**
	   * @since 1.1
	   * <p>----------------------------------------------------------------</p>
	   * <p>Date              Mender              Reason</p>
	   * <p>2012-7-9           qiushuzhong            </p>
	   */
	  public void clear(){
	   editor = sp.edit();
	   editor.clear();
	   editor.commit();
	  }

}
