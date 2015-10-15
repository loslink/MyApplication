package com.itlao.Database;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;  
    private SQLiteDatabase db; 
    private String id="965918077";
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();
       
        //byte[] b_user = null;
    }  
     
    public SQLiteDatabase getDatabase() {  
        
        return db;
    }  
    /** 
     * 保存myself 信息 
     * @param
     */  
    public void updateMyself(String name ,int age,String idcard ,String sex ,boolean isSavePassword ,String id ,String password ,String nickname ,String address ,String head_sculpture ,String phone_no ,String qq ,String email ,boolean is_professor ,int total_score ,int profession ,String expertcontent ) {  
        db.beginTransaction();  //开始事务  
        try {  
                db.execSQL("INSERT INTO myself VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", new Object[]{ name , age, idcard , sex , isSavePassword , id , password , nickname , address , head_sculpture , phone_no , qq , email , is_professor , total_score , profession , expertcontent});  
             
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    
    /** 
     * add persons 
     * @param users 
     */  
    public void add(List<User> users) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (User user : users) {  
                db.execSQL("INSERT INTO user VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", new Object[]{user.name, user.age,user.sex, user.info,user.id,user.password,user.nickname,user.address,user.head_sculpture,user.phone_no,user.qq,user.email,user.is_professor,user.total_score,user.profession,user.idcard,user.expertcontent});  
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    /** 
     * 添加信息 
     * @param
     */  
    public void addMessages(List<Messages> messages) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (Messages msg : messages) {  
                db.execSQL("INSERT INTO messages VALUES(null, ?, ?)", new Object[]{msg.time, msg.content}); 
                
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    /** 
     * update person's age 
     * @param user 
     */  
    public void updateAge(User user) {
        ContentValues cv = new ContentValues();
        cv.put("name", user.name);
        cv.put("age", user.age);
        db.update("user", cv, "id = ?", new String[]{user.id});
    }
    
    /** 
     * 修改age 
     * @param
     */  
    public void alterAge(int age) {
        ContentValues cv = new ContentValues();
        cv.put("age", age);
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改擅长内容 
     * @param
     */  
    public void alterExpertcontent(String expertcontent) {
        ContentValues cv = new ContentValues();
        cv.put("expertcontent", expertcontent); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改sex 
     * @param
     */  
    public void alterSex(String sex) {
        ContentValues cv = new ContentValues();
        cv.put("sex", sex); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改师傅类型
     * @param
     */  
    public void alterShifuClass(int i_profession) {
        ContentValues cv = new ContentValues();
        cv.put("profession", i_profession); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改是否储存密码
     * @param
     */  
    public void alterisSavePwd(boolean isSavePassword) {
        ContentValues cv = new ContentValues();
        cv.put("isSavePassword", isSavePassword); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改账户
     * @param
     */  
    public void alterUsername(String id) {
        ContentValues cv = new ContentValues();
        cv.put("id", id); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改密码
     * @param
     */  
    public void alterPassword(String password) {
        ContentValues cv = new ContentValues();
        cv.put("password", password); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改昵称
     * @param
     */  
    public void alterNickname(String nickname) {
        ContentValues cv = new ContentValues();
        cv.put("nickname", nickname); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改地址
     * @param
     */  
    public void alterAddress(String address) {
        ContentValues cv = new ContentValues();
        cv.put("address", address); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改电话
     * @param
     */  
    public void alterPhone_no(String phone_no) {
        ContentValues cv = new ContentValues();
        cv.put("phone_no", phone_no); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改qq
     * @param
     */  
    public void alterQQ(String qq) {
        ContentValues cv = new ContentValues();
        cv.put("qq", qq); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改qq
     * @param
     */  
    public void alterEmail(String email) {
        ContentValues cv = new ContentValues();
        cv.put("email", email); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改是否师傅
     * @param
     */  
    public void alterIs_professor(Boolean is_professor) {
        ContentValues cv = new ContentValues();
        cv.put("is_professor", is_professor); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改分数
     * @param
     */  
    public void alterTotal_score(int total_score) {
        ContentValues cv = new ContentValues();
        cv.put("total_score", total_score); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改真名
     * @param
     */  
    public void alterRealname(String name) {
        ContentValues cv = new ContentValues();
        cv.put("name", name); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * 修改身份证号
     * @param
     */  
    public void alterIdcard(String idcard) {
        ContentValues cv = new ContentValues();
        cv.put("idcard", idcard); 
        db.update("myself", cv, "id = ?", new String[]{id});
    }
    /** 
     * delete old person 
     * @param user 
     */  
    public void deleteOldPerson(User user) {  
        db.delete("person", "age >= ?", new String[]{String.valueOf(user.age)});  
    }  
      
    /** 
     * query all persons, return list 
     * @return List<Person> 
     */  
    public List<User> query() {  
        ArrayList<User> users = new ArrayList<User>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
            User user = new User();  
            user._id = c.getInt(c.getColumnIndex("_id"));  
            user.name = c.getString(c.getColumnIndex("name"));  
            user.age = c.getInt(c.getColumnIndex("age"));  
            user.info = c.getString(c.getColumnIndex("info"));  
            users.add(user);  
        }  
        c.close();  
        return users;  
    }  
      
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM person", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}
