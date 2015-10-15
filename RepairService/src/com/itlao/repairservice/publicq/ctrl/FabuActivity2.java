package com.itlao.repairservice.publicq.ctrl;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.testpic.Bimp;
import com.example.testpic.PublishedActivity;
import com.example.utils.recordvoice.util.AudioItemBean;

import com.itlao.cmd.Clazz;
import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.R;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;

public class FabuActivity2 extends PublishedActivity {

	Spinner spinner02;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	
	private EditText et_detail;
	private TextView send;
	RequestHandler readListHandler;
	List<String> bmppaths = new ArrayList<String>();
	String pathStr = null;
	private Integer clazz;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	String adrress;
	double lat;// 纬度
	double lng;// 经度
	private String[] quetions_style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		locationAddress();
		Iterator<String> iterator=Clazz.tyleslist.iterator();
		int len = 0;
		len=Clazz.tyleslist.size();
		quetions_style=new String[len] ;
		int i=0;
		while(iterator.hasNext()){
            //System.out.println(iterator.next());
            quetions_style[i]=iterator.next();
            i++;
        }
		
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, quetions_style);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		setListener();
		setHandler(this);

	}

	public void setHandler(Context context) {
		readListHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				if (json.getBoolean("saved")) {

					Toast.makeText(FabuActivity2.this, "发布成功",
							Toast.LENGTH_SHORT).show();
					Bimp.bmp.clear();//清除图片列表的图片
					FabuActivity2.this.finish();
				}
				/*
				 * login_message = JsonUtil.getString(json, "login");
				 * System.out.print(login_message);
				 */
			}
		};
	}

	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("word", et_detail.getText().toString());
		params.put("clazz", clazz);
		params.put("o_id", MyCmd.getId());
		// Date now = new Date();
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		params.put("p_t", curDate);
		params.put("address", adrress);
		params.put("longitude", lng);
		params.put("latitude", lat);
		params.put("status", 1);// 求助
		params.put("voice", new File(pathStr));
		int i = 0;

		Iterator iterator = bmppaths.iterator();
		while (iterator.hasNext()) {

			System.out.println("图片" + i);
			// bmp_save((byte[])iterator.next(),
			// "D:/RepairResource/questions/"+id+"/","D:/RepairResource/questions/"+id+"/"+id+"_"+index+".jpg");
			// index++;
			File file = new File((String) iterator.next());
			params.put("file" + i, file);
			i = i + 1;
		}
		/*
		 * for(String bmp : bmppaths){ File file=new File(bmp);
		 * params.put("file"+i, file); i = i + 1; }
		 */
		try {

			// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
			SendRequestUtil.post("AndroidCtrl.do?action=publicQuestion",
					params, readListHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListener() {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				clazz = Clazz.tylesmap.get(quetions_style[arg2]);
						
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (getBitmap().size() == 4 && pathStr != null
						&& !et_detail.getText().toString().trim().equals("")) {

					dialogShow();

				} else {

					Toast.makeText(FabuActivity2.this, "请填完整信息",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initView() {

		et_detail = (EditText) findViewById(R.id.et_detail);
		send = (TextView) findViewById(R.id.activity_selectimg_send);
		spinner = (Spinner) findViewById(R.id.Spinner01);
	}

	/**
	 * dialog
	 */
	public void dialogShow() {

		android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setMessage("是否发布问题？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(FabuActivity2.this, "发布...", Toast.LENGTH_SHORT)
						.show();
				bmppaths.clear();
				for (String bmp : getBitmapPaths()) {

					bmppaths.add(bmp);
				}
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

	@Override
	public void ItemBeanCallBack(AudioItemBean itemBean) {
		// TODO Auto-generated method stub
		super.ItemBeanCallBack(itemBean);
		pathStr = itemBean.getFilePath();
		int Duration = itemBean.getDuration();
		System.out.print(pathStr);
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

			TextView adrr_tv = (TextView) findViewById(R.id.addr_tv);
			
			 adrress=location.getAddrStr();
			 lat = location.getLatitude();// 纬度
			 lng = location.getLongitude();// 经度
			adrr_tv.setText("当前位置："+adrress);

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
