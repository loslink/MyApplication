package com.itlao.repairservice.findmaster.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.color;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.findmaster.ListQuestionsShowOders;
import com.itlao.repairservice.pcenter.immaster.ListQuestionsShowShifu;





import android.os.Bundle;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class ActivityOders extends Activity {

	public static ActivityOders activityOders;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	//private String[] item = { "大师兄", "二师兄 ", "三师兄", "大师兄", "大师兄 ", "大师兄", "大师兄", "大师兄 ", "大师兄", "大师兄","大师兄", "大师兄 ", "猪八戒", "沙和尚" };  
	private String[] item_message = { "大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢.", "大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢. ", "猪八戒爱泡妞、爱吃饭、爱偷懒", "大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢.", "大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢. ", "猪八戒爱泡妞、爱吃饭、爱偷懒", "大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢.", "猪八戒爱泡妞、爱吃饭、爱偷懒 ", "猪八戒爱泡妞、爱吃饭、爱偷懒", "猪八戒爱泡妞、爱吃饭、爱偷懒","大师兄，你好。你刚刚接了一个单，以在个人中心中联系客户喔，人家正等着你帮忙呢.", "猪八戒爱泡妞、爱吃饭、爱偷懒 ", "猪八戒", "沙和尚" }; 
	
	private String[] pc_path = { "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg ", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg","/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg ", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg" };   
	private RadioButton done_rbtn;
	private RadioButton not_done_rbtn;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		activityOders=this;
		//ListQuestionsShowShifu.List_questions_show(item_message,pc_path);
		new ListQuestionsShowOders().List_questions_show();
		ImageView back=(ImageView) findViewById(R.id.back);
		done_rbtn=(RadioButton) findViewById(R.id.done_rbtn);
		not_done_rbtn=(RadioButton) findViewById(R.id.not_done_rbtn);
		
		if(done_rbtn.isChecked()){
			not_done_rbtn.setTextColor(getResources().getColor(R.color.main_blue));
		}
		
		done_rbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				done_rbtn.setTextColor(getResources().getColor(R.color.white));
				not_done_rbtn.setTextColor(getResources().getColor(R.color.main_blue));
			}
		});
		not_done_rbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				done_rbtn.setTextColor(getResources().getColor(R.color.main_blue));
				not_done_rbtn.setTextColor(getResources().getColor(R.color.white));
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	
}
