package com.itlao.repairservice.pcenter.contact.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.pcenter.contact.Contacts;
import com.itlao.repairservice.utils.GetPhoneContacts;
import com.itlao.repairservice.utils.GetPhoneContacts.SamContact;





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

public class PhoneContactsActivity extends Activity {

	//public static Iam_kehu_Activity kehu_Activity;///storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg
	private String[] item_shoucang_yaoqing = {"邀请","收藏","邀请","收藏","邀请","收藏"};
	private String[] item_phone = {"188264110002","18826410994","188264110002","18826410994","188264110002","18826410994"};
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		//kehu_Activity=this;
		ImageView back=(ImageView) findViewById(R.id.back);
		//获取手机和sim通讯录
		ArrayList<SamContact> arrayList=GetPhoneContacts.getAllContacts(this);


		/*//方法1
        Iterator it1 = arrayList.iterator();
        while(it1.hasNext()){
            System.out.println(it1.next());
        }
        //方法2
        for(Iterator it2 = arrayList.iterator();it2.hasNext();){
             System.out.println(it2.next());
        }*/
        //方法3
        /*for(SamContact tmp: arrayList){
        	
        	
            System.out.println(tmp.phone);
        }*/
        /*//方法4
        for(int i = 0;i < arrayList.size(); i ++){
            System.out.println(arrayList.get(i));
        }*/
		
		Contacts.List_questions_show(this,arrayList);
		
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	
}
