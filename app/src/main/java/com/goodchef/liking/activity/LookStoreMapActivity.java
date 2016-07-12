package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.ResourceUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.MapStoreDialog;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.mvp.presenter.CheckGymPresenter;
import com.goodchef.liking.mvp.view.CheckGymView;

/**
 * 说明:查看场馆
 * Author shaozucheng
 * Time:16/6/7 下午5:49
 */
public class LookStoreMapActivity extends AppBarActivity implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, CheckGymView {
    private MapView mMapView;
    private AMap mAMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //定位监听
    private OnLocationChangedListener mListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    //添加的覆盖物标志
    private Marker currentMarker;
    private MapStoreDialog mMapStoreDialog;

    private CheckGymPresenter mCheckGymPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_store);
        initView();
        initMap();
        initData();
        mMapView.onCreate(savedInstanceState);
        setTitle("上海市");
    }

    private void initData() {
        mCheckGymPresenter = new CheckGymPresenter(this, this);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.store_map);
    }

    private void initMap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mAMap = mMapView.getMap();

        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(13));

        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_mark));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(ResourceUtils.getColor(R.color.map_radius_back));
        myLocationStyle.radiusFillColor(ResourceUtils.getColor(R.color.map_radius_gray_back));
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(2);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //开启定位
        mLocationClient.startLocation();
        mAMap.setOnMarkerClickListener(this);

        setMapMark();
    }

    /**
     * 设置mark覆盖物标记
     */
    private void setMapMark() {
        LatLng latLng = new LatLng(31.1798261320, 121.4435724420);
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.position(latLng);
        otMarkerOptions.visible(true);//设置可见
        otMarkerOptions.draggable(true);
        //   otMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_mark)));
        otMarkerOptions.icon(ImageNormal(0));
        mAMap.addMarker(otMarkerOptions);
    }

    /**
     * 自定义标记物的图片（未选中状态）
     *
     * @param i
     * @return
     */
    private BitmapDescriptor ImageNormal(int i) {
        View view = getLayoutInflater().inflate(R.layout.layout_map_mark, null);
        TextView tv = (TextView) view.findViewById(R.id.map_mark_title);
        tv.setText("凌空SoHo店");
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
        return bitmap;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mListener = null;
            mLocationOption = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        showStoreDialog();
        return false;
    }

    private void showStoreDialog() {
        mMapStoreDialog = new MapStoreDialog(this);
    }

    private void dismissDialog() {
        if (mMapStoreDialog != null) {
            mMapStoreDialog.dismiss();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        dismissDialog();
    }

    @Override
    public void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData) {

    }
}
