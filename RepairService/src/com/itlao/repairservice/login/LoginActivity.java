package com.itlao.repairservice.login;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;

import android.R.drawable;
import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Menu;
import android.widget.ImageView;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ImageView pc_head=(ImageView) findViewById(R.id.pc_head);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pc_1);
		Bitmap bm_square=centerSquareScaleBitmap(bm);
		//pc_head.setImageBitmap(bm_square);
		pc_head.setImageBitmap(toRoundCorner(bm_square,2));
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
	
	/**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
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
          
     return result;
    }

}
