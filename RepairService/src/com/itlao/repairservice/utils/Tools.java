package com.itlao.repairservice.utils;

import android.os.Environment;
/**
 * 
 * @author 
 * Create at 2012-8-17 上午10:14:40
 */
public class Tools {
	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
}
