package com.itlao.repairservice.pcenter.manito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.R;
import com.itlao.repairservice.R.id;
import com.itlao.repairservice.R.layout;

import com.itlao.repairservice.findmaster.ctrl.Zhaoshifu_geren_Activity;
import com.itlao.repairservice.home.ctrl.MainActivity;
import com.itlao.repairservice.pcenter.manito.ctrl.DashenPersonalActivity;
import com.itlao.repairservice.utils.EncodeDecodeUtil;
import com.itlao.repairservice.utils.PhotoYasuo;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.slidingmenu.SlidingMenu;
import com.itlao.utils.http.handler.RequestHandler;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class ListDashenShow {

	private SimpleAdapter adapterSimple;
	private GridView gridView;
	RequestHandler ListHandler;
	byte[] by;
	
	private JSONArray rows;

	// 创建一个ArrayList列表,内部存的是HashMap列表
	static ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();

	public void List_questions_show(Activity context) {

		gridView = (GridView) context.findViewById(R.id.page_home_grid_dashen);

		setHandler(context);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("o_id", MyCmd.id);
		// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
		SendRequestUtil
				.post("MasterCtrl.do?action=getCollectMaster", params, ListHandler);
		

	}

	/**
	 * 网络获取的数据在这里获取处理
	 * 
	 */
	public void setHandler(final Context context) {
		ListHandler = new RequestStringHandler(context) {
			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

			
				rows = json.getJSONArray("Rows");
				int len2 = rows.length();
				PhotoYasuo photo_Yasuo = new PhotoYasuo();
				listItems.clear();
				for (int i = 0; i < len2; i++) {

					by = EncodeDecodeUtil.decode(JsonUtil.getString(
							rows.getJSONObject(i), "h_s"));
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
					map.put("item", JsonUtil.getString(rows.getJSONObject(i),
							"nickname"));

					listItems.add(map);
					// 回收
					bmp.recycle();
				}

				// HashMap中的Key信息,要与grid_item.xml中的信息作对应
				String[] from = { "image", "item" };
				// grid_item.xml中对应的ImageView控件和TextView控件
				int[] to = { R.id.item_imageView_shifu_list,
						R.id.item_textView_shifu_list };
				// 设定一个适配器
				adapterSimple = new SimpleAdapter(
						context, listItems,
						R.layout.grid_item_shifu_list, from, to);

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

				gridView.setAdapter(adapterSimple);
				/* 动态跟新ListView */
				adapterSimple.notifyDataSetChanged();

				gridView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						final Intent intent = new Intent(
								context,
								DashenPersonalActivity.class);

						try {
							intent.putExtra("id", rows.getJSONObject(position)
									.getLong("id"));
							intent.putExtra("nickname", JsonUtil.getString(
									rows.getJSONObject(position), "nickname"));
							intent.putExtra("sex", rows.getJSONObject(position)
									.getInt("sex"));
							intent.putExtra("address", JsonUtil.getString(
									rows.getJSONObject(position), "address"));
							intent.putExtra(
									"longitude",
									rows.getJSONObject(position).getDouble(
											"longitude"));
							intent.putExtra(
									"latitude",
									rows.getJSONObject(position).getDouble(
											"latitude"));
							intent.putExtra("h_s", JsonUtil.getString(
									rows.getJSONObject(position), "h_s"));
							intent.putExtra("p_n", JsonUtil.getString(
									rows.getJSONObject(position), "p_n"));
							intent.putExtra(
									"qq",
									JsonUtil.getString(
											rows.getJSONObject(position), "qq"));
							intent.putExtra("email", JsonUtil.getString(
									rows.getJSONObject(position), "email"));
							intent.putExtra("t_s", rows.getJSONObject(position)
									.getInt("t_s"));
							intent.putExtra(
									"profession",
									rows.getJSONObject(position).getInt(
											"profession"));
							intent.putExtra("pro_det", JsonUtil.getString(
									rows.getJSONObject(position), "pro_det"));
							intent.putExtra(
									"status",
									rows.getJSONObject(position).getInt(
											"status"));
							// intent.putExtra("dirty",
							// rows.getJSONObject(position).getInt("dirty"));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						context
								.startActivity(intent);
					}

				});

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
