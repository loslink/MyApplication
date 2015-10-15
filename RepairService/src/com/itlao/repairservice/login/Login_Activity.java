package com.itlao.repairservice.login;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import com.itlao.Database.DBManager;
import com.itlao.Database.Messages;
import com.itlao.Database.User;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.utils.GetPhoneContacts.SamContact;
import com.itlao.utils.http.Request;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Activity extends Activity {

	static Login_Activity activity;
	EditText et_username;
	EditText et_password;
	MainActivity mainActivity;
	DataOutputStream dos;
	DataInputStream dis;
	Socket socket;
	String login_message;
	static String username="";
	static String password="";
	public MyHandler myHandler=new MyHandler();
	private String password_saved;
	private String username_saved;
	byte[] send_pack=new byte[76];
	DBManager dbManager;
	RequestHandler readtaskListHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		et_username=(EditText) findViewById(R.id.et_username);
		et_password=(EditText) findViewById(R.id.et_password);
		activity=this;
		
		
		String str_issavepsw="false";//是否保存密码
		//保存密码
		if(str_issavepsw.equals("true")) {
			
			final Intent intent=new Intent(Login_Activity.this,MainActivity.class );
			startActivity( intent);
			Login_Activity.activity.finish();
		}
		
		//获取CheckBox实例
		CheckBox cb = (CheckBox)this.findViewById(R.id.checkBox1);
		//绑定监听器
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		            
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(Login_Activity.this, 
		                        arg1?"选中了":"取消了选中", Toast.LENGTH_LONG).show();
						
						String fileDirPath = android.os.Environment  
					            .getExternalStorageDirectory().getAbsolutePath()// 得到外部存储卡的数据库的路径名     /storage/sdcard0/RepairService/user/info/informations.xml
					            + "/RepairService/user/info/informations.xml";// 我要存储的目录 
						File file = new File(fileDirPath); 
						
						//JiexiZiliao jiexiZiliao=new JiexiZiliao(file);
						if(arg1) {
							
							//jiexiZiliao.SavedPassWord();
						}else {
							//jiexiZiliao.notSavedPassWord();
						}
					}
		        });
	
		setHandler(Login_Activity.this);
		
	}//底部
	

	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);
				
				login_message = JsonUtil.getString(json, "login");
				System.out.print(login_message);
				
			}
			 
		};
	}
	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", et_username.getText().toString().trim());
		params.put("password", et_password.getText().toString().trim());
		try {
			//String url="http://172.27.35.1:8080/RepairServer/views/data/Test.do?action=main";
			//String url="http://172.22.236.1:8080/RepairServer/android/LoginCtrl.do?action=login";
			String url="http://192.168.191.1:18080/RepairServer/android/LoginCtrl.do?action=login";
			Request.post(url, params, readtaskListHandler,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //应用的最后一个Activity关闭时应释放DB  
        //dbManager.closeDB();  
    }  
	
public void click(View view) {
		
	    sendResquest();//向服务器发送请求
		username=et_username.getText().toString().trim();
		password=et_password.getText().toString().trim();
		
		/*new Thread() {
			
			public void run() {*/
				boolean loop=true;
				if(username.equals("")||password.equals("")) {
					Message msg=new Message();
					msg.what=4;
					msg.obj="账户或密码不能为空";
					myHandler.sendMessage(msg);
					loop=false;
					}else if(username.length()<3) {
						
						Message msg=new Message();
						msg.what=4;
						msg.obj="输入账户错误";
						myHandler.sendMessage(msg);
						loop=false;
					}
				/*//输入账号密码和保存的一致
				if("劳其恋".equals(username)&&"123".equals(password)) {
					
					final Intent intent=new Intent(Login_Activity.this,MainActivity.class );
					startActivity( intent);
					Login_Activity.activity.finish();
				}
				*/
				
					try {
						
						if(login_message.equals("success")==true) {
							
							Intent intent = new Intent(Login_Activity.this, MainActivity.class);
							intent.putExtra("username", username);
							startActivity(intent);
							Message msg=new Message();
							msg.what=2;
							msg.obj="登录成功";
							myHandler.sendMessage(msg);
							Login_Activity.this.finish();
							/*dis.close();
							dos.close();
							socket.close();*/
							loop=false;
						}
						if(login_message.equals("failure")==true) {
							
							Message msg=new Message();
							msg.what=1;
							msg.obj="账号不存在或者密码错误";
							myHandler.sendMessage(msg);
							/*dis.close();
							dos.close();
							socket.close();*/
							loop=false;
						}
						if(login_message.equals("existed")==true) {
							
							Message msg=new Message();
							msg.what=3;
							msg.obj="账号已在线，不能重登！";
							myHandler.sendMessage(msg);
							/*dis.close();
							dos.close();
							socket.close();*/
							loop=false;
						}
					} catch (Exception e) {
						String string=e.getMessage();
						System.out.print(e.getMessage());
						e.printStackTrace();
					}
				
			/*};
		}.start();*/
		
	}


	/*public void click_sign(View view) {
		
		Intent intent = new Intent(Login_Activity.this, Sign_Activity.class);
		//intent.putExtra("username", username);
		Login_Activity.this.finish();
		startActivity(intent);
	}*/
	public class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String string=(String) msg.obj;
				Toast.makeText(Login_Activity.this, string,Toast.LENGTH_SHORT).show();
				break;
				
			case 2:
				String string_2=(String) msg.obj;
				Toast.makeText(Login_Activity.this, string_2,Toast.LENGTH_SHORT).show();
				break;
			case 3:
				String string_3=(String) msg.obj;
				Toast.makeText(Login_Activity.this,string_3 ,Toast.LENGTH_SHORT).show();
				break;
			case 4:
				String string_4=(String) msg.obj;
				Toast.makeText(Login_Activity.this,string_4 ,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}
