package com.itlao.repairservice.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class CirclePhoto {

		/**
	     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	     * @param bitmap      原图
	     * @return  缩放截取正中部分后的位图。
	     */
	    public static Bitmap getCirclePhoto(Bitmap bitmap)
	    {
	     if(null == bitmap )
	     {
	      return  null;
	     }
	     int edgeLength=0;
	     Bitmap result = bitmap;
	     int widthOrg = bitmap.getWidth();
	     int heightOrg = bitmap.getHeight();
	     
	     if(widthOrg >=heightOrg)
	     {
	    	 edgeLength=heightOrg;
	    	 try{
	             result = Bitmap.createBitmap(result, (widthOrg-heightOrg)/2, 0, edgeLength, edgeLength);
	             
	            }
	            catch(Exception e){
	             return null;
	            }     
	     }else {
	    	 
	    	 edgeLength=widthOrg;
	    	 try{
	             result = Bitmap.createBitmap(result, 0, (heightOrg-widthOrg)/2, edgeLength, edgeLength);
	             
	            }
	            catch(Exception e){
	             return null;
	            }   
	     }
	          
	     return toRoundCorner(result,2);//使正方形图片变成正圆
	    }
	    
	  //如果图片是正方形的，将ratio设置为2，如果图片不是正方形，自己再做个截图吧！将图片截成需要的正方形。显示圆角边长1/4,则传入8，以此类推！
	  		public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
	  	        System.out.println("图片是否变成圆形模式了+++++++++++++");
	  	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	  	                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	  	        Canvas canvas = new Canvas(output);
	  	 
	  	        final Paint paint = new Paint();
	  	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	  	        final RectF rectF = new RectF(rect);
	  	 
	  	        paint.setAntiAlias(true);
	  	        canvas.drawARGB(0, 0, 0, 0);
	  	        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
	  	                bitmap.getHeight() / ratio, paint);
	  	 
	  	        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	  	        canvas.drawBitmap(bitmap, rect, rect, paint);
	  	        System.out.println("pixels+++++++" + String.valueOf(ratio));
	  	 
	  	        return output;
	  	 
	  	    }

}
