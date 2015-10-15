package com.itlao.repairservice.login;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

//import cn.sharesdk.demo.tpl.ThirdPartLogin;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.qzone.QZone;

import com.itlao.Database.DBManager;
import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.ActivityFindPassword;
import com.itlao.repairservice.R;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.register.ctrl.ActivityRegister;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.utils.http.Request;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mytwitter.acitivity.LoginActivity;

public class Login_Library_Activity extends LoginActivity {

	static Login_Library_Activity activity;
	EditText et_username;
	EditText et_password;
	MainActivity mainActivity;
	DataOutputStream dos;
	DataInputStream dis;
	Socket socket;
	String login_message;
	static String username = "";
	static String password = "";
	public MyHandler myHandler = new MyHandler();
	byte[] send_pack = new byte[76];
	DBManager dbManager;
	RequestHandler readtaskListHandler;
	private SharedPreferences sharedPreferences;
	private ProgressDialog progressDialog = null;
	private TextView register_link;
	private Button gointo_btn;
	private TextView lost_pass;
	private ImageView qq;
	private ImageView sina;
//	ThirdPartLogin third;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_login);
		initView();
//		 third=new ThirdPartLogin(Login_Library_Activity.this);
		activity = this;

		sharedPreferences = getSharedPreferences("userInfo",
				Context.MODE_WORLD_WRITEABLE);

		if (sharedPreferences.getBoolean("isRember", false)) {
			et_username.setText(sharedPreferences.getString("userName", ""));
			et_password.setText(sharedPreferences.getString("passWord", ""));

		}

		setHandler(Login_Library_Activity.this);
		setListener();
	}// 底部

	private void initView(){

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		register_link=(TextView) findViewById(R.id.register_link);
		gointo_btn=(Button) findViewById(R.id.gointo_btn);
		lost_pass=(TextView) findViewById(R.id.lost_pass);
		qq = (ImageView) findViewById(R.id.qq);
		sina = (ImageView) findViewById(R.id.sina);
	} 
	
	private void setListener(){

		qq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
//				Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//				third.authorize(qzone);
			}
		});
		
		sina.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//新浪微博
//				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
//				third.authorize(sina);
//				
			}
		});
		lost_pass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent();
				intent.setClass(Login_Library_Activity.this, ActivityFindPassword.class);
				startActivity(intent);
			}
		});
		register_link.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent();
				intent.setClass(Login_Library_Activity.this, ActivityRegister.class);
				startActivity(intent);
			}
		});
		gointo_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/*//储存个人信息
				MyCmd.setId(-1);
				MyCmd.setPassword(null);
				MyCmd.setName(null);
				MyCmd.setNickname(null);
				MyCmd.setSex(-1);
				MyCmd.setAddress(null);
				MyCmd.setLongitude(null);
				MyCmd.setLatitude(null);
				MyCmd.setH_s(null);
				MyCmd.setP_n(null);
				MyCmd.setQq(null);
				MyCmd.setEmail(null);
				MyCmd.setIs_pro(0);
				MyCmd.setT_s(0);
				MyCmd.setProfession(-1);
				MyCmd.setPro_det(null);
				MyCmd.setStatus(1);
				//MyCmd.setDirty(json.getInt("dirty"));

				
				Intent intent = new Intent(Login_Library_Activity.this,
						MainActivity.class);
				//intent.putExtra("username", username);
				
				
				startActivity(intent);
				Message msg = new Message();
				msg.what = 2;
				msg.obj = "游客登录";
				myHandler.sendMessage(msg);
				Login_Library_Activity.this.finish();*/
			}
		});
	} 
	
	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				login_message = JsonUtil.getString(json, "login");
				System.out.print(login_message);

				// btnLogin.setEnabled(false);
				if (progressDialog == null)
					progressDialog = ProgressDialog.show(
							Login_Library_Activity.this, "请稍等...",
							"正在验证用户名和密码...", true, true);
				else {
					if (progressDialog.isShowing())
						progressDialog.cancel();
					progressDialog.show();
				}

				
				try {

					if (login_message.equals("success") == true) {
						
						//储存个人信息
						
						MyCmd.setId(json.getLong("id"));
						MyCmd.setPassword(json.getString("password"));
						MyCmd.setName(json.getString("name"));
						MyCmd.setNickname(json.getString("nickname"));
						MyCmd.setSex(json.getInt("sex"));
						MyCmd.setAddress(json.get("address").equals("NULL")? null : json.getString("address"));
						MyCmd.setLongitude(json.getDouble("longitude"));
						MyCmd.setLatitude(json.getDouble("latitude"));
						MyCmd.setH_s(json.getString("h_s"));
						MyCmd.setP_n(json.get("p_n").equals("NULL")? null : json.getString("p_n"));
						MyCmd.setQq(json.get("qq").equals("NULL")? null : json.getString("qq"));
						MyCmd.setEmail(json.getString("email"));
						MyCmd.setIs_pro(json.getInt("is_pro"));
						MyCmd.setT_s(json.getInt("t_s"));
						MyCmd.setProfession(json.get("profession").equals("NULL")? null : json.getInt("profession"));
						MyCmd.setPro_det(json.get("pro_det").equals("NULL")? null : json.getString("pro_det"));
						MyCmd.setStatus(json.get("status").equals("NULL")? null : json.getInt("status"));
						//MyCmd.setDirty(json.getInt("dirty"));

						Intent intent = new Intent(Login_Library_Activity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
						intent.putExtra("login", true);
						intent.putExtra("username", username);
						startActivity(intent);
						Message msg = new Message();
						msg.what = 2;
						msg.obj = "登录成功";
						myHandler.sendMessage(msg);
						Login_Library_Activity.this.finish();

						// 保存账号密码
						Editor editor = sharedPreferences.edit();
						editor.putString("userName", username);
						editor.putString("passWord", password);
						editor.putBoolean("isRember", true);
						editor.commit();
						// sharedPreferences.edit().putBoolean("isAutoLogin",
						// true).commit();

						// loop = false;
					}
					if (login_message.equals("failure") == true) {

						Message msg = new Message();
						msg.what = 1;
						msg.obj = "账号不存在或者密码错误";
						myHandler.sendMessage(msg);

						// loop = false;
					}
					if (login_message.equals("existed") == true) {

						Message msg = new Message();
						msg.what = 3;
						msg.obj = "账号已在线，不能重登！";
						myHandler.sendMessage(msg);

						// loop = false;
					}
				} catch (Exception e) {
					e.getMessage();
					System.out.print(e.getMessage());
					e.printStackTrace();
				}
			}

		};
	}

	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", et_username.getText().toString().trim());
		params.put("password", et_password.getText().toString().trim());
		try {
			// String
			// url="http://172.27.35.1:8080/RepairServer/views/data/Test.do?action=main";
			// String
			// url="http://172.22.236.1:8080/RepairServer/android/LoginCtrl.do?action=login";
			// String url =
			// "http://172.21.8.1:8080/RepairServer/android/LoginCtrl.do?action=login";
			// Request.post(url, params, readtaskListHandler, null);

			// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
			SendRequestUtil.post("LoginCtrl.do?action=login", params,
					readtaskListHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		// dbManager.closeDB();
	}

	public void click(View view) {

		checkNetwork(this);
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();

		// boolean loop = true;
		if (username.equals("") || password.equals("")) {
			Message msg = new Message();
			msg.what = 4;
			msg.obj = "账户或密码不能为空";
			myHandler.sendMessage(msg);
			// loop = false;
		} else if (username.length() < 3) {

			/*Message msg = new Message();
			msg.what = 4;
			msg.obj = "输入账户错误";
			myHandler.sendMessage(msg);*/
			// loop = false;
		}else{
			
			sendResquest();// 向服务器发送请求
		}

		
	}

	public void checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			NetworkInfo networkinfo = cm.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()) {
				new AlertDialog.Builder(this)
						.setTitle("网络设置提示")
						.setMessage("网络不可用，是否现在设置网络？")
						.setPositiveButton("设置",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												android.provider.Settings.ACTION_WIRELESS_SETTINGS);
										startActivity(intent);
									}
								}).setNegativeButton("取消", null).show();
			}
		}
	}

	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String string = (String) msg.obj;
				Toast.makeText(Login_Library_Activity.this, string,
						Toast.LENGTH_SHORT).show();
				break;

			case 2:
				String string_2 = (String) msg.obj;
				Toast.makeText(Login_Library_Activity.this, string_2,
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				String string_3 = (String) msg.obj;
				Toast.makeText(Login_Library_Activity.this, string_3,
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				String string_4 = (String) msg.obj;
				Toast.makeText(Login_Library_Activity.this, string_4,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}
