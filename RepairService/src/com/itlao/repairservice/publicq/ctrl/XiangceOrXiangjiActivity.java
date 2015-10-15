package com.itlao.repairservice.publicq.ctrl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.utils.SavePhoto;





import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class XiangceOrXiangjiActivity extends Activity {

	public static XiangceOrXiangjiActivity zhaoshifuActivity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private TextView fabu_xiangcedaoru;
	private TextView fabu_xiangjipaishe;
	public static Bitmap bitmap_photo=null;
	private ImageView things_photo1;
	private boolean isPhoto1=false;
	private boolean is_xiangcedaoru=false;
	private boolean is_xiangjipaishe=false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiangji_xiangce);
		zhaoshifuActivity=this;
		
		fabu_xiangcedaoru=(TextView) findViewById(R.id.fabu_xiangcedaoru);
		fabu_xiangjipaishe=(TextView) findViewById(R.id.fabu_xiangjipaishe);
		/*ll_layout_fabu1=(LinearLayout) findViewById(R.id.llayout_fabu1);
		ll_layout_fabu2=(LinearLayout) findViewById(R.id.llayout_fabu2);*/
		
		fabu_xiangcedaoru.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				choosePicture();
				is_xiangcedaoru=true;
                //isPhoto1=true;
                //things_photo1.setImageBitmap(bitmap_photo);// 将图片显示在ImageView里  
			}
		});
		fabu_xiangjipaishe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
                startActivityForResult(intent, 1);
                //finish();
                is_xiangjipaishe=true;
                //isPhoto1=true;
                //things_photo1.setImageBitmap(bitmap_photo);// 将图片显示在ImageView里  
			}
		});
	}

	public static byte[] readStream(InputStream inStream) throws Exception { 
        byte[] buffer = new byte[1024]; 
        int len = -1; 
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
        while ((len = inStream.read(buffer)) != -1) { 
                 outStream.write(buffer, 0, len); 
        } 
        byte[] data = outStream.toByteArray(); 
        outStream.close(); 
        inStream.close(); 
        return data; 

   } 
	
	public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) { 
        if (bytes != null) 
            if (opts != null) 
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
            else 
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
        return null; 
    } 
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data); 
        //相册选择
        if(is_xiangcedaoru==true) {
        	
        	//返回图库图片
            switch(resultCode){
                                 case RESULT_OK:    
                                   Uri uri = data.getData(); // 得到Uri
                                   byte[] mContent = null; 
                                   ContentResolver resolver = getContentResolver();
				try {
					mContent=readStream(resolver.openInputStream(Uri.parse(uri.toString())));
					//将字节数组转换为ImageView可调用的Bitmap对象 
                    bitmap_photo = getPicFromBytes(mContent, null); 
                    
                    SavePhoto.bitmap_photo=getSquarePhoto(bitmap_photo);
                    //String fPath = uri2filePath(uri); // 转化为路径
                    // things_photo1.setImageURI(uri);
                    break;
				} catch (Exception e) {
					
					e.printStackTrace();
				} 
                                  
                            }
            is_xiangcedaoru=false;
            finish();
            //相机拍摄
        }else if(is_xiangjipaishe==true) {
        	

            //返回相机图片
            if (resultCode == Activity.RESULT_OK) {  
                String sdStatus = Environment.getExternalStorageState();  
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
                    Log.i("TestFile",  
                            "SD card is not avaiable/writeable right now.");  
                    return;  
                }  
                String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
                Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
                Bundle bundle = data.getExtras();  
                bitmap_photo = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
              
                FileOutputStream b = null;  
               //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？  
                File file = new File("/sdcard/myImage/");  
                file.mkdirs();// 创建文件夹  
                String fileName = "/sdcard/myImage/"+name;  
      
                try {  
                    b = new FileOutputStream(fileName);  
                    bitmap_photo.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
                    SavePhoto.bitmap_photo=getSquarePhoto(bitmap_photo);
                } catch (FileNotFoundException e) {  
                    e.printStackTrace();  
                } finally {  
                    try {  
                        b.flush();  
                        b.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
                
                //if(isPhoto1==true) {
                	//choosePicture();
                	//things_photo1.setImageBitmap(getSquarePhoto(bitmap_photo));
                	//isPhoto1=false;
                /*}else if(isPhoto2==true) {
                	
                	things_photo2.setImageBitmap(getSquarePhoto(bitmap_photo));
                	isPhoto2=false;
                }else if(isPhoto3==true) {
                	
                	things_photo3.setImageBitmap(getSquarePhoto(bitmap_photo));
                	isPhoto3=false;
                }*/
                
                
            }  
            is_xiangjipaishe=false;
            finish();
        }
        
    }
	
	//选择相册
			public void choosePicture(){
			    Intent intent3 = new Intent();
		     intent3.setType("image/*");
			    intent3.setAction(Intent.ACTION_GET_CONTENT);
			    startActivityForResult(intent3, 1);
			}

			
			//获取正方形图片
			public static Bitmap getSquarePhoto(Bitmap bitmap)
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
		          
		     return result;//正方形
		    }
	
}
