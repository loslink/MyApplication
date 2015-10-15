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

public class ListQuestionsShowHistory {

	private static SimpleAdapter adapterSimple;  
	private static GridView gridView; 
	
	//创建一个ArrayList列表,内部存的是HashMap列表   
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>(); 
	
	
	public static void List_questions_show(String[] text_item,String[] pc_paths) {
		
		gridView = (GridView)MainActivity.mainActivity.findViewById(R.id.page_home_grid_history);  
		
		  
		 //将数组信息分别存入ArrayList中   
		int len = text_item.length;   
		       for(int i=0; i < len ; i++){   
		           HashMap<String, Object> map = new HashMap<String, Object>(); //"/storage/sdcard1/tieba/bing.jpg"
		           
		   		   Bitmap bm_square=centerSquareScaleBitmap(BitmapFactory.decodeFile(pc_paths[i].trim(), null));
		   		   
		           map.put("image",bm_square );   
		           map.put("item", text_item[i]);   
		           listItems.add(map);    
		       } 
		       

	 // HashMap中的Key信息,要与grid_item.xml中的信息作对应   
		       String[] from = {"image", "item"};   
		       //grid_item.xml中对应的ImageView控件和TextView控件   
		       int[] to = {R.id.item_imageView_history, R.id.item_textView_history};   
		       // 设定一个适配器   
		       adapterSimple = new SimpleAdapter(MainActivity.mainActivity, listItems, R.layout.grid_item_history, from, to);   
		  
		       // 对GridView进行适配   
		       //gridView.setAdapter(adapterSimple); 
		 
		       /*实现ViewBinder()这个接口*/  
		   adapterSimple.setViewBinder(new ViewBinder() {  
		   public boolean setViewValue(View view, Object data,  
		           String textRepresentation) {  
		       // TODO Auto-generated method stub  
		       if(view instanceof ImageView && data instanceof Bitmap){  
		           ImageView i = (ImageView)view;  
		           i.setImageBitmap((Bitmap) data);  
		           return true;  
		       }  
		       return false;  
		   }}); 
		  
		       gridView.setAdapter(adapterSimple);  
		       /*动态跟新ListView*/  
		       adapterSimple.notifyDataSetChanged();  
	}
	
	       
	       /**
	        * 按照固定比例剪切图片。
	        * @param bitmap      原图
	        * @return  缩放截取正中部分后的位图。
	        */
	       public static Bitmap centerSquareScaleBitmap(Bitmap bitmap)
	       {
	        if(null == bitmap )
	        {
	         return  null;
	        }
	        int edgeLength=0;
	        Bitmap result = bitmap;
	        int widthOrg = bitmap.getWidth();
	        int heightOrg = bitmap.getHeight();
	        if(widthOrg <=((200/100)*heightOrg))
	        {
	       	 
	       	 try{
	       		 int w=(100*widthOrg)/200;
	       		 int e=(heightOrg-(widthOrg*200)/100)/2;
	                result = Bitmap.createBitmap(result, 0,(heightOrg-(widthOrg*100)/200)/2, widthOrg,w);
	                
	               }
	               catch(Exception e){
	                return null;
	               }     
	        }else {
	       	 
	       	 try{
	                result = Bitmap.createBitmap(result,(widthOrg-(heightOrg*200)/100)/2,0 , (heightOrg*200)/100, heightOrg);
	                
	               }
	               catch(Exception e){
	                return null;
	               }   
	        }
	            
	        return result;
	       }
}
