package com.itlao.repairservice.findmaster.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.LayoutZiDingYi;
import com.itlao.repairservice.R;
import com.itlao.repairservice.LayoutZiDingYi.OnScrollListener;
import com.itlao.repairservice.R.drawable;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.findmaster.ListShifuShow;
import com.itlao.slidingmenu.SlidingMenu;






import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class ZhaoShiFuActivity extends Activity  {

	public static ZhaoShiFuActivity zhaoshifuActivity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item = { "大师兄", "二师兄 ", "三师兄", "大师兄", "大师兄 ", "大师兄", "大师兄", "大师兄 ", "大师兄", "大师兄","大师兄", "大师兄 ", "猪八戒", "沙和尚" };  
	
	private String[] pc_path = { "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg ", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg","/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/bing.jpg ", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg" };   
	//
	private static final String TAG = "ChrisSlideMenu"; 
	private LinearLayout mainLayout; 
	private RelativeLayout leftLayout; 
	private RelativeLayout rightLayout; 
	private LayoutZiDingYi layoutSlideMenu; 
	private ListView mListMore; 

	private ImageView ivMore; 
	private ImageView ivSettings; 
	private GestureDetector mGestureDetector; 

	private static final int SPEED = 30; 
	private boolean bIsScrolling = false; 
	private int iLimited = 0; 
	private int mScroll = 0; 
	private View mClickedView = null; 
	private SlidingMenu slidingMenu;

	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_shifu);
		zhaoshifuActivity=this;
		
		
		//返回键
        ImageView iv_back=(ImageView) findViewById(R.id.back_shifu);
		 iv_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
		 });
		 
		 initView();
		 initSlidingMenu();
		 
		 ListShifuShow lsfs=new ListShifuShow();
		 lsfs.List_questions_show(slidingMenu);
		
	}


	private void initView(){
		
		
		
		ivMore = (ImageView) findViewById(R.id.ivMore); 
		
		//TextView tv=(TextView) findViewById(R.id.tv);
				ivMore.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						slidingMenu.toggle(true);
					}
				});
		} 
	
	/**
	 * 初始化左右滑动菜单
	 * 
	 * @author hezheng
	 */
	public void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidth(3);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		slidingMenu.setBehindOffset(200);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// slidingMenu.setMenu(R.layout.home_right_main);
		slidingMenu.setMenu(R.layout.fragment_lists_sliding);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}
}
