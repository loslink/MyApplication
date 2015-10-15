package com.itlao.repairservice.pcenter.msgcenter.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.ListQuestionsShowMessage;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;

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
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class MessageCenterActivity extends Activity {

	// public static Iam_kehu_Activity
	// kehu_Activity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item_message = {
			"您好，感谢成为我们的一员，么么哒，您好，感谢成为我们的一员!",
			"您好，您发布的问题已经被云淡风轻师傅接单了，请耐心等待！", "您好，您发布的问题已经被维修能手师傅接单了，请耐心等待！", "您好，您发布的问题已经被八卦掌师傅接单了，请耐心等待！",
			 };

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_center);
		// kehu_Activity=this;
		ListQuestionsShowMessage.List_questions_show(this, item_message);
		ImageView back = (ImageView) findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
