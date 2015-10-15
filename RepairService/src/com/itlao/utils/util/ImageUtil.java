package com.itlao.utils.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class ImageUtil {
	public static Bitmap readImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ��ʱ����bmΪ��

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ�Ƚ϶��ｿ00*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��̿80f
		// ��űȡ������ǹ̶�������ţ�ֻ�ø߻��߿�����һ����ݽ��м��㼴�ｿ
		int be = 1;// be=1��ʾ����ｿ
		if (w > h && w > ww) {// ����ȴ�Ļ���ݿ�ȹ̶���С��ｿ
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ���߶ȸߵĻ���ݿ�ȹ̶���С��ｿ
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// ������ű��ｿ
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// ѹ��ñ����С���ٽ�����ѹ��
	}
	
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ��ѹ��������ｿ00��ʾ��ѹ���ѹ������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ���ͼƬ�Ƿ���ｿ00kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ�������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream������ͼƬ
		return bitmap;
	}
	
	// ��ˮӡ Ҳ���Լ�����
		public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String title) {
			if (src == null) {
				return null;
			}
			int w = src.getWidth();
			int h = src.getHeight();
			// ��Ҫ����ͼƬ̫����ɵ��ڴ泬�������,�����ҵ�ͼƬ��С���Բ�д��Ӧ������
			Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// ����һ���µĺ�SRC���ȿ��һ���λͼ
			Canvas cv = new Canvas(newb);
			cv.drawBitmap(src, 0, 0, null);// �� 0��0��꿪ʼ����src
			Paint paint = new Paint();
			// ����ͼƬ
			if (watermark != null) {
				int ww = watermark.getWidth();
				int wh = watermark.getHeight();
				paint.setAlpha(50);
				cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// ��src�����½ǻ���ˮӡ
			}
			// ��������
			if (title != null) {
				String familyName = "����";
				Typeface font = Typeface.create(familyName, Typeface.BOLD);
				TextPaint textPaint = new TextPaint();
				textPaint.setColor(Color.RED);
				textPaint.setTypeface(font);
				textPaint.setTextSize(22);
				// �������Զ����е�
				StaticLayout layout = new StaticLayout(title, textPaint, w,
						Alignment.ALIGN_OPPOSITE, 1.0F, 0.0F, true);
				layout.draw(cv);
			}
			cv.save(Canvas.ALL_SAVE_FLAG);// ����
			cv.restore();// �洢
			return newb;
		}
		
		/**
		 * ��ȡ������ͼƬ
		 * @return
		 */
		public static Bitmap serverBitMap(String imagePath){
			HttpGet get = new HttpGet(imagePath); 
		    HttpClient client = new DefaultHttpClient(); 
		    Bitmap bitMap = null; 
		    try { 
		        HttpResponse response = client.execute(get); 
		        HttpEntity entity = response.getEntity(); 
		        InputStream is = entity.getContent(); 

		        bitMap = BitmapFactory.decodeStream(is); 

		    } catch (Exception e) { 
		        e.printStackTrace(); 
		    } 
		    return bitMap; 
	    }
       
	    /**
	     * ���ط����ͼĿ
	     * @param url
	     * @param imageView
	     */
		public  void loadImage(final String url,final ImageView imageView) { 
		
	        final Handler handler = new Handler() { 
	            public void handleMessage(Message msg){ 
	            	Bitmap bitmap=(Bitmap) msg.obj;
	            	//bitmap.setDensity((int) (bitmap.getDensity()*1.3));
	            	imageView.setImageBitmap(resizeImage(bitmap));
	            } 
	        }; 

	        new Thread(new Runnable(){ 
	            public void run(){ 
	                try{ 
	                    URL u = new URL(url); 

	                    HttpURLConnection httpConnection = (HttpURLConnection)u.openConnection(); 

	                    if (httpConnection.getResponseCode()== HttpURLConnection.HTTP_OK){ 

	                        Bitmap bm = BitmapFactory.decodeStream(httpConnection.getInputStream()); 
	                        int w=bm.getWidth();
	                        int h=bm.getHeight();
	                        
	                        sendMessage(handler, bm); 
	                      
	                        return; 
	                    } 
	                    sendMessage(handler, null); 

	                } catch (MalformedURLException e){ 
	                    sendMessage(handler, null); 
	                }catch (IOException e) { 
	                    sendMessage(handler, null); 
	                } 
	            } 
	        }).start(); 
	    } 
	    
	    /**
	     * ��handler���ʹ������Ϳ
	     * @param handler
	     * @param bm
	     */ 
	    private void sendMessage(Handler handler, Bitmap bm) { 

	        Message msg = handler.obtainMessage(); 

	        msg.obj = bm; 

	        handler.sendMessage(msg);
	    } 
		
	    public static Bitmap resizeImage(Bitmap bitmap) {
            if(bitmap==null){
            	return bitmap;
            }
	        Bitmap BitmapOrg = bitmap;
	       
	        int width = BitmapOrg.getWidth();
	        int height = BitmapOrg.getHeight();
	        
	        float scaleWidth = 1.27f;
	        float scaleHeight =1.27f;

	        Matrix matrix = new Matrix();

	        matrix.postScale(scaleWidth, scaleHeight);

	        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
	                        height, matrix, true);
	       return resizedBitmap;

	    }
	    
	    /**
	     * ��ȡSRC·��
	     * @param image
	     * @return
	     */
	    public static String getImageSRCPath(String image){
	    	//1.
    	   int imageLen=image.length();
		   int srcINDEX=image.indexOf("src")+3;
		   
		   String imageSRCPath=image.substring(srcINDEX,imageLen);
		   imageSRCPath=imageSRCPath.trim();
		   
		   int startLen=imageSRCPath.indexOf("\"")+1;
		   int endLen=imageSRCPath.lastIndexOf("\"");
		   
		   String path=imageSRCPath.substring(startLen, endLen);
	       return path;
	    }
	    
	    /**
		 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处ﺿ1.
		 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度ﺿ
		 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图ὠ2.
		 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸Ὅ
		 * 
		 * @param imagePath
		 *            图像的路弿
		 * @param width
		 *            指定输出图像的宽帿
		 * @param height
		 *            指定输出图像的高帿
		 * @return 生成的缩略图
		 */
		public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
			Bitmap bitmap = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高，注意此处的bitmap为null
			bitmap = BitmapFactory.decodeFile(imagePath, options);
			options.inJustDecodeBounds = false; // 设为 false
			// 计算缩放歿
			int h = options.outHeight;
			int w = options.outWidth;
			int beWidth = w / width;
			int beHeight = h / height;
			int be = 1;
			if (beWidth < beHeight) {
				be = beWidth;
			} else {
				be = beHeight;
			}
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
			bitmap = BitmapFactory.decodeFile(imagePath, options);
			// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bitmap;
		}
	
}
