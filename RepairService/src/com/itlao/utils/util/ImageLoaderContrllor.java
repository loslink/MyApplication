package com.itlao.utils.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;





import com.itlao.repairservice.R;
import com.itlao.utils.util.FileCache.FileClearProcessListener;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author KeJianLong
 * @creatTime 2013-6-28	下午04:30:59
 * @param:
 */

public class ImageLoaderContrllor implements Serializable{
	private static final long serialVersionUID = -7060210544600464481L;
	private static ImageLoaderContrllor contrllor;
	MemoryCache memoryCache = new MemoryCache();  
    FileCache fileCache;  
    Resources resources;
    private final String paramUrl = "&photoType=";
       
    // 线程池  
    ExecutorService executorService;  
  
    private ImageLoaderContrllor(Context context) {  
        fileCache = new FileCache(context);  
        executorService = Executors.newFixedThreadPool(5);
    }  
    
    public static ImageLoaderContrllor getInstance(Context context){
    	if(contrllor == null) contrllor = new ImageLoaderContrllor(context);
    	return contrllor;
    }
  
    // 当进入listview时默认的图片，可换成你自己的默认图片  
    final int loading = R.drawable.loading;  
  
    public void displayImage(PhotoToLoad p) {  
        // 先从内存缓存中查找  
        Bitmap bitmap = memoryCache.get(p.getType() + p.getId());  
        if (bitmap != null)  
        	drawImage(p.getView(), bitmap);  
        else {  
        	 // 若没有的话则开启新线程加载图片  
        	executorService.submit(new PhotosLoader(p)); 
            drawImage(p.getView(), bitmap);
        }  
    }  
  
    private Bitmap getBitmap(PhotoToLoad photoToLoad) {  
        File f = fileCache.getFile(photoToLoad.getId(), photoToLoad.getType());  
  
        // 先从文件缓存中查找是否有 
        Bitmap b = decodeFile(f);  
        if (b != null)  
            return b;  
  
     // 最后从Byte[]中生成图片  
        try {  
        	Bitmap bitmap = null; 
            String url = Config.getUrl("viewImage_url") + photoToLoad.getId() + paramUrl + photoToLoad.getType();
        	URL imageUrl = new URL(url); 
        	Log.i("viewThumbnail_url", url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl  
                    .openConnection();  
            conn.setConnectTimeout(30000);  
            conn.setReadTimeout(30000);  
            conn.setInstanceFollowRedirects(true);  
            InputStream is = conn.getInputStream();  
            OutputStream os = new FileOutputStream(f); 
            
            CopyStream(is, os);  
            os.close();   
            bitmap = decodeFile(f);
            return bitmap; 
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }  
  
 // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的  
    private Bitmap decodeFile(File f) {  
    	if(f == null || (f != null && f.length() == 0)){
    		return null;
    	}
        try { 
        	 FileInputStream fis = new FileInputStream(f);
        	 BufferedInputStream bis = new BufferedInputStream(fis);
        	 Bitmap bm = BitmapFactory.decodeStream(bis);
        	 bis.close();
        	 fis.close();
            return bm;  
        } catch (FileNotFoundException e) {  
        	return null;
        } catch (IOException e) {
			e.printStackTrace();
			return null;
		}  
    }  
  
    // Task for the queue  
     
  
    class PhotosLoader implements Runnable {  
        PhotoToLoad photoToLoad;  
  
        PhotosLoader(PhotoToLoad photoToLoad) {  
            this.photoToLoad = photoToLoad;  
        }  
  
        @Override  
        public void run() {  
            /*if (imageViewReused(photoToLoad))  
                return;*/  
            Bitmap bmp = getBitmap(photoToLoad);  
            memoryCache.put(photoToLoad.getType() + photoToLoad.getId() , bmp);  
            /*if (imageViewReused(photoToLoad))  
                return; */ 
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);  
         // 更新的操作放在UI线程中  
            Activity a = (Activity) photoToLoad.getView().getContext();  
            a.runOnUiThread(bd);  
        }  
    }  
  
  
 // 用于在UI线程中更新界面 
    class BitmapDisplayer implements Runnable {  
        Bitmap bitmap;  
        PhotoToLoad photoToLoad;  
  
        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {  
            bitmap = b;  
            photoToLoad = p;  
        }  
  
        public void run() {  
        	drawImage(photoToLoad.getView(), bitmap);  
            	
        }  
    }  
  
    public void clearCache() {  
        memoryCache.clear();  
    }  
    
    public void clearFileCache(){
    	fileCache.clear();  
    }
  
    public static void CopyStream(InputStream is, OutputStream os) {  
        final int buffer_size = 1024;  
        try {  
            byte[] bytes = new byte[buffer_size];  
            for (;;) {  
                int count = is.read(bytes, 0, buffer_size);  
                if (count == -1)  
                    break;  
                os.write(bytes, 0, count);  
            }  
        } catch (Exception ex) {  
        }  
    } 
    
    private void drawImage(View view, Bitmap bmp){
    	if(view instanceof TextView){
    		if(bmp != null){
    			((TextView)view).setBackgroundDrawable(new BitmapDrawable(bmp));
    		}else{
    			((TextView)view).setBackgroundResource(loading);
    		}
    	}else if(view instanceof ImageButton){
    		if(bmp != null){
    			((ImageButton)view).setBackgroundDrawable(new BitmapDrawable(bmp));
    		}else{
    			((ImageButton)view).setBackgroundResource(loading);
    		}
    	}else if(view instanceof ImageView){
    		if(bmp != null){
    			((ImageView)view).setImageBitmap(bmp);
    		}else{
    			((ImageView)view).setImageResource(loading);
    		}
    		
    	} 
    }
    
    public void setFileClearProcessListener(FileClearProcessListener fileClearProcessListener) {
		this.fileCache.setFileClearProcessListener(fileClearProcessListener);
	}
}
