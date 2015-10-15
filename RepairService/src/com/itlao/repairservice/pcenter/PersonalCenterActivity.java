package com.itlao.repairservice.pcenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.login.Login_Library_Activity;
import com.itlao.repairservice.pcenter.contact.ctrl.PhoneContactsActivity;
import com.itlao.repairservice.pcenter.imcustomer.ctrl.IamKehuActivity;
import com.itlao.repairservice.pcenter.immaster.ctrl.IamShifuActivity;
import com.itlao.repairservice.pcenter.manito.ctrl.DashenShoucangActivity;
import com.itlao.repairservice.pcenter.msgcenter.ctrl.MessageCenterActivity;
import com.itlao.repairservice.pcenter.setting.ctrl.SettingActivity;
import com.itlao.repairservice.utils.CirclePhoto;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;
import com.itlao.utils.util.JsonUtil;

import android.os.Bundle;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class PersonalCenterActivity extends Activity {

	public static PersonalCenterActivity my_Activity;// /storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item = { "大师兄", "二师兄 ", "三师兄", "大师兄", "大师兄 ", "大师兄",
			"大师兄", "大师兄 ", "大师兄", "大师兄", "大师兄", "大师兄 ", "猪八戒", "沙和尚" };

	private String[] pc_path = { "/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/long.jpg ",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/long.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/long.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/bing.jpg",
			"/storage/sdcard1/tieba/bing.jpg ",
			"/storage/sdcard1/tieba/long.jpg",
			"/storage/sdcard1/tieba/bing.jpg" };
	RelativeLayout shifu;
	RelativeLayout dengji;
	RelativeLayout kehu;
	RelativeLayout dashen_shoucang;
	RelativeLayout tongxvnlu;
	RelativeLayout message_center;
	RelativeLayout setting;
	Button finish_btn;
	TextView email_tv;
	private RatingBar ratingBar1;
	private ImageView pchead_iv;
	private byte[] by;
	private Button login_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		my_Activity = this;
		// List_shifu_show.List_questions_show(item,pc_path);
		shifu = (RelativeLayout) findViewById(R.id.center_personal_shifu);
		dengji = (RelativeLayout) findViewById(R.id.center_personal_dengji);
		kehu = (RelativeLayout) findViewById(R.id.center_personal_kehu);
		dashen_shoucang = (RelativeLayout) findViewById(R.id.center_personal_dashenshoucang);
		tongxvnlu = (RelativeLayout) findViewById(R.id.center_personal_tongxvnlu);
		message_center = (RelativeLayout) findViewById(R.id.center_personal_massagecenter);
		setting = (RelativeLayout) findViewById(R.id.center_personal_setting);
		finish_btn = (Button) findViewById(R.id.finish_btn);
		login_btn = (Button) findViewById(R.id.login_btn);
		email_tv = (TextView) findViewById(R.id.email_tv);
		pchead_iv = (ImageView) findViewById(R.id.pchead_iv);

		ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar1.setMax(10);
		ratingBar1.setProgress(MyCmd.t_s);// 师傅等级
		/*
		 * ratingBar1.setOnRatingBarChangeListener(new
		 * RatingBar.OnRatingBarChangeListener() {
		 * 
		 * @Override public void onRatingChanged(RatingBar ratingBar, float
		 * rating, boolean fromUser) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * });
		 */

		if (MyCmd.id == -1) {

			finish_btn.setVisibility(View.GONE);

		} else {

			login_btn.setVisibility(View.GONE);
			PhotoYasuo photo_Yasuo = new PhotoYasuo();
			try {
				by = EncodeDecodeUtil.decode(MyCmd.h_s);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 解决加载图片 内存溢出的问题
			// Options 只保存图片尺寸大小，不保存图片到内存
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
			opts.inSampleSize = 2; // 质量缩小8倍
			Bitmap bmp = null;
			bmp = BitmapFactory.decodeByteArray(by, 0, by.length, opts);
			Bitmap citcleBitmap = CirclePhoto.getCirclePhoto(photo_Yasuo
					.comp(bmp));
			pchead_iv.setImageBitmap(citcleBitmap);

			email_tv.setText(MyCmd.email);

		}
		if (MyCmd.is_pro == 0) {

			shifu.setVisibility(View.GONE);
			dengji.setVisibility(View.GONE);
		}

		final Intent intent_shifu = new Intent(this, IamShifuActivity.class);
		final Intent intent_kehu = new Intent(this, IamKehuActivity.class);
		final Intent intent_dashen_shoucang = new Intent(this,
				DashenShoucangActivity.class);
		final Intent intent_tongxvnlu = new Intent(this,
				PhoneContactsActivity.class);
		final Intent intent_message_center = new Intent(this,
				MessageCenterActivity.class);
		final Intent intent_setting = new Intent(this, SettingActivity.class);
		final Intent intent_login = new Intent(this,
				Login_Library_Activity.class);

		// 返回键
		ImageView iv_back = (ImageView) findViewById(R.id.back_per_center);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		// 注销
		finish_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//onBackPressed();
				dialogShow();
				
			}
		});

		// 登录
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(intent_login);
			}
		});
		shifu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(intent_shifu);
			}
		});

		/*
		 * dengji.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * startActivity(intent_shifu); } });
		 */
		kehu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyCmd.id == -1) {

					startActivity(intent_login);
				}else{
					
						startActivity(intent_kehu);
				}
			
			}
		});
		dashen_shoucang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyCmd.id == -1) {

					startActivity(intent_login);
				}else{
					
					startActivity(intent_dashen_shoucang);
				}
				
			}
		});
		tongxvnlu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyCmd.id == -1) {

					startActivity(intent_login);
				}else{
					
					startActivity(intent_tongxvnlu);
				}
				
			}
		});
		message_center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyCmd.id == -1) {

					startActivity(intent_login);
				}else{
					
					startActivity(intent_message_center);
				}
				
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MyCmd.id == -1) {

					startActivity(intent_login);
				}else{
					
					startActivity(intent_setting);
				}
				
			}
		});
	}

	/**
	 * dialog
	 */
	public void dialogShow() {

		android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setMessage("是否退出账号登录？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			private SharedPreferences sharedPreferences;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				sharedPreferences = getSharedPreferences("userInfo",
						Context.MODE_WORLD_WRITEABLE);

				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				
				Intent intent = new Intent(); 
				intent.setClass(PersonalCenterActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
				startActivity(intent);
			}
		}).create();
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create();

		dialog.show();
	}
}
