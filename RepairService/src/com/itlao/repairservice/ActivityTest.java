package com.itlao.repairservice;

import android.os.Bundle; 
import android.view.Window; 
import android.widget.ImageView; 
import android.app.Activity; 
public class ActivityTest extends Activity{ 
private ImageView ivMore; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
setContentView(R.layout.activity_mian_page); 
initView(); 

} 

private void initView(){

ivMore = (ImageView) findViewById(R.id.ivMore); 



} 

} 

