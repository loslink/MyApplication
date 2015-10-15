package com.itlao.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //数据库第一次被创建时onCreate会被调用  (第一次安装程序)
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT)");*/
    	/*db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT,id VARCHAR,password VARCHAR,address VARCHAR,h_s VARCHAR,p_n VARCHAR,qq VARCHAR,email VARCHAR,is_pro BOOLEAN,t_s INTEGER,profession INTEGER)");
    	*/
        //
        db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, info TEXT,id char(11),password varchar(256),nickname varchar(20),address varchar(100),head_sculpture varchar(256),phone_no char(11),qq varchar(13),email varchar(40),is_professor BOOLEAN,total_score INTEGER,profession tinyint)");
        //user表
        db.execSQL("CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER,sex VARCHAR(1), info TEXT,id char(11),password varchar(256),nickname varchar(20),address varchar(100),head_sculpture varchar(256),phone_no char(11),qq varchar(13),email varchar(40),is_professor BOOLEAN,total_score INTEGER,profession tinyint,idcard char(18),expertcontent varchar(256))");
        //question表
        db.execSQL("CREATE TABLE IF NOT EXISTS question" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id BIGINT,word varchar(800),sound varchar(256),pictures varchar(256),question_class tinyint,owner_id char(11),public_time datetime,received_time datetime,solved_time datetime,address varchar(100),professor_id char(11),status INTEGER)");
        //feedback表
        db.execSQL("CREATE TABLE IF NOT EXISTS feedback" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id BIGINT,user_feedback varchar(800),user_feedback_time datetime,professor_feedback varchar(800),professor_feedback_time datetime,score tinyint)");
        //class表
        db.execSQL("CREATE TABLE IF NOT EXISTS class" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id tinyint,name varchar(10))");
        //myself表
        db.execSQL("CREATE TABLE IF NOT EXISTS myself" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,age INTEGER,idcard  char(18),sex VARCHAR(1),isSavePassword BOOLEAN,id char(11),password varchar(256),nickname varchar(20),address varchar(100),head_sculpture varchar(256),phone_no char(11),qq varchar(13),email varchar(40),is_professor BOOLEAN,total_score INTEGER,profession tinyint,expertcontent varchar(256))");
        //messages表
        db.execSQL("CREATE TABLE IF NOT EXISTS messages" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time datetime,content VARCHAR(256))");
        //sc_shifu表
        db.execSQL("CREATE TABLE IF NOT EXISTS sc_shifu" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id BIGINT)");

        //int id, String  name
        //Long id, String  u_fb, String u_fb_t,String p_fb,String p_fb_t,int score
        //Long id, String  word, String sound,String pictures,int q_class,String o_id,String p_t,String r_t,String s_t,String address,String p_id,int status
        //String name, int age, String info,String id,String password,String password,String address,String h_s,String p_n,String qq,String email,boolean is_pro,int t_s,int profession
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }

}
