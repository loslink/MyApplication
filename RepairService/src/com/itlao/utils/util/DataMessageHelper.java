package com.itlao.utils.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DataMessageHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	public DataMessageHelper(Context context, String name, CursorFactory factory,
			int version) {
	
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DataMessageHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public DataMessageHelper(Context context,String name,int version){
		this(context, name,null,version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
	
		db.execSQL("create table messages(ids varchar(20),userCode varchar(25),msgs varchar(550),createTimes varchar(20),isRead varchar(5))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		System.out.println("update a Database");
	}

}
