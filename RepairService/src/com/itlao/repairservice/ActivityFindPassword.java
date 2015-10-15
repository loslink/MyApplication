package com.itlao.repairservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.itlao.repairservice.register.ctrl.ActivityMasterRegister;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.utils.http.handler.RequestStringHandler;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ActivityFindPassword extends Activity {

	private ImageView iv_back;
	private Button bt_password;
	private EditText et_email;
	private RequestStringHandler readtaskListHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_find_password);
		initView();
		setHandler(this);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		bt_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(!et_email.getText().toString().trim().equals("")){
					
					sendResquest();
				}else{
					
					Toast.makeText(ActivityFindPassword.this, "邮箱不能为空！",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
	}

	
	
	private void initView() {

		iv_back = (ImageView) findViewById(R.id.iv_back);
	bt_password = (Button) findViewById(R.id.bt_password);
	et_email = (EditText) findViewById(R.id.et_email);
	}
	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("email", et_email.getText().toString().trim());
		

		try {

			SendRequestUtil.post("AndroidCtrl.do?action=getpassword", params,
					readtaskListHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {

			private boolean isSend=false;

			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				// login_message = JsonUtil.getString(json, "register");
				isSend = json.getBoolean("isSend");

				
				if (isSend) {
					
					Toast.makeText(ActivityFindPassword.this, "密码已经发送到"+et_email.getText().toString().trim()+"邮箱，请查看！",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					
					Toast.makeText(ActivityFindPassword.this, "不存在此邮箱！",
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

}
