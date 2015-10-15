package com.itlao.repairservice.pcenter.setting.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.drawable;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.pcenter.setting.SettingDialog;
import com.itlao.repairservice.utils.CirclePhoto;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class SettingActivity extends Activity {

	
	TextView email_tv;
	private byte[] by;
	private ImageView pchead_iv;
	TextView nicheng_content;
	TextView sex_content;
	TextView qq_content;
	TextView address_content;
	TextView phone_content;
	TextView shifustyle_content;
	TextView shanchang_content;
	RelativeLayout nicheng ;
	RelativeLayout setting_sex ;
	RelativeLayout setting_qq ;
	RelativeLayout setting_address ;
	RelativeLayout setting_phone ;
	RelativeLayout setting_shifustyle ;
	RelativeLayout setting_shanchang ;
	TextView tv_submit;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		 nicheng = (RelativeLayout) findViewById(R.id.setting_nicheng);
		 setting_sex = (RelativeLayout) findViewById(R.id.setting_sex);
		 setting_qq = (RelativeLayout) findViewById(R.id.setting_qq);
		 setting_address = (RelativeLayout) findViewById(R.id.setting_address);
		 setting_phone = (RelativeLayout) findViewById(R.id.setting_phone);
		 setting_shifustyle = (RelativeLayout) findViewById(R.id.setting_shifustyle);
		 setting_shanchang = (RelativeLayout) findViewById(R.id.setting_shanchang);
		 tv_submit=(TextView) findViewById(R.id.tv_submit);
		pchead_iv = (ImageView) findViewById(R.id.pchead_iv);
		email_tv = (TextView) findViewById(R.id.email_tv);
		email_tv.setText(MyCmd.email);
		

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
		Bitmap citcleBitmap = CirclePhoto.getCirclePhoto(photo_Yasuo.comp(bmp));
		pchead_iv.setImageBitmap(citcleBitmap);

		nicheng_content = (TextView) findViewById(R.id.nicheng_content);
		sex_content = (TextView) findViewById(R.id.sex_content);
		qq_content = (TextView) findViewById(R.id.qq_content);
		address_content = (TextView) findViewById(R.id.address_content);
		phone_content = (TextView) findViewById(R.id.phone_content);
		shifustyle_content = (TextView) findViewById(R.id.shifustyle_content);
		shanchang_content = (TextView) findViewById(R.id.shanchang_content);

		if(MyCmd.is_pro==0){
			
			setting_shifustyle.setVisibility(View.GONE);
			setting_shanchang.setVisibility(View.GONE);
		}
		nicheng_content.setText(MyCmd.nickname);
		sex_content.setText(MyCmd.sex == 0 ? "女" : "男");
		qq_content.setText(MyCmd.qq.equals("null")?"无":MyCmd.qq);
		address_content.setText(MyCmd.address);
		phone_content.setText(MyCmd.p_n.equals("null")?"无":MyCmd.p_n);
		shifustyle_content.setText(MyCmd.profession+"");
		shanchang_content.setText(MyCmd.pro_det.equals("null")?"无":MyCmd.pro_det);
		
		ImageView back = (ImageView) findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 设置圆形头像
		/*
		 * Bitmap bm = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.pc_1);
		 * user_head.setImageBitmap(CirclePhoto.getCirclePhoto(bm));
		 */
		 
		tv_submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Toast.makeText(MainActivity.mainActivity,
							"正在提交...", Toast.LENGTH_SHORT).show();

				}
		});
		// 昵称
		nicheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "昵称",
						R.id.nicheng_content);
				// nicheng_content.setText(nicheng);

			}
		});

		setting_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "性别",
						R.id.sex_content);
				// nicheng_content.setText(nicheng);

			}
		});
		setting_qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "QQ",
						R.id.qq_content);
				// nicheng_content.setText(nicheng);

			}
		});
		setting_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "常在地",
						R.id.address_content);
				// nicheng_content.setText(nicheng);

			}
		});
		setting_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "手机号",
						R.id.phone_content);
				// nicheng_content.setText(nicheng);

			}
		});
		setting_shifustyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "师傅类型",
						R.id.shifustyle_content);
				// nicheng_content.setText(nicheng);

			}
		});
		setting_shanchang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SettingDialog.Setting_text(SettingActivity.this, "擅长内容",
						R.id.shanchang_content);
				// nicheng_content.setText(nicheng);

			}
		});

	}

}