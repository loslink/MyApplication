package com.itlao.repairservice.findmaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.QuetionDetailActivity;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;

import com.itlao.repairservice.findmaster.ctrl.ActivityOders;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.pcenter.immaster.ctrl.IamShifuActivity;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.slidingmenu.SlidingMenu;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class ListQuestionsShowOders {

	private static SimpleAdapter adapterSimple;
	private static GridView gridView;

	// 创建一个ArrayList列表,内部存的是HashMap列表
	// static ArrayList<HashMap<String, Object>> listItems = new
	// ArrayList<HashMap<String, Object>>();

	RequestHandler readtaskListHandler;
	byte[] by;
	private LinkedList<String> mListItems;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private ArrayAdapter<ArrayList<HashMap<String, Object>>> mAdapter;
	private org.json.JSONArray rowsStyle;
	private org.json.JSONArray rows;
	int pageNum = 1;// 页码
	int pageSize = 10;// 每页大小
	private boolean isAsc;
	private boolean isSolved;

	// 创建一个ArrayList列表,内部存的是HashMap列表
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();

	public void List_questions_show() {

		setHandler(ActivityOders.activityOders);

		mPullRefreshGridView = (PullToRefreshGridView) ActivityOders.activityOders
				.findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		listItems.clear();
		Map<String, Object> params = new HashMap<String, Object>();
		isAsc=false;
		isSolved=true;
		params.put("isSolved", isSolved);
		params.put("sortName", "p_t");
		params.put("isAsc", isAsc);
		
		params.put("o_id", MyCmd.id);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
		SendRequestUtil.post("HomeCtrl.do?action=questionsByMaserId", params,
				readtaskListHandler);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					/**
					 * 下拉刷新
					 */
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						Toast.makeText(ActivityOders.activityOders,
								"正在努力为亲刷新...", Toast.LENGTH_SHORT).show();
						Map<String, Object> params = new HashMap<String, Object>();
						listItems.clear();// 清除问题数据
						pageNum = 1;
						params.put("isSolved", isSolved);
						params.put("sortName", "p_t");
						params.put("isAsc", isAsc);
						
						params.put("o_id", MyCmd.id);
						params.put("pageNum", pageNum);
						params.put("pageSize", pageSize);
						// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
						SendRequestUtil.post("HomeCtrl.do?action=questionsByMaserId",
								params, readtaskListHandler);
						/* new GetDataTask().execute(); */
					}

					/**
					 * 上拉刷新
					 */
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						Toast.makeText(MainActivity.mainActivity, "正在加载更多",
								Toast.LENGTH_SHORT).show();
						Map<String, Object> params = new HashMap<String, Object>();
						pageNum++;
						params.put("isSolved", isSolved);
						params.put("sortName", "p_t");
						params.put("isAsc", isAsc);
						
						params.put("o_id", MyCmd.id);
						params.put("pageNum", pageNum);
						params.put("pageSize", pageSize);
						// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
						SendRequestUtil.post("HomeCtrl.do?action=questionsByMaserId",
								params, readtaskListHandler);
						new GetDataTask().execute();
					}

				});

		// mListItems = new LinkedList<String>();

	}

	/**
	 * 处理数据
	 * 
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

		/**
		 * 后台获取数据
		 */
		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(
				Void... params) {

			return listItems;
		}

		/**
		 * 后台获取数据后才调用此方法显示
		 */
		@Override
		protected void onPostExecute(
				final ArrayList<HashMap<String, Object>> listItems) {

			// HashMap中的Key信息,要与grid_item.xml中的信息作对应
			String[] from = { "image", "word", "p_t" };
			// grid_item.xml中对应的ImageView控件和TextView控件
			int[] to = { R.id.item_imageView_shifu, R.id.item_textView_shifu,
					R.id.item_tv_time_main_page_shifu };
			// 设定一个适配器
			adapterSimple = new SimpleAdapter(
					ActivityOders.activityOders, listItems,
					R.layout.grid_item_shifu, from, to);
			/* 实现ViewBinder()这个接口 */
			adapterSimple.setViewBinder(new ViewBinder() {
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					if (view instanceof ImageView && data instanceof Bitmap) {
						ImageView i = (ImageView) view;
						i.setImageBitmap((Bitmap) data);
						return true;
					}
					return false;
				}
			});

			mGridView.setAdapter(adapterSimple);
			/* 动态跟新ListView */
			adapterSimple.notifyDataSetChanged();
			mGridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					final Intent intent = new Intent(
							ActivityOders.activityOders,
							QuetionDetailActivity.class);
					intent.putExtra("id", listItems.get(position).get("id")
							.toString());
					intent.putExtra("status",
							listItems.get(position).get("status").toString());
					intent.putExtra("p_t", listItems.get(position).get("p_t")
							.toString());
					intent.putExtra("r_t", listItems.get(position).get("r_t")
							.toString());
					intent.putExtra("s_t", listItems.get(position).get("s_t")
							.toString());
					intent.putExtra("word", listItems.get(position).get("word")
							.toString());
					intent.putExtra("address",
							listItems.get(position).get("address").toString());
					intent.putExtra("longitude",
							(Double) listItems.get(position).get("longitude"));
					intent.putExtra("latitude", (Double) listItems
							.get(position).get("latitude"));
					ActivityOders.activityOders.startActivity(intent);
				}
			});
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshGridView.onRefreshComplete();
			super.onPostExecute(listItems);
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

				rows = json.getJSONArray("Rows");
				int len2 = rows.length();
				PhotoYasuo photo_Yasuo = new PhotoYasuo();
				//listItems.clear();
				for (int i = 0; i < len2; i++) {

					by = EncodeDecodeUtil.decode(JsonUtil.getString(
							rows.getJSONObject(i), "pictures"));
					HashMap<String, Object> map = new HashMap<String, Object>(); // "/storage/sdcard1/tieba/bing.jpg"

					// 解决加载图片 内存溢出的问题
					// Options 只保存图片尺寸大小，不保存图片到内存
					BitmapFactory.Options opts = new BitmapFactory.Options();
					// 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
					opts.inSampleSize = 2; // 质量缩小8倍
					Bitmap bmp = null;
					bmp = BitmapFactory.decodeByteArray(by, 0, by.length, opts);
					Bitmap bm_square = centerSquareScaleBitmap(photo_Yasuo
							.comp(bmp));// 剪裁图片
					map.put("image", photo_Yasuo.comp(bm_square));

					map.put("id",
							JsonUtil.getString(rows.getJSONObject(i), "id"));
					map.put("status",
							JsonUtil.getString(rows.getJSONObject(i), "status"));
					map.put("p_t",
							JsonUtil.getString(rows.getJSONObject(i), "p_t"));
					map.put("r_t",
							JsonUtil.getString(rows.getJSONObject(i), "r_t"));
					map.put("s_t",
							JsonUtil.getString(rows.getJSONObject(i), "s_t"));
					map.put("word",
							JsonUtil.getString(rows.getJSONObject(i), "word"));
					map.put("address", JsonUtil.getString(
							rows.getJSONObject(i), "address"));
					map.put("longitude",
							rows.getJSONObject(i).getDouble("longitude"));
					map.put("latitude",
							rows.getJSONObject(i).getDouble("latitude"));

					listItems.add(map);

					// 回收
					bmp.recycle();
				}
				new GetDataTask().execute();

			}

		};
	}

	/**
	 * 按照固定比例剪切图片。
	 * 
	 * @param bitmap
	 *            原图
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		int edgeLength = 0;
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		if (widthOrg <= ((200 / 100) * heightOrg)) {

			try {
				int w = (100 * widthOrg) / 200;
				int e = (heightOrg - (widthOrg * 200) / 100) / 2;
				result = Bitmap.createBitmap(result, 0,
						(heightOrg - (widthOrg * 100) / 200) / 2, widthOrg, w);

			} catch (Exception e) {
				return null;
			}
		} else {

			try {
				result = Bitmap.createBitmap(result,
						(widthOrg - (heightOrg * 200) / 100) / 2, 0,
						(heightOrg * 200) / 100, heightOrg);

			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}
}
