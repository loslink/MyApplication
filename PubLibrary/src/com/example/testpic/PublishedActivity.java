package com.example.testpic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.utils.recordvoice.util.AudioItemBean;
import com.example.utils.recordvoice.util.AudioRecorder;
import com.example.utils.recordvoice.util.ICallback;
import com.example.utils.recordvoice.util.Utils;
import com.example.utils.recordvoice.view.RecordMicView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PublishedActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private AudioRecorder recorder;
	private RecordMicView recordMicView;
	private Rect recordMicViewRect;
	private Handler handler2 = new Handler();
	// 录音已经停止
	private boolean stop = true;
	private Button record;
	private FrameLayout fl_play_delete;
	private Button playBtn;
	private LinearLayout ll_delete;
	private int mark = 0;
	final MediaPlayer mediaPlayer = new MediaPlayer();
	public AudioItemBean itemBean;
	public static List<Bitmap> bmplist = new ArrayList<Bitmap>();
	TextView send;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg);
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		FileUtils.deleteDir();
		Init();
		
		// 录音机
		this.recorder = new AudioRecorder(recordCallback, 60000, 1000, 500);

		setListener();
	}

	private void setListener() {

		/*send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(Bimp.bmp.size()==4){
					Toast.makeText(PublishedActivity.this, "发布...", Toast.LENGTH_SHORT).show();
				}else{
					
					Toast.makeText(PublishedActivity.this, "请选够四张图片", Toast.LENGTH_SHORT).show();
				}
			}
		});*/
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				playBtn.setBackgroundResource(R.drawable.play);
				mediaPlayer.pause();
				mediaPlayer.seekTo(0);
				mark--;
			}
		});

		playBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// playBtn.setBackgroundResource(R.drawable.pause);
				// 播放录音
				if (mark == 0) {
					playBtn.setBackgroundResource(R.drawable.pause);
					mediaPlayer.start();

					mark++;
				} else {
					playBtn.setBackgroundResource(R.drawable.play);
					mediaPlayer.pause();
					mediaPlayer.seekTo(0);
					mark--;
				}
			}
		});
		ll_delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				File file = new File(itemBean.getFilePath());
				file.delete();
				fl_play_delete.setVisibility(View.GONE);
				record.setVisibility(View.VISIBLE);

			}
		});
		record.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View paramView, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					startRecord();
				} else if (action == MotionEvent.ACTION_UP) {
					stopRecord();
				} else if (action == MotionEvent.ACTION_MOVE) {
					if (!stop) {
						// 获取触摸相对于屏幕的坐标
						float rawX = event.getRawX();
						float rawY = event.getRawY();
						if (recordMicViewRect == null) {
							recordMicViewRect = Utils
									.getViewAbsoluteLocation(recordMicView);
						}
						if (recordMicViewRect.contains((int) rawX, (int) rawY)) {
							cancelRecord();
						}
					}
				}
				return false;
			}
		});
	}

	public void Init() {
		//send = (TextView) findViewById(R.id.activity_selectimg_send);
		playBtn = (Button) findViewById(R.id.playBtn);
		ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
		recordMicView = (RecordMicView) findViewById(R.id.record_mic);
		record = (Button) findViewById(R.id.record);
		fl_play_delete = (FrameLayout) findViewById(R.id.fl_play_delete);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update1();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishedActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring(
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str + ".JPEG");
				}
				// 高清的压缩图片全部就在 list 路径里面了
				// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				FileUtils.deleteDir();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update1() {
			loading1();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// final int coord = position;
			ViewHolder holder = null;

			System.out.println("测试下表=" + position);
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.image.setVisibility(View.VISIBLE);

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));

			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			if (position == 9) {
				holder.image.setVisibility(View.GONE);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading1() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								int len=Bimp.max;
								if(Bimp.drr.size()>len){
									
									String path = Bimp.drr.get(Bimp.max);
									System.out.println(path);
									Bitmap bm = Bimp.revitionImageSize(path);
									Bimp.bmp.add(bm);
									String newStr = path.substring(
											path.lastIndexOf("/") + 1,
											path.lastIndexOf("."));
									FileUtils.saveBitmap(bm, "" + newStr);
									Bimp.max += 1;
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
								}
								
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update1();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			super(mContext);

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();

				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							TestPicActivity.class);
					startActivityForResult(intent, R.id.item_popupwindows_Photo);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	public void photo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/myimage/");
			if (!dir.exists())
				dir.mkdirs();

			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(dir,
					String.valueOf(System.currentTimeMillis()) + ".jpg");
			path = file.getPath();
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);

		} else {
			Toast.makeText(PublishedActivity.this, "没有储存卡", Toast.LENGTH_LONG)
					.show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 4 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		/*case R.id.item_popupwindows_Photo:
			
			break;*/
		}

	}

	//

	private void startRecord() {
		stop = false;
		recordMicView.setVisibility(View.VISIBLE);
		recorder.startRecorder();
	}

	private void stopRecord() {
		stop = true;
		recordMicView.setVisibility(View.INVISIBLE);
		recorder.stop();
	}

	private void cancelRecord() {
		stop = true;
		recordMicView.setVisibility(View.INVISIBLE);
		recorder.cancel();
	}

	private void updateVolume() {
		int amplitude = recorder.getAmplitude();
		int volumeImgIndex = 7 * amplitude / 32768;
		recordMicView.setVolumeImg(volumeImgIndex);
	}

	// 录音完成以后的回调
	private ICallback recordCallback = new ICallback() {
		@Override
		public void onSuccess(final Object... result) {
			if (result != null && result.length == 2) {
				handler2.post(new Runnable() {
					@Override
					public void run() {
						stopRecord();
						String filePath = (String) result[0];
						int duration = (Integer) result[1];
						Log.e("test", "filePath:" + filePath + ",duration:"
								+ duration);
						itemBean = new AudioItemBean();
						itemBean.setFilePath(filePath);
						itemBean.setDuration(duration);
						record.setVisibility(View.GONE);
						fl_play_delete.setVisibility(View.VISIBLE);
						try {
							mediaPlayer.reset();
							mediaPlayer.setDataSource(PublishedActivity.this,
									Uri.parse(itemBean.getFilePath()));
							mediaPlayer.prepare();
						} catch (Exception e) {

							e.printStackTrace();
						}
						ItemBeanCallBack(itemBean);
					}

				});
			} else {
				onError(ERROR, "");
			}
		}

		@Override
		public void onProgress(int progress) {
			handler2.post(new Runnable() {
				@Override
				public void run() {
					// 周期性的调用，刷新页面上的音量
					updateVolume();
				}
			});
		}

		@Override
		public void onError(int code, final String info) {
			handler2.post(new Runnable() {
				@Override
				public void run() {
					stopRecord();
					if (info != null && info.length() > 0) {
						Utils.show(info, PublishedActivity.this);
					} else {
						Utils.show("录音出错", PublishedActivity.this);
					}
				}
			});
		}
	};

	public void ItemBeanCallBack(AudioItemBean itemBean) {

	}
	public List<Bitmap> getBitmap() {

		return Bimp.bmp;
	}
	
	public List<String> getBitmapPaths() {

		return Bimp.drr;
	}
}
