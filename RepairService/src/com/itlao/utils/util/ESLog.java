package com.itlao.utils.util;

import android.util.Log;

public class ESLog {
	private String className;
	
	public ESLog(String className) {
		super();
		this.className = className;
	}

	public void error(String err){
		Log.e(className, err);
	}
	public void error(String tag,Throwable t){
		Log.e(tag, t.getMessage());
	}
}
