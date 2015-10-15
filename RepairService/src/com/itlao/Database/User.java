package com.itlao.Database;

import android.R.string;

public class User {

	public int _id;  
    public String name;  
    public int age;  
    public String info;  
    public String  id;
    public String password;
    public String nickname;
    public String address;
    public String head_sculpture;
    public String phone_no;
    public String qq;
    public String email;
    public boolean is_professor;
    public int total_score;
    public int profession;
    public String sex;
    public String idcard;
    public String expertcontent;
      
    public User() {  
    }  
      
    public User(String name, int age,String sex, String info,String id,String password,String nickname,String address,String head_sculpture,String phone_no,String qq,String email,boolean is_professor,int total_score,int profession,String idcard,String expertcontent) {  
        this.name = name;  
        this.age = age;  
        this.info = info;
        
        this.sex=sex;
        this.idcard=idcard;
        this.expertcontent=expertcontent;
        this.id = id; 
        this.password = password;  
        this.nickname = nickname;  
        this.address = address; 
        this.head_sculpture = head_sculpture;  
        this.phone_no = phone_no;  
        this.qq = qq; 
        this.email = email; 
        this.is_professor = is_professor;  
        this.total_score = total_score;  
        this.profession = profession;
    }  
}
