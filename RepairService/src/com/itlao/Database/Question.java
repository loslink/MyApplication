package com.itlao.Database;

import android.R.string;

public class Question {

	public Long id;
	public String  word;
	public String sound;
	public String pictures;
	public int question_class;
	public String owner_id;
	public String public_time;
	public String received_time;
	public String solved_time;
	public String address;
	public String professor_id;
	public int status;
	
      
    public Question() {  
    }  
      
    public Question(Long id, String  word, String sound,String pictures,int question_class,String owner_id,String public_time,String received_time,String solved_time,String address,String professor_id,int status) {  
        
    	this.id = id;  
        this.word = word;  
        this.sound = sound;
        this.pictures = pictures; 
        this.question_class = question_class;  
        this.owner_id = owner_id;  
        this.public_time = public_time; 
        this.received_time = received_time;  
        this.solved_time = solved_time;  
        this.address = address;  
        this.professor_id = professor_id; 
        this.status = status;
    }  
}
