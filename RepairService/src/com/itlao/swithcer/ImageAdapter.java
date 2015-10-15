package com.itlao.swithcer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import com.itlao.repairservice.QuetionDetailActivity;
import com.itlao.repairservice.R;
import com.itlao.repairservice.utils.ClipphotoOfDetail;
import com.itlao.repairservice.utils.EncodeDecodeUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter{  
    private List<String> imageUrls;       //鍥剧墖鍦板潃list  
    private Context context;  
    private ImageAdapter self;
    Uri uri;
    Intent intent;
    ImageView imageView;
    static HashMap<String, Object> map;
   /*public static Integer[] imgs = {
 			R.drawable.one,
 			R.drawable.two,
			R.drawable.three,
			R.drawable.four
	};*/
    
    public static String[] imgs = {
    	"/storage/sdcard1/tieba/bing.jpg",
    	"/storage/sdcard1/tieba/long.jpg",
    	"/storage/sdcard1/tieba/bing.jpg",
    	"/storage/sdcard1/tieba/long.jpg"
};
    
    private String[] myuri = {
    		"http://www.36939.net/",
    		"http://www.36939.net/",
    		"http://www.36939.net/",
    		"http://www.36939.net/"
	};
    public ImageAdapter(/*List<String> imageUrls, */Context context,HashMap<String, Object> map) {  
       // this.imageUrls = imageUrls;  
        this.context = context;  
        this.self = this;
        this.map=map;
    }  
  
    public int getCount() {  
        return Integer.MAX_VALUE;  
    }  
  
    public Object getItem(int position) {  
        return imageUrls.get(position % imgs.length);  
    }  
   
    public long getItemId(int position) {  
        return position;  
    }  
  
    @SuppressWarnings("unused")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0: {
						self.notifyDataSetChanged();
					}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};
    
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        //Bitmap image;  
        if(convertView==null){  
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image,null); //瀹炰緥鍖朿onvertView  
            Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT,Gallery.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(params);
           // image = ((ImageActivity)context).imagesCache.get(imageUrls.get(position % imageUrls.size())); //浠庣紦瀛樹腑璇诲彇鍥剧墖  
           /* if(image==null){  
                //褰撶紦瀛樹腑娌℃湁瑕佷娇鐢ㄧ殑鍥剧墖鏃讹紝鍏堟樉绀洪粯璁ょ殑鍥剧墖  
                image = ((ImageActivity)context).imagesCache.get("background_non_load");  
                //寮傛鍔犺浇鍥剧墖  
                LoadImageTask task = new LoadImageTask(convertView);  
                task.execute(imageUrls.get(position % imageUrls.size()));  
            }  */
            convertView.setTag(imgs);  
  
        }  
       else{  
            //image = (Bitmap) convertView.getTag();  
        }  
       // TextView textView = (TextView) convertView.findViewById(R.id.gallery_text);  
       // textView.setText(position % imgs.length+" sdfsdfdsfdf");
       // textView.setBackgroundColor(Color.argb(200, 135, 135, 152));
         imageView = (ImageView) convertView.findViewById(R.id.gallery_image);  
        //imageView.setImageResource(imgs[position % imgs.length]);
         //取出图片
         Bitmap bmp = null;
         /*for(int i=0;i<map.size();i++){
        	 
        	 byte[] by;
			try {
				by = EncodeDecodeUtil.decode(map.get(i+"").toString());
				// 解决加载图片 内存溢出的问题
				// Options 只保存图片尺寸大小，不保存图片到内存
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
				opts.inSampleSize = 8; // 质量缩小8倍
				
				bmp = BitmapFactory.decodeByteArray(by, 0, by.length, opts);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
				
				
         }*/
        
         try {
        	 byte[] by;
				by = EncodeDecodeUtil.decode(map.get(position%map.size()+"").toString());
				// 解决加载图片 内存溢出的问题
				// Options 只保存图片尺寸大小，不保存图片到内存
				BitmapFactory.Options opts = new BitmapFactory.Options();
				// 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
				opts.inSampleSize = 2; // 质量缩小2倍
				
				bmp = BitmapFactory.decodeByteArray(by, 0, by.length, opts);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        //Bitmap bm_square=BitmapFactory.decodeFile(imgs[position % imgs.length].trim(), null);
        
        ClipphotoOfDetail cp=new ClipphotoOfDetail();
        bmp=cp.getPhoto(bmp, context);
        imageView.setImageBitmap(bmp);
        
        //璁剧疆缂╂斁姣斾緥锛氫繚鎸佸師鏍? 
        imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
       // textView.setText(position % imgs.length+" sdfsdfdsfdf");
        ((QuetionDetailActivity)context).changePointView(position % map.size());
        
        return convertView;  
        
    }  
  
   /* //鍔犺浇鍥剧墖鐨勫紓姝ヤ换鍔? 
    class LoadImageTask extends AsyncTask<String,Void,Bitmap>{  
         private View resultView;  
  
        LoadImageTask(View resultView) {  
            this.resultView = resultView;  
        }  
         // doInBackground瀹屾垚鍚庢墠浼氳璋冪敤  
        @Override  
            protected void onPostExecute(Bitmap bitmap) {  
                //璋冪敤setTag淇濆瓨鍥剧墖浠ヤ究浜庤嚜鍔ㄦ洿鏂板浘鐗? 
                resultView.setTag(bitmap);  
            }  
            //浠庣綉涓婁笅杞藉浘鐗? 
            @Override  
            protected Bitmap doInBackground(String... params) {  
                Bitmap image=null;  
                   try {  
                       //new URL瀵硅薄  鎶婄綉鍧?紶鍏? 
                       URL url = new URL(params[0]);  
                       //鍙栧緱閾炬帴  
                       URLConnection conn = url.openConnection();  
                       conn.connect();  
                       //鍙栧緱杩斿洖鐨処nputStream  
                       InputStream is = conn.getInputStream();  
                       //灏咺nputStream鍙樹负Bitmap  
                       image = BitmapFactory.decodeStream(is);  
                       is.close();  
                       ((ImageActivity)context).imagesCache.put(params[0],image);   //鎶婁笅杞藉ソ鐨勫浘鐗囦繚瀛樺埌缂撳瓨涓?
                       	Message m = new Message();
           				m.what = 0;
           				mHandler.sendMessage(m);
                   } catch (Exception e) {  
                       e.printStackTrace();  
                   }  
  
                return image;  
            }  
    }  */
}  
