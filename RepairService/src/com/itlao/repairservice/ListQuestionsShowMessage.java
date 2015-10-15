package com.itlao.repairservice;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class ListQuestionsShowMessage {

	private static SimpleAdapter adapterSimple;  
	private static GridView gridView; 
	
	//创建一个ArrayList列表,内部存的是HashMap列表   
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>(); 
	
	
	public static void List_questions_show(Context context,String[] text_item) {
		
		gridView = (GridView)((Activity) context).findViewById(R.id.page_home_grid_message);  
		
		  
		 //将数组信息分别存入ArrayList中   
		int len = text_item.length;   
		       for(int i=0; i < len ; i++){   
		           HashMap<String, Object> map = new HashMap<String, Object>(); //"/storage/sdcard1/tieba/bing.jpg"
		           
		           map.put("item", text_item[i]);   
		           listItems.add(map);    
		       } 
		       

	 // HashMap中的Key信息,要与grid_item.xml中的信息作对应   
		       String[] from = {"item"};   
		       //grid_item.xml中对应的ImageView控件和TextView控件   
		       int[] to = {R.id.item_textView_message};   
		       // 设定一个适配器   
		       adapterSimple = new SimpleAdapter(context, listItems, R.layout.grid_item_message, from, to);   
		  
		       // 对GridView进行适配   
		       gridView.setAdapter(adapterSimple); 
		 
		       /*实现ViewBinder()这个接口*/  
		   
	}
	
	       
}
