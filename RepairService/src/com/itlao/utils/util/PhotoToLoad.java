package com.itlao.utils.util;

import android.view.View;


public class PhotoToLoad {
	private String id;
	private int type;// ԭͼor����ͼ
	private String infoType;// ͼƬ����
	private int width;
	private int height;
	private View view;

	public PhotoToLoad(String id, int type, String infoType, View i, int width, int height) {
		this.id = id;
		this.type = type;
		this.infoType = infoType;
		this.view = i;
		this.width = width;
		this.height = height;		
	}
	
	public PhotoToLoad(String id, int type, String infoType, View i) {
		this.id = id;
		this.type = type;
		this.infoType = infoType;
		this.view = i;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
}
