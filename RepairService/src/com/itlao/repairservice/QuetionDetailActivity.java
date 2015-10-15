package com.itlao.repairservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.example.testpic.PhotoActivity;
import com.example.testpic.PublishedActivity;
import com.itlao.cmd.MyCmd;
import com.itlao.map.LocationMap;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.publicq.ctrl.FabuActivity2;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.repairservice.utils.ShareUtil;
import com.itlao.swithcer.GuideGallery;
import com.itlao.swithcer.ImageAdapter;

import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.CircleProgress;
import com.itlao.utils.util.JsonUtil;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class QuetionDetailActivity extends Activity {

	private int STATE = 3;
	public List<String> urls;
	public GuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	public LinearLayout pointLinear = null;
	Uri uri;
	Intent intent;
	int gallerypisition = 0;
	private int mark = 0;
	final MediaPlayer mediaPlayer = new MediaPlayer();
	RequestHandler readtaskListHandler;
	Intent getintent;
	Handler handler = new Handler();
	CircleProgress mCircleProgressBar;
	private boolean isPause = false;
	private byte[] soundByte;
	private TextView word_tv;
	private Button recei_order_btn;
	private Button finish_feek_btn;
	private boolean isFeedback = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getintent = this.getIntent();
		String str = getintent.getCharSequenceExtra("status").toString();
		STATE = Integer.parseInt(getintent.getCharSequenceExtra("status")
				.toString());
		try {
			isFeedback=getintent.getBooleanExtra("isFeedback", false);
		} catch (Exception e) {
			
		}

//状态截止
		// List_shifu_show.List_questions_show(item,pc_path);
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};

		setHandler(this);
		Map<String, Object> params = new HashMap<String, Object>();
		// Intent intent=this.getIntent();

		params.put("id", getintent.getCharSequenceExtra("id"));
		// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
		SendRequestUtil.post("HomeCtrl/questionDetail", params,
				readtaskListHandler);

		timeThread.start();
		//init();
	}

	// 进度条显示
	Runnable r = new Runnable() {

		@Override
		public void run() {
			// while(isPause==false) {
			try {
				mCircleProgressBar.setMainProgress(((mediaPlayer
						.getCurrentPosition()) * 100)
						/ (mediaPlayer.getDuration()));

				if (mediaPlayer.isPlaying()) {

					handler.postDelayed(r, 10); // 延迟10毫秒执行一次r线程。并把子线程放到UI线程里
				} else {
					mCircleProgressBar.setBackgroundResource(R.drawable.play);

					mCircleProgressBar.setMainProgress(0);
					mark--;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			// }
		}
	};
	private RequestStringHandler readOderHandler;

	/**
	 * 字节播放音频
	 * 
	 */
	private void mp3FromByte(byte[] mp3SoundByteArray) {
		try {
			// create temp file that will hold byte array
			File tempMp3 = File
					.createTempFile("kurchina", "mp3", getCacheDir());
			tempMp3.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempMp3);
			fos.write(mp3SoundByteArray);
			fos.close();

			// MediaPlayer mediaPlayer = new MediaPlayer();

			FileInputStream fis = new FileInputStream(tempMp3);
			mediaPlayer.setDataSource(fis.getFD());

			mediaPlayer.prepare();
			// mediaPlayer.start();
		} catch (IOException ex) {
			String s = ex.toString();
			ex.printStackTrace();
		}
	}

	/**
	 * 网络获取的数据在这里获取处理
	 * 
	 */
	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				JSONObject data = json.getJSONObject("Question");
				System.out.println(data.toString());
				String pictures1 = data.getString("pictures");
				org.json.JSONArray pictures=new org.json.JSONArray(pictures1);

				try {
					// String soundString=JsonUtil.getString(data, "sound");
					String sound = data.getString("sound");// 获取声音
					soundByte = EncodeDecodeUtil.decode(sound);
					mp3FromByte(soundByte);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HashMap<String, Object> map = new HashMap<String, Object>(); // "/storage/sdcard1/tieba/bing.jpg"
				int ii = pictures.length();
				for (int i = 0; i < pictures.length(); i++) {
					String str = pictures.getString(i);
					/*
					 * JSONObject jb=pictures.getJSONObject(i); String
					 * pic=jb.toString();
					 */

					map.put(i + "", str);

				}

				/*// 图片触摸事件
				images_ga.setImageActivity(QuetionDetailActivity.this);

				ImageAdapter imageAdapter = new ImageAdapter(
						QuetionDetailActivity.this, map);
				images_ga.setAdapter(imageAdapter);
				*/
				// 完成
				if (STATE == 3) {
					// 完成
					setContentView(R.layout.activity_question_details_complete);

					word_tv = (TextView) findViewById(R.id.word_tv);
					word_tv.setText("问题描述：" + data.getString("word"));
					final ImageView play_complete = (ImageView) findViewById(R.id.play_complete);
					ImageView backbt_complete = (ImageView) findViewById(R.id.back_complete);
					TextView time_tv = (TextView) findViewById(R.id.time_tv);
					ImageView share_iv = (ImageView) findViewById(R.id.share_iv);
					mCircleProgressBar = (CircleProgress) findViewById(R.id.roundBar1);
					mCircleProgressBar.setMainProgress(0);
					time_tv.setText("完成于" + data.getString("sT"));
					backbt_complete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onBackPressed();
						}
					});
					share_iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ShareUtil su = new ShareUtil(QuetionDetailActivity.this);
							su.initSharePopupWindow(v);
						}
					});
					mCircleProgressBar.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 播放录音
							if (mark == 0) {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.pause);
								mediaPlayer.start();
								handler.post(r);// 进度条显示
								mark++;
							} else {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.play);
								mediaPlayer.pause();
								mediaPlayer.seekTo(0);
								mark--;
							}

						}
					});
					// 处理中
				} else if (STATE == 2) {
					// 处理中
					setContentView(R.layout.activity_question_details_dealing);

					word_tv = (TextView) findViewById(R.id.word_tv);
					word_tv.setText("问题描述：" + data.getString("word"));
					final ImageView play_dealing = (ImageView) findViewById(R.id.play_dealing);
					ImageView backbt_dealing = (ImageView) findViewById(R.id.back_dealing);
					TextView time_tv = (TextView) findViewById(R.id.time_tv);
					ImageView share_iv = (ImageView) findViewById(R.id.share_iv);
					mCircleProgressBar = (CircleProgress) findViewById(R.id.roundBar1);
					finish_feek_btn = (Button) findViewById(R.id.finish_feek_btn);
					mCircleProgressBar.setMainProgress(0);
					time_tv.setText("接单于" + data.getString("rT"));

					// intent.putExtra("stlyeMarks",2);//主页：0，师傅：1，客户;2
					if (getintent.getIntExtra("stlyeMarks", -1) == 1) {

						
					} else if (getintent.getIntExtra("stlyeMarks", -1) == 2) {

						/*if(isFeedback){
							
						}else{
							
							finish_feek_btn.setVisibility(View.GONE);
						}*/
						
					} else if(getintent.getIntExtra("stlyeMarks", -1) == 0){//主页
						
						finish_feek_btn.setVisibility(View.GONE);
					}
					// 完成反馈
					finish_feek_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							feekDialogShow();

						}
					});
					backbt_dealing.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onBackPressed();
						}
					});
					share_iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ShareUtil su = new ShareUtil(QuetionDetailActivity.this);
							su.initSharePopupWindow(v);
						}
					});
					mCircleProgressBar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 播放录音
							if (mark == 0) {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.pause);
								mediaPlayer.start();
								handler.post(r);// 进度条显示
								mark++;
							} else {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.play);
								mediaPlayer.pause();
								mediaPlayer.seekTo(0);
								mark--;
							}

						}
					});
					// 求助中
				} else if (STATE == 1) {
					// 求助中
					setContentView(R.layout.activity_question_details_help);

					word_tv = (TextView) findViewById(R.id.word_tv);
					word_tv.setText("问题描述：" + data.getString("word"));
					final ImageView play_help = (ImageView) findViewById(R.id.play_help);
					ImageView backbt_help = (ImageView) findViewById(R.id.back_help);
					TextView time_tv = (TextView) findViewById(R.id.time_tv);
					ImageView share_iv = (ImageView) findViewById(R.id.share_iv);
					mCircleProgressBar = (CircleProgress) findViewById(R.id.roundBar1);
					recei_order_btn = (Button) findViewById(R.id.recei_order_btn);

					mCircleProgressBar.setMainProgress(0);
					time_tv.setText("发布于" + data.getString("pT"));
					LocationMap lMap = new LocationMap(QuetionDetailActivity.this,
							data.getString("address"),
							data.getDouble("latitude"),
							data.getDouble("longitude"));
					lMap.Map();

					if (getintent.getIntExtra("stlyeMarks", -1) == 1) {//师傅

					} else if (getintent.getIntExtra("stlyeMarks", -1) == 2) {//客户

						recei_order_btn.setVisibility(View.GONE);
					}
					if (MyCmd.is_pro == 0) {

						recei_order_btn.setVisibility(View.GONE);
					}
					// 接单
					recei_order_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							dialogShow();

						}
					});
					backbt_help.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onBackPressed();
						}
					});
					share_iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ShareUtil su = new ShareUtil(QuetionDetailActivity.this);
							su.initSharePopupWindow(v);
						}
					});
					mCircleProgressBar.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 播放录音
							if (mark == 0) {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.pause);
								mediaPlayer.start();
								handler.post(r);// 进度条显示
								// mCircleProgressBar.setMainProgress(((mediaPlayer.getCurrentPosition())*100)/(mediaPlayer.getDuration()));
								mark++;
							} else {
								mCircleProgressBar
										.setBackgroundResource(R.drawable.play);
								mediaPlayer.pause();
								mediaPlayer.seekTo(0);
								mark--;
							}
						}
					});
				}
	//			
				init();
				// 图片触摸事件
				images_ga.setImageActivity(QuetionDetailActivity.this);

				ImageAdapter imageAdapter = new ImageAdapter(
						QuetionDetailActivity.this, map);
				images_ga.setAdapter(imageAdapter);
				

			}

		};

		readOderHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				boolean bool = json.getBoolean("isGet");

				if (bool) {

					Toast.makeText(QuetionDetailActivity.this, "接单成功",
							Toast.LENGTH_SHORT).show();
					recei_order_btn.setVisibility(View.GONE);
				} else {

					Toast.makeText(QuetionDetailActivity.this, "接单失败",
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	private void init() {
		Bitmap image = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		// imagesCache.put("background_non_load",image); //设置缓存中默认的图片

		if (STATE == 3) {
			// 完成
			images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery_complete);
			pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear_complete);
		} else if (STATE == 2) {
			// 处理中
			images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery_dealing);
			pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear_dealing);
		} else if (STATE == 1) {
			// 求助中
			images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);
			pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		}

		// pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
		for (int i = 0; i < 4; i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}
		images_ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println(arg2 + "arg2");
				switch (arg2) {
				case 0:
					uri = Uri.parse("http://www.baidu.com/");
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

					break;
				case 1:
					uri = Uri.parse("http://www.baidu.com/");
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

					break;
				case 2:
					uri = Uri.parse("http://www.baidu.com/");
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

					break;
				case 3:
					uri = Uri.parse("http://www.baidu.com/");
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);

					break;

				default:
					break;
				}

			}
		});

	}

	public void changePointView(int cur) {
		// LinearLayout pointLinear = (LinearLayout)
		// findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timeFlag = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timeTaks.timeCondition = false;
		mediaPlayer.release();
		// isPause=true;

	}

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				gallerypisition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();// 存放数据
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;// 消息标识
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Timer autoGallery = new Timer();

	/**
	 * dialog
	 */
	public void dialogShow() {

		android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setMessage("是否接单？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Map<String, Object> params = new HashMap<String, Object>();
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				// params.put("r_t", curDate);
				params.put("p_id", MyCmd.id);
				params.put("id", getintent.getCharSequenceExtra("id"));
				// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
				SendRequestUtil.post("HomeCtrl.do?action=getOrder", params,
						readOderHandler);
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

	/**
	 * dialog
	 */
	public void feekDialogShow() {

		android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setMessage("问题解决了吗？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				if(getintent.getIntExtra("stlyeMarks", -1) == 1){//师傅反馈
					
					 intent = new Intent(QuetionDetailActivity.this,
								ActivityMasterFeekback.class);
					 intent.putExtra("id", getintent.getCharSequenceExtra("id"));
				}else if(getintent.getIntExtra("stlyeMarks", -1) == 2){//用户
					
					 intent = new Intent(QuetionDetailActivity.this,
							ActivityFeekback.class);
					 intent.putExtra("id", getintent.getCharSequenceExtra("id"));
				}
				
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
