package com.itlao.repairservice.register.ctrl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.itlao.repairservice.R;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.login.Login_Library_Activity;
import com.itlao.repairservice.publicq.ctrl.FabuActivity2;
import com.itlao.repairservice.publicq.ctrl.FabuActivity2.MyLocationListenner;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.repairservice.utils.Tools;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ActivityMasterRegister extends Activity {
	private ImageView ivMore;
	ImageView back;
	private RequestStringHandler readtaskListHandler;
	EditText et_nickname;
	EditText et_username;
	EditText et_password1;
	EditText et_password2;
	private String login_message;
	private ProgressDialog progressDialog = null;
	boolean isRegister = false;
	Button bt_register;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	String adrress;
	double lat;// 纬度
	double lng;// 经度
	private boolean isfirst = true;
	/* 组件 */
	private RelativeLayout switchAvatar;
	private ImageView faceImage;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	File filepatn;
	private EditText et_addr;
	Drawable drawable_head;
	RadioGroup group;
	private int sex = 1;
	private boolean ishead=false;
	private TextView tv_change;
	private EditText et_realname;
	private EditText et_idcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_master_register);
		initView();
		locationAddress();
		setHandler(this);
		setListener();
		switchAvatar = (RelativeLayout) findViewById(R.id.switch_face_rl);
		faceImage = (ImageView) findViewById(R.id.face);
		// 设置事件监听
		switchAvatar.setOnClickListener(listener);

	}

	private void initView() {

		back = (ImageView) findViewById(R.id.back);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password1 = (EditText) findViewById(R.id.et_password1);
		et_password2 = (EditText) findViewById(R.id.et_password2);
		et_realname = (EditText) findViewById(R.id.et_realname);
		et_idcard = (EditText) findViewById(R.id.et_idcard);
		bt_register = (Button) findViewById(R.id.bt_register);
		et_addr = (EditText) findViewById(R.id.et_addr);
		group = (RadioGroup) this.findViewById(R.id.radioGroup1);
		tv_change = (TextView) findViewById(R.id.tv_change);

	}

	private void setListener() {

		tv_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent=new Intent();
				intent.setClass(ActivityMasterRegister.this, ActivityRegister.class);
				startActivity(intent);
				finish();
				
			}
		});
		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) ActivityMasterRegister.this
						.findViewById(radioButtonId);
				Toast.makeText(ActivityMasterRegister.this, rb.getText(),
						Toast.LENGTH_SHORT).show();
				if (rb.getText().toString().trim().equals("男")) {

					sex = 1;
				} else {

					sex = 0;
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		bt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String etp1 = et_password1.getText().toString().trim();
				String etp2 = et_password2.getText().toString().trim();
				String nickname = et_nickname.getText().toString().trim();
				String username = et_username.getText().toString().trim();
				if (etp1.equals(etp2) && !etp1.equals("") && !etp2.equals("")
						&& !nickname.equals("")&& !username.equals("")&&ishead) {

					//sendResquest();
					dialogShow();
				} else {

					Toast.makeText(ActivityMasterRegister.this, "请填完整信息！",
							Toast.LENGTH_SHORT).show();
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
		dialog.setMessage("是否注册？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(ActivityMasterRegister.this, "注册...", Toast.LENGTH_SHORT)
						.show();
				
				sendResquest();
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

	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nickname", et_nickname.getText().toString().trim());
		params.put("username", et_username.getText().toString().trim());
		params.put("password", et_password1.getText().toString().trim());
		params.put("email", et_username.getText().toString().trim());
		params.put("sex", sex);// 男：1，女：0
		params.put("name", et_realname.getText().toString().trim());
		params.put("idcard", et_idcard.getText().toString().trim());
		params.put("address", et_addr.getText().toString().trim());
		params.put("longitude", lng);
		params.put("latitude", lat);
		params.put("file", filepatn);// 头像
		// params.put("h_s", drawable_head);

		String email = null;
		int sex = 0;
		String address = null;
		double longitude = 0;
		double latitude = 0;
		try {

			SendRequestUtil.post("AndroidCtrl.do?action=register", params,
					readtaskListHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {

			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				// login_message = JsonUtil.getString(json, "register");
				isRegister = json.getBoolean("register");

				// btnLogin.setEnabled(false);
				if (progressDialog == null)
					progressDialog = ProgressDialog.show(ActivityMasterRegister.this,
							"请稍等...", "正在验证用户名和密码...", true, true);
				else {
					if (progressDialog.isShowing())
						progressDialog.cancel();
					progressDialog.show();
				}

				if (isRegister) {
					progressDialog.cancel();
					Toast.makeText(ActivityMasterRegister.this, "注册成功！",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					progressDialog.cancel();
					Toast.makeText(ActivityMasterRegister.this, "注册失败！",
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	//

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog();
		}
	};
	

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Tools.hasSdcard()) {

								filepatn = new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME);
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(filepatn));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					/*
					 * File tempFile = new File(
					 * Environment.getExternalStorageDirectory() +
					 * IMAGE_FILE_NAME);
					 */
					startPhotoZoom(Uri.fromFile(filepatn));
				} else {
					Toast.makeText(ActivityMasterRegister.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			saveBitmapFile(photo);
			drawable_head = new BitmapDrawable(photo);
			faceImage.setImageDrawable(drawable_head);
			ishead=true;
		}
	}

	// Bitmap对象保存味图片文件
	public void saveBitmapFile(Bitmap bitmap) {
		// File file=new File("/mnt/sdcard/head/head.jpg");//将要保存图片的路径
		/*if(filepatn.exists()){
			
			filepatn.delete();
		}*/
		
		filepatn = new File(Environment.getExternalStorageDirectory(),
				IMAGE_FILE_NAME);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filepatn));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void locationAddress() {

		// 定位初始化

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;

			if (isfirst) {

				EditText et_addr = (EditText) findViewById(R.id.et_addr);
				adrress = location.getAddrStr();
				lat = location.getLatitude();// 纬度
				lng = location.getLongitude();// 经度
				et_addr.setText(adrress);
				isfirst = false;
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
