/**
 * 
 */
package com.itlao.utils.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper
{
	private final static int VERSION=1;
	public DBHelper(Context context)
	{
		super(context, "train_staff.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		System.out.println("--------create --------" );
		db.execSQL("drop table IF EXISTS TN_ANSWER");
		String userExamSQL="create table TN_ANSWER(" +
		           " QUE_ID         varchar(50)," +
		           " ORDERNO        int," +
		           " CORRECT_ANSWER varchar(20)," +
		           " USER_ANSWER    varchar(20)" +
		           ")";
		db.execSQL(userExamSQL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.i("--------onUpdate Called--------","" 
				+ oldVersion + "--->" + newVersion);
		if (oldVersion < VERSION) {
			db.execSQL("drop table IF EXISTS TN_ANSWER");
			String userExamSQL="create table TN_ANSWER(" +
			           " QUE_ID         varchar(50)," +
			           " ORDERNO        int," +
			           " CORRECT_ANSWER varchar(20)," +
			           " USER_ANSWER    varchar(20)" +
			           ")";
			db.execSQL(userExamSQL);
		}
	}
}
