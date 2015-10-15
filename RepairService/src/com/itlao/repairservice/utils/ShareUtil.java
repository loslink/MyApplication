package com.itlao.repairservice.utils;

import java.util.ArrayList;
import java.util.List;


import com.itlao.cmd.AppInfo;
import com.itlao.repairservice.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;





public class ShareUtil {

	ShareCustomAdapter adapter;
	Context context;
		/**
	     * 
	     */
    public ShareUtil(Context context){
    	
    	this.context=context;
    }
	
	private List<AppInfo> getShareAppList() {	
		List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> resolveInfos = getShareApps();
		if (null == resolveInfos) {
			return null;
		} else {
			for (ResolveInfo resolveInfo : resolveInfos) {
				AppInfo appInfo = new AppInfo();
				appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
				appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
				appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
				appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
				shareAppInfos.add(appInfo);
			}
		}		
		return shareAppInfos;
	}
	
	
	//查询手机内所有支持分享的应用列表
	public List<ResolveInfo> getShareApps() {	
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}
	
	public void initSharePopupWindow(View parent) {
		PopupWindow sharePopupWindow = null;
		View view = null;
		ListView shareList = null;
		if(null == sharePopupWindow) {
			//加载布局文件
			view = LayoutInflater.from(context).inflate(R.layout.popup_share, null);
			shareList = (ListView) view.findViewById(R.id.share_list);
			List<AppInfo> shareAppInfos = getShareAppList();
			 adapter = new ShareCustomAdapter(context, shareAppInfos);
			shareList.setAdapter(adapter);
			
			shareList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					AppInfo appInfo = (AppInfo) adapter.getItem(position);
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setComponent(new ComponentName(appInfo.getAppPkgName(), appInfo.getAppLauncherClassName()));
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_TEXT, "测试，这里发送推广地址");
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(shareIntent);
				}
			});
			
			// 获取屏幕密度（方法2）  
			DisplayMetrics dm = new DisplayMetrics();  
			dm = context.getResources().getDisplayMetrics();  

			sharePopupWindow = new PopupWindow(view, (int)(160 * dm.density), LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		//使其聚焦
		sharePopupWindow.setFocusable(true);
		//设置允许在外点击消失
		sharePopupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		//xoff,yoff基于anchor的左下角进行偏移。正数表示下方右边，负数表示（上方左边）
		//showAsDropDown(parent, xPos, yPos);
		sharePopupWindow.showAsDropDown(parent, -5, 5);
	}
	
	
	class ShareCustomAdapter extends BaseAdapter {
		
	            private Context context;
	            private List<AppInfo> list;
	            public ShareCustomAdapter(Context context,List<AppInfo> list) {
	                super();
	                this.context = context;
	                this.list=list;
	            }


	            @Override
	            public int getCount() {
	                return list.size();
	            }

	            @Override
	            public Object getItem(int position) {
	                return list.get(position);
	            }

	           
	            @Override
	            public long getItemId(int position) {
	                return position;
	            }

				@Override
				public View getView(int position, View v, ViewGroup arg2) {
					
					if (v==null) {
						v=LayoutInflater.from(context).inflate(R.layout.item_popup_share, null); 
					}
					
					AppInfo item=list.get(position);
					
					ImageView iv=(ImageView) v.findViewById(R.id.share_item_icon);
					TextView tv=(TextView) v.findViewById(R.id.share_item_name);
					
					iv.setImageDrawable(item.getAppIcon());
					tv.setText(item.getAppName());
					v.setTag(item);
					
					return v;
				}

	        }

	

}
