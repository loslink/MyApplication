package com.itlao.repairservice.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class UsersMessage {

	private String username;
	private String password;
		
	    public  void setUsername(String username)
	    {
	    	this.username=username;
	    }
	    public  void setPassword(String password)
	    {
	    	this.password=password;
	    }
	    public String  getUsername()
	    {
	    	return username;
	    }
	    public  String getPassword()
	    {
	    	return password;
	    }
	

}
