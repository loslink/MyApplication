package com.itlao.utils.util;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class JdbcDAOImpl {
	public DBHelper dbHelper;
	
	public JdbcDAOImpl(Context context){
		dbHelper=new DBHelper(context);
	}
	
	/**
	 * ��ｿ
	 * @param tableName
	 * @param insertMaps
	 */
	public void insert(String tableName,Map<String,String> insertMaps){
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues(); 
		for(String key:insertMaps.keySet()){
			String value=insertMaps.get(key);
			cv.put(key, value);
		}
		
		db.insert(tableName, null, cv);
		
		db.close();
		
	}
	
	/**
	 * �޸Ĳ���
	 * @param tableName
	 * @param updateMaps
	 * @param where
	 * @param whereArgs
	 */
	public void update(String tableName,Map<String,String> updateMaps,
			String where,String whereArgs[]){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues(); 
		for(String key:updateMaps.keySet()){
			String value=updateMaps.get(key);
			cv.put(key, value);
		}
		
		db.update(tableName, cv, where, whereArgs);
		
		db.close();
	}
	
	/**
	 * ɾ����������
	 * @param tableName
	 */
	public void deleteAll(String tableName,String where,String whereArgs[]){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(tableName, where, whereArgs);
		db.close();
	}
	
	/**
	 * ��ѯ��ݼ�¼�ｿ
	 * @param tableName
	 * @return
	 */
	public int getCount(String tableName,String where,String whereArgs[] ){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor c=db.query(tableName, new String[]{"COUNT(*)"}, 
				where, whereArgs, null, null, null, null);
		
		c.moveToNext();
		
		int count=c.getInt(0);
		c.close();
		db.close();
		return count;
	}
}
