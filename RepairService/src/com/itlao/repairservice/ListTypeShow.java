package com.itlao.repairservice;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.home.ctrl.MainActivity;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class ListTypeShow {

	private static SimpleAdapter adapterSimple;  
	private static GridView gridView; 
	
	//创建一个ArrayList列表,内部存的是HashMap列表   
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>(); 
	
	
	public static void List_questions_show(String[] text_item1,String[] text_item2) {
		
		gridView = (GridView)MainActivity.mainActivity.findViewById(R.id.page_home_grid_type);  
		
		  
		 //将数组信息分别存入ArrayList中   
		int len = text_item1.length;   
		       for(int i=0; i < len ; i++){   
		           HashMap<String, Object> map = new HashMap<String, Object>(); //"/storage/sdcard1/tieba/bing.jpg"
		           
		           map.put("item1", text_item1[i].trim()); 
		           map.put("item2", text_item2[i].trim());
		           listItems.add(map);    
		       } 
		       

	 // HashMap中的Key信息,要与grid_item.xml中的信息作对应   
		       String[] from = {"item1","item2"};   
		       //grid_item.xml中对应的ImageView控件和TextView控件   
		       int[] to = {R.id.item_textView_type1,R.id.item_textView_type2};   
		       // 设定一个适配器   
		       adapterSimple = new SimpleAdapter(MainActivity.mainActivity, listItems, R.layout.grid_item_type, from, to);   
		  
		       // 对GridView进行适配   
		       gridView.setAdapter(adapterSimple); 
		 
		       /*实现ViewBinder()这个接口*/  
		   
	}
	
	       
}
