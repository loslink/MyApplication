package com.itlao.repairservice.pcenter.manito.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.findmaster.ctrl.ActivityOders;
import com.itlao.repairservice.findmaster.ctrl.Zhaoshifu_geren_Activity;
import com.itlao.repairservice.utils.CirclePhoto;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;





import android.net.Uri;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class DashenPersonalActivity extends Activity {

	
	/*@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashen_personal);
		//zhaoshifuActivity=this;
		//List_dashen_show.List_questions_show(this,item,pc_path);
		
	}*/

	private Intent getintent;
	private TextView nickname_tv;
	private TextView sex_tv;
	private TextView t_s_tv;
	private TextView qq_tv;
	private TextView address_tv;
	private TextView pro_det_tv;
	private ImageView back_iv;

	private long id;
	private String nickname;
	private int sex;
	private String address;
	private Double longitude;
	private Double latitude;
	private String p_n;
	private String qq;
	private String email;
	private int t_s;
	private int profession;
	private String pro_det;
	private int status;
	private String h_s;
	private byte[] by;
	private ImageView pc_head;
	private Button phoneBtn;
	private Button sms_btn;
	private Button btn_history;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashen_personal);

		iniview();
		getintent = this.getIntent();
		id = getintent.getLongExtra("id", -1);
		nickname = getintent.getStringExtra("nickname");
		sex = getintent.getIntExtra("sex", -1);
		address = getintent.getStringExtra("address");
		longitude = getintent.getDoubleExtra("longitude", -1);
		latitude = getintent.getDoubleExtra("latitude", -1);
		p_n = getintent.getStringExtra("p_n");
		qq = getintent.getStringExtra("qq");
		email = getintent.getStringExtra("email");
		t_s = getintent.getIntExtra("t_s", -1);
		profession = getintent.getIntExtra("profession", -1);
		pro_det = getintent.getStringExtra("pro_det");
		status = getintent.getIntExtra("profession", -1);
		
		try {
			by = EncodeDecodeUtil.decode(getintent.getStringExtra("h_s"));
			BitmapFactory.Options opts = new BitmapFactory.Options();
			Bitmap bmp = null;
			bmp = BitmapFactory.decodeByteArray(by, 0, by.length, opts);
			PhotoYasuo photo_Yasuo = new PhotoYasuo();
			Bitmap citcleBitmap=CirclePhoto.getCirclePhoto(photo_Yasuo.comp(bmp));
			//Drawable background = null;
			pc_head.setBackground(new BitmapDrawable(citcleBitmap ));
			//Bitmap bm_square = centerSquareScaleBitmap(photo_Yasuo.comp(bmp));//剪裁图片
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nickname_tv.setText("昵称："+nickname);
		if(sex==0){
			sex_tv.setText("性别：女");
		}else if(sex==1){
			sex_tv.setText("性别：男");
		}
		
		t_s_tv.setText("等级："+t_s);
		qq_tv.setText("QQ："+qq);
		address_tv.setText("地址："+address);
		pro_det_tv.setText("擅长内容："+pro_det);
		
		//返回键
		back_iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
		 });
		phoneBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+p_n));
					startActivity(intent);
					
				}
		 });
		sms_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Uri smsToUri = Uri.parse("smsto:"+p_n);
				Intent intent2 = new Intent(Intent.ACTION_SENDTO, smsToUri);
				intent2.putExtra("sms_body", "");
				startActivity(intent2);
			}
	 });
		btn_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final Intent intent = new Intent(
						DashenPersonalActivity.this,
						ActivityOders.class);
				
				DashenPersonalActivity.this.startActivity(intent);
			}
	 });
		
	}

	public void iniview() {
		
		sms_btn= (Button) findViewById(R.id.sms_btn);
		phoneBtn= (Button) findViewById(R.id.phoneBtn);
		pc_head= (ImageView) findViewById(R.id.pc_head);
		back_iv= (ImageView) findViewById(R.id.back_iv);
		nickname_tv = (TextView) findViewById(R.id.nickname_tv);
		sex_tv = (TextView) findViewById(R.id.sex_tv);
		t_s_tv = (TextView) findViewById(R.id.t_s_tv);
		qq_tv = (TextView) findViewById(R.id.qq_tv);
		address_tv = (TextView) findViewById(R.id.address_tv);
		pro_det_tv = (TextView) findViewById(R.id.pro_det_tv);
		btn_history= (Button) findViewById(R.id.btn_history);
	}

}
