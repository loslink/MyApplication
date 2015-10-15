package com.itlao.repairservice.findq.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.findmaster.ListShifuShow;





import android.os.Bundle;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class FindQuetionActivity extends Activity {

	public static FindQuetionActivity zhaoshifuActivity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item = { "大师兄", "二师兄 ", "三师兄", "大师兄", "大师兄 ", "大师兄", "大师兄", "大师兄 ", "大师兄", "大师兄","大师兄", "大师兄 ", "猪八戒", "沙和尚" };  
	
	private String[] pc_path = { "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg ", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg","/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg ", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg" };   
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_shifu);
		zhaoshifuActivity=this;
		/*ListShifuShow lsfs=new ListShifuShow();
		 lsfs.List_questions_show(slidingMenu);*/
		
	}

	
}
