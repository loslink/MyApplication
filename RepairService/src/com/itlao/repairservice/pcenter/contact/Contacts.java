package com.itlao.repairservice.pcenter.contact;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.utils.GetPhoneContacts;
import com.itlao.repairservice.utils.GetPhoneContacts.SamContact;

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

public class Contacts {

	private static SimpleAdapter adapterSimple;  
	private static GridView gridView; 
	
	//创建一个ArrayList列表,内部存的是HashMap列表   
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>(); 
	
	
	public static void List_questions_show(Context context,ArrayList<SamContact> arrayList) {
		
		gridView = (GridView)((Activity) context).findViewById(R.id.page_home_grid_people);  
		
		  
		 //将数组信息分别存入ArrayList中   
		int len = arrayList.size();   
		       for(int i=0; i < len ; i++){   
		           HashMap<String, Object> map = new HashMap<String, Object>(); //"/storage/sdcard1/tieba/bing.jpg"
		           
		   		   //Bitmap bm_square=centerSquareScaleBitmap(BitmapFactory.decodeFile(pc_paths[i].trim(), null));
		   		   
		           map.put("image",arrayList.get(i).phone );   
		           map.put("item", arrayList.get(i).name);   
		           listItems.add(map);    
		       } 
		       

	 // HashMap中的Key信息,要与grid_item.xml中的信息作对应   
		       String[] from = {"image", "item"};   
		       //grid_item.xml中对应的ImageView控件和TextView控件   
		       int[] to = {R.id.item_textView_phone, R.id.item_textView_shoucan_or_yaoqing};   
		       // 设定一个适配器   
		       adapterSimple = new SimpleAdapter(context, listItems, R.layout.grid_item_people, from, to);   
		  
		       // 对GridView进行适配   
		       gridView.setAdapter(adapterSimple); 
		 
		       /*实现ViewBinder()这个接口*/  
		  /* adapterSimple.setViewBinder(new ViewBinder() {  
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
		       动态跟新ListView  
		       adapterSimple.notifyDataSetChanged();  */
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
