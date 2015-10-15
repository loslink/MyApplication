package com.example.utils.recordvoice.util;

public abstract interface ICallback {
	
	public static final int ERROR = 0;
	
	public abstract void onSuccess(Object... paramArrayOfObject);

	public abstract void onError(int paramInt, String paramString);

	public abstract void onProgress(int paramInt);
}