package com.itlao.repairservice.utils;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.WindowManager;

public class ClipphotoOfDetail {

		/**
	     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	     * @param bitmap      原图

	     * @return  缩放截取正中部分后的位图。
	     */
	    public  Bitmap getPhoto(Bitmap bitmap,Context context)
	    {
	     if(null == bitmap )
	     {
	      return  null;
	     }
	     
	     WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

	     int width = wm.getDefaultDisplay().getWidth();
	     int height =dip2px(context, 150);//算出150dip为多少px
	     
	     int edgeLength=0;
	     Bitmap result = bitmap;
	     int widthOrg = bitmap.getWidth();
	     int heightOrg = bitmap.getHeight();
	     
	     if(widthOrg >=heightOrg*(width/height))
	     {
	    	 edgeLength=heightOrg;
	    	 try{
	    		 int cut_width=heightOrg*(width/height);
	             result = Bitmap.createBitmap(result, (int)((widthOrg-cut_width)/2), 0,cut_width, edgeLength);
	             
	            }
	            catch(Exception e){
	             return null;
	            }     
	     }else {
	    	 
	    	 edgeLength=widthOrg;
	    	 try{
	    		 int cut_height=(widthOrg*height)/width;int cut=(int)((heightOrg-cut_height)/2);
	             result = Bitmap.createBitmap(result, 0, (int)((heightOrg-cut_height)/2), edgeLength, cut_height);
	             
	            }
	            catch(Exception e){
	             return null;
	            }   
	     }
	          
	     return result;//
	    }
	    
	    public static int dip2px(Context context, float dipValue){ 
            final float scale = context.getResources().getDisplayMetrics().density; 
            return (int)(dipValue * scale + 0.5f); 
    } 
    
	    public static int px2dip(Context context, float pxValue){ 
            final float scale = context.getResources().getDisplayMetrics().density; 
            return (int)(pxValue / scale + 0.5f); 
    } 
	 

}
