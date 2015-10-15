package com.itlao.repairservice.pcenter.setting;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;

public class SettingDialog {

	
	static String edit;
	
	
	public static String Setting_text(Context context,String text,int textview_ID) {
		
		final TextView content=(TextView) ((Activity) context).findViewById(textview_ID);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);   
		 LayoutInflater factory = LayoutInflater.from(context);  
		 final View textEntryView = factory.inflate(R.layout.edit_dialog, null);  
		     //builder.setIcon(R.drawable.icon);  
		     builder.setTitle(text);  
		     builder.setView(textEntryView);  
		     final EditText edittext = (EditText) textEntryView.findViewById(R.id.setting_nicheng);
		     edittext.setText(content.getText());
		     
		     builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {  
		           
		         
		         
		         //showDialog("姓名 ："  + userName.getText().toString()  + "密码：" + password.getText().toString() ); 
		          edit=edittext.getText().toString().trim();
		          content.setText(edit);
		         }  
		     });  
		     builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {  
		 
		        	 edit=null;
		         }  
		     });  
		   builder.create().show();
		   return edit;
		       } 
		       

}
