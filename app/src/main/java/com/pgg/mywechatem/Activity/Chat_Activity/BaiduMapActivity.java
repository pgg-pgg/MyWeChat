package com.pgg.mywechatem.Activity.Chat_Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.FlippingLoadingDialog;

public class BaiduMapActivity extends BaseActivity implements OnClickListener {


	private ImageButton ib_exit_01;
	private View vertical_line;
	private TextView title_tv_left;
	private TextView title_tv_center;
	private Button btn_sure;

	static MapView mMapView = null;
	FrameLayout mMapViewContainer = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;
	private static BDLocation lastLocation = null;
	public static BaiduMapActivity instance = null;
	private BaiduMap mBaiduMap;
	private FlippingLoadingDialog mLoadingDialog;
	private LocationMode mCurrentMode;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class BaiduSDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(instance, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_SHORT).show();
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(instance, "网络出错", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private BaiduSDKReceiver mBaiduReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_baidumap);
		mMapView =  findViewById(R.id.bmapView);
		btn_sure = findViewById(R.id.btn_sure);
		btn_sure.setText("发送");
		btn_sure.setVisibility(View.VISIBLE);


		ib_exit_01 =findViewById(R.id.ib_exit_01);
		vertical_line =findViewById(R.id.vertical_line);
		title_tv_left =findViewById(R.id.title_tv_left);
		title_tv_center =findViewById(R.id.title_tv_center);
		ib_exit_01.setVisibility(View.VISIBLE);
		vertical_line.setVisibility(View.VISIBLE);
		title_tv_left.setVisibility(View.VISIBLE);
		title_tv_center.setVisibility(View.GONE);
		title_tv_left.setText("我的位置");
		ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
		btn_sure.setEnabled(true);
		btn_sure.setBackgroundResource(R.color.greeny);
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMyLocationEnabled(true);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		initMapView();
		if (latitude == 0) {
			BaiduMapOptions mapoption = new BaiduMapOptions();
			mMapView = new MapView(this, mapoption);
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, null));
			showMapWithLocationClient();
		} else {
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");
			LatLng p = new LatLng(latitude, longtitude);
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
			showMap(latitude, longtitude, address);
		}
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mBaiduReceiver = new BaiduSDKReceiver();
		registerReceiver(mBaiduReceiver, iFilter);
		initClick();
	}

	private void initClick() {
		btn_sure.setOnClickListener(this);
		ib_exit_01.setOnClickListener(this);
	}

	private void showMap(double latitude, double longtitude, String address) {
		btn_sure.setVisibility(View.GONE);
		LatLng llA = new LatLng(latitude, longtitude);
		CoordinateConverter converter = new CoordinateConverter();
		converter.coord(llA);
		converter.from(CoordinateConverter.CoordType.COMMON);
		LatLng convertLatLng = converter.convert();
		OverlayOptions ooA = new MarkerOptions()
				.position(convertLatLng)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)).zIndex(4)
				.draggable(true);
		mBaiduMap.addOverlay(ooA);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng,
				17.0f);
		mBaiduMap.animateMapStatus(u);
	}

	private void showMapWithLocationClient() {
		mLoadingDialog = new FlippingLoadingDialog(this, "正在确定你的位置...");
		mLoadingDialog.show();

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		// option.setCoorType("bd09ll"); //设置坐标类型
		// Johnson change to use gcj02 coordination. chinese national standard
		// so need to conver to bd09 everytime when draw on baidu map
		option.setCoorType("gcj02");
		option.setScanSpan(30000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mLocClient != null) {
			mLocClient.stop();
		}
		super.onPause();
		lastLocation = null;
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mLocClient != null) {
			mLocClient.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.onDestroy();
		unregisterReceiver(mBaiduReceiver);
		super.onDestroy();
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			Log.d("map", "On location change received:" + location);
			Log.d("map", "addr:" + location.getAddrStr());
			btn_sure.setEnabled(true);
			if (mLoadingDialog != null) {
				mLoadingDialog.dismiss();
			}

			if (lastLocation != null) {
				if (lastLocation.getLatitude() == location.getLatitude()
						&& lastLocation.getLongitude() == location
								.getLongitude()) {
					Log.d("map", "same location, skip refresh");

					// mMapView.refresh(); //need this refresh?
					return;
				}
			}
			lastLocation = location;
			mBaiduMap.clear();
			LatLng llA = new LatLng(lastLocation.getLatitude(),
					lastLocation.getLongitude());
			CoordinateConverter converter = new CoordinateConverter();
			converter.coord(llA);
			converter.from(CoordinateConverter.CoordType.COMMON);
			LatLng convertLatLng = converter.convert();
			OverlayOptions ooA = new MarkerOptions()
					.position(convertLatLng)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marka)).zIndex(2)
					.draggable(true);
			mBaiduMap.addOverlay(ooA);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
					convertLatLng, 17.0f);
			mBaiduMap.animateMapStatus(u);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	public void back(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_exit_01:
			Utils.finish(BaiduMapActivity.this);
			break;
		case R.id.btn_sure:
			if (lastLocation != null) {
				Intent intent = getIntent();
				intent.putExtra("latitude", lastLocation.getLatitude());
				intent.putExtra("longitude", lastLocation.getLongitude());
				intent.putExtra("address", lastLocation.getAddrStr());
				setResult(RESULT_OK, intent);
				Utils.finish(BaiduMapActivity.this);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void initController() {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {

	}
}
