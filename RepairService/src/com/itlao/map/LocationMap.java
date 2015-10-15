
package com.itlao.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;


import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;


import com.itlao.repairservice.R;
import com.itlao.repairservice.utils.DistanceLngLat;


/**
 * package com.itlao.map;
 * @Laoqilian
 * 2015-1-31 下午8:15:44
 */

//DB:B1:E0:07:55:32:E2:F1:CD:46:F2:C9:EA:D2:28:25:32:B0:7E:2C;baidumapsdk.demo
public class LocationMap {
	
	
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;
		//private PopupOverlay mPopupOverlay  = null;//弹出泡泡图层，浏览节点时使用
		MapView mMapView;
		BaiduMap mBaiduMap;
		Activity context;
		private View viewCache;
		String addr;
		double lat;
		double lng;

		// UI相关
		OnCheckedChangeListener radioButtonListener;
		Button requestLocButton;
		boolean isFirstLoc = true;// 是否首次定位
		
		
		public LocationMap(Activity context,String addr,double lat,double lng){
			
			this.context=context;
			this.addr=addr;
			this.lat=lat;
			this.lng=lng;
		}
		
		public void Map(){
			
			mCurrentMode = LocationMode.NORMAL;
			
			// 地图初始化
			mMapView = (MapView)context.findViewById(R.id.bmapView);
			mBaiduMap = mMapView.getMap();
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			// 隐藏缩放控件
	        int childCount = mMapView.getChildCount();
	        View zoom = null;
	        for (int i = 0; i < childCount; i++) {
	                View child = mMapView.getChildAt(i);
	                if (child instanceof ZoomControls) {
	                        zoom = child;
	                        break;
	                }
	        }
	        zoom.setVisibility(View.GONE);
	        
	        /*viewCache = LayoutInflater.from(context).inflate(R.layout.pop_layout, null);
	        mPopupOverlay = new PopupOverlay(mMapView ,new PopupClickListener() {
				
				@Override
				public void onClickedPopup(int arg0) {
					mPopupOverlay.hidePop();
				}
			});*/
	        // 隐藏指南针
	        UiSettings mUiSettings = mBaiduMap.getUiSettings();
	        mUiSettings.setCompassEnabled(true);
	        // 删除百度地图logo
	        mMapView.removeViewAt(1);
	        // 删除左下角比例尺
	        mMapView.showScaleControl(false);
	        
			// 定位初始化
			mLocClient = new LocationClient(context);
			mLocClient.registerLocationListener(myListener);
			
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setAddrType("all");//返回的定位结果包含地址信息
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			
			/*
			BDLocation location=new BDLocation();
			location.setRadius(2);
			MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(lat)
			.longitude(lng).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(lat,lng);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);*/
		}


		/*private void showPopupOverlay(BDLocation location){
			 TextView popText = ((TextView)viewCache.findViewById(R.id.location_tips));
			 popText.setText("[我的位置]\n" + location.getAddrStr());
			 mPopupOverlay.showPopup(getBitmapFromView(popText),
						new GeoPoint((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6)),
						10);
		}*/	
		
		/**
		 * 
		 * @param view
		 * @return
		 */
		/*public static Bitmap getBitmapFromView(View view) {
			view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	        view.buildDrawingCache();
	        Bitmap bitmap = view.getDrawingCache();
	        return bitmap;
		}*/
		
		
/**
 * 定位SDK监听函数
 */
public class MyLocationListenner implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mMapView == null)
			return;
		
		TextView adrr_tv=(TextView) context.findViewById(R.id.adrr_tv);
		double distance=DistanceLngLat.gps2m(lat, lng, location.getLatitude(), location.getLongitude());
		adrr_tv.setText("发布问题位置："+addr+"\n"+"距离自己："+distance+"米以内");
		/*MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();*/
		
		MyLocationData locData = new MyLocationData.Builder()
		.accuracy(location.getRadius())
		// 此处设置开发者获取到的方向信息，顺时针0-360
		.direction(100).latitude(lat)
		.longitude(lng).build();
		mBaiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			isFirstLoc = false;
			/*LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());*/
			LatLng ll = new LatLng(lat,lng);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			
		}
	}

	public void onReceivePoi(BDLocation poiLocation) {
	}
}

/*@Override
protected void onPause() {
	mMapView.onPause();
	super.onPause();
}

@Override
protected void onResume() {
	mMapView.onResume();
	super.onResume();
}

@Override
protected void onDestroy() {
	// 退出时销毁定位
	mLocClient.stop();
	// 关闭定位图层
	mBaiduMap.setMyLocationEnabled(false);
	mMapView.onDestroy();
	mMapView = null;
	super.onDestroy();
}
*/
}
