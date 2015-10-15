package com.itlao.repairservice.publicq.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.drawable;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.publicq.Media_Recorder;
import com.itlao.repairservice.utils.SavePhoto;





import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FabuActivity extends Activity {

	public static FabuActivity fabu_Activity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item = { "大师兄", "二师兄 ", "三师兄", "大师兄", "大师兄 ", "大师兄", "大师兄", "大师兄 ", "大师兄", "大师兄","大师兄", "大师兄 ", "猪八戒", "沙和尚" };  
	
	private String[] pc_path = { "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg ", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg","/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg ", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg" };   
	
	
	ImageView things_photo1;
	ImageView things_photo2;
	ImageView things_photo3;
	Bitmap bitmap_photo;
	Spinner spinner02;
	private boolean isPhoto1=false;
	private boolean isPhoto2=false;
	private boolean isPhoto3=false;
	
	public final static int CONSULT_DOC_PICTURE = 1000;  
	public final static int CONSULT_DOC_CAMERA = 1001;  
	  
	  
	private int SELECT_PICTURE = 0;  
	private int SELECT_CAMERA = 1;  
	private ImageView iv;  
	private Bitmap bmp;  
	private Uri outputFileUri;  
	
	private static final String quetions_style[]={"电脑", "手机", "电视", "收音机", "装系统" ,"刷机"};
	private TextView textview;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private ImageView record;
	private MediaRecorder recorder;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_edit_1);
		fabu_Activity=this;
		//List_shifu_show.List_questions_show(item,pc_path);
		
		final RelativeLayout fabu_camera=(RelativeLayout) findViewById(R.id.fabu_camera);
		final RelativeLayout rl_tupian=(RelativeLayout) findViewById(R.id.rl_tupian);
		
		 things_photo1=(ImageView) findViewById(R.id.things_photo11);
		 things_photo2=(ImageView) findViewById(R.id.things_photo22);
		 things_photo3=(ImageView) findViewById(R.id.things_photo33);
		 
		 
		 record=(ImageView) findViewById(R.id.record);
		
		 ImageView iv_back=(ImageView) findViewById(R.id.back_edit);
		 iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
		 });
		 final Intent intent_xiangce_xiangji=new Intent(this,XiangceOrXiangjiActivity.class );
		
		 recorder=Media_Recorder.initializeAudio();
		 //spinner02.addView();
		
		 //开始录音
		 record.setOnClickListener(new OnClickListener() {

				private int mark=1;

				@Override
				public void onClick(View v) {
					
					
					/*recorder.stop();// 停止刻录  
	                // recorder.reset(); // 重新启动MediaRecorder.  
	                recorder.release(); // 刻录完成一定要释放资源  
	                // recorder = null;  
*/
					if (mark == 0) {
						record.setBackgroundResource(R.drawable.play);
						
						recorder.stop();// 停止刻录  
		                // recorder.reset(); // 重新启动MediaRecorder.  
		                recorder.release(); // 刻录完成一定要释放资源  
		                // recorder = null;
						mark++;
					} else {
						record.setBackgroundResource(R.drawable.record);
						recorder.start();// 开始录制  
						mark--;
					}
					
				}
			});
		 
		fabu_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				fabu_camera.setVisibility(View.GONE);
				rl_tupian.setVisibility(View.VISIBLE);
				startActivity(intent_xiangce_xiangji);
				isPhoto1=true;
				
			}
		});
		things_photo1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(intent_xiangce_xiangji);
                isPhoto1=true;
               
			}
		});
		things_photo2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(intent_xiangce_xiangji);
                isPhoto2=true;
				
			}
		});
		things_photo3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				startActivity(intent_xiangce_xiangji);
                isPhoto3=true;
               
			}
		});
		
		
		
		spinner=(Spinner)findViewById(R.id.Spinner01);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quetions_style);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//textview.setText("您的血型是："+m[arg2]);
				Toast.makeText(FabuActivity.this,quetions_style[arg2], Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}

	
	
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			if(isPhoto1==true) {
            	//choosePicture();
            	things_photo1.setImageBitmap(SavePhoto.bitmap_photo);
            	isPhoto1=false;
            }else if(isPhoto2==true) {
            	
            	things_photo2.setImageBitmap(SavePhoto.bitmap_photo);
            	isPhoto2=false;
            }else if(isPhoto3==true) {
            	
            	things_photo3.setImageBitmap(SavePhoto.bitmap_photo);
            	isPhoto3=false;
            }
		}

		
}
