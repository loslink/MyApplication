package com.itlao.repairservice.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ReadMessage {

		private static BufferedReader message_stream;
		private static UsersMessage users_Message=new UsersMessage();

		/**
	     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	     * @param bitmap      原图
	     * @param edgeLength  希望得到的正方形部分的边长
	     * @return  缩放截取正中部分后的位图。
	     */
	    public static UsersMessage getMessage(Context context)
	    {
	    	try {
	    		/*InputStream is = Login_Activity.activity.getAssets().open("message.txt");  
	            int size = is.available(); */
				//message_stream = new BufferedReader(new InputStreamReader(new FileInputStream("/RepairService/assets/message.txt"), "utf-8"));
	            message_stream = new BufferedReader(new InputStreamReader(context.getAssets().open("message.txt"), "GB2312"));
				//文件名
				String line;
				int i_index=0;
				boolean isUser=false;
				boolean isPassword=false;
				
				while((line=message_stream.readLine())!=null) {
					
					String[] str_id=line.trim().split("：");
					if(str_id[0].trim().startsWith("师傅")) {
						
					}else if(str_id[0].trim().startsWith("账号")) {
						
						/*if(str_id[1].trim().equals("laoqilian")) {
							isUser=true;
						}*/
						users_Message.setUsername(str_id[1].trim());
						
					}else if(str_id[0].trim().startsWith("密码")) {
						
						/*if(str_id[1].trim().equals("123")) {
							isPassword=true;
						}*/
						
						users_Message.setPassword(str_id[1].trim());
					}
					
					/*if(isUser&&isPassword) {
						
						final Intent intent=new Intent(context,MainActivity.class );
						context.startActivity( intent);
					}*/
					//String str=line.trim();
					
				}
				message_stream.close();
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				String string=e.getMessage();
				System.out.print(string);
				e.printStackTrace();
			}
	    	return users_Message;
	    }
	

}
