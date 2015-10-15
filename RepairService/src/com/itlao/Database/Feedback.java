package com.itlao.Database;

import android.R.string;

public class Feedback {

	public Long id ;
	public String  user_feedback;
	public String user_feedback_time;
	public String professor_feedback;
	public String professor_feedback_time;
	public int score;
	
      
    public Feedback() {  
    }  
      
    public Feedback(Long id, String  user_feedback, String user_feedback_time,String professor_feedback,String professor_feedback_time,int score) {  
        
    	this.id = id;  
        this.user_feedback = user_feedback;  
        this.user_feedback_time = user_feedback_time;
        this.professor_feedback = professor_feedback; 
        this.professor_feedback_time = professor_feedback_time;  
        this.score = score;  
        
    }  
}
