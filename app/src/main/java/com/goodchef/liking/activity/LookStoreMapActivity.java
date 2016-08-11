package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
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
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.mvp.presenter.CheckGymPresenter;
import com.goodchef.liking.mvp.view.CheckGymView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:查看场馆
 * Author shaozucheng
 * Time:16/6/7 下午5:49
 */
public class LookStoreMapActivity extends AppBarActivity implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, CheckGymView {
    private MapView mMapView;
    private LinearLayout mNoDataLayout;
    private LikingStateView mStateView;

    private TextView mNameTextView;
    private TextView mAddressTextView;
    private HImageView mHImageView;
    private RelativeLayout mLayout;
    private LinearLayout mGymLayout;

    private AMap mAMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //定位监听
    private OnLocationChangedListener mListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    //添加的覆盖物标志
    private Marker currentMarker;

    private CheckGymPresenter mCheckGymPresenter;
    private int cityId;
    private List<CheckGymListResult.CheckGymData.CheckGym> allGymList;

    private String selectCityName;
    private String selectCityId;
    private boolean isLoaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_store);
        initView();
        initMap();
        initData();
        mMapView.onCreate(savedInstanceState);
    }


    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.look_store_state_view);
        mMapView = (MapView) findViewById(R.id.store_map);
        mNoDataLayout = (LinearLayout) findViewById(R.id.layout_no_data);

        mNameTextView = (TextView) findViewById(R.id.map_store_name);
        mAddressTextView = (TextView) findViewById(R.id.store_address);
        mHImageView = (HImageView) findViewById(R.id.store_image);
        mLayout = (RelativeLayout) findViewById(R.id.layout_store_image);
        mGymLayout = (LinearLayout) findViewById(R.id.layout_gym);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initMap();
                setNetWorkView();
            }
        });
    }

    private void initData() {
        selectCityName = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY);
        selectCityId = getIntent().getStringExtra(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getIntent().getBooleanExtra(LikingHomeActivity.KEY_START_LOCATION, false);
        setTitle(selectCityName);
        mCheckGymPresenter = new CheckGymPresenter(this, this);
        setNetWorkView();
    }

    private void setNetWorkView(){
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.FAILED);
        } else {
            initMapData();
        }
    }

    private void initMapData() {
        if (isLoaction) {
            startLocation();
        } else {
            mCheckGymPresenter.getGymList(Integer.parseInt(selectCityId), 0, 0);
        }
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
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));

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
        mAMap.setOnMarkerClickListener(this);

    }


    private void startLocation() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
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
    }

    /**
     * 设置mark覆盖物标记
     */
    private void setMapMark(CheckGymListResult.CheckGymData.CheckGym gym) {
        LatLng latLng = new LatLng(gym.getLatitude(), gym.getLongitude());
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.position(latLng);
        otMarkerOptions.visible(true);//设置可见
        otMarkerOptions.draggable(true);
        otMarkerOptions.icon(ImageNormal(gym));
        Marker mark = mAMap.addMarker(otMarkerOptions);
        mark.setObject(gym);
    }

    /**
     * 自定义标记物的图片（未选中状态）
     *
     * @param
     * @return
     */
    private BitmapDescriptor ImageNormal(CheckGymListResult.CheckGymData.CheckGym gym) {
        View view = getLayoutInflater().inflate(R.layout.layout_map_mark, null);
        TextView tv = (TextView) view.findViewById(R.id.map_mark_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.map_mark_image);
        if (gym.isSelect()) {
            imageView.setImageResource(R.drawable.map_left_select);
        } else {
            imageView.setImageResource(R.drawable.map_left_no_select);
        }
        tv.setText(gym.getGymName());
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
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    mStateView.setState(StateView.State.FAILED);
                } else {
                    mCheckGymPresenter.getGymList(Integer.parseInt(selectCityId), aMapLocation.getLongitude(), aMapLocation.getLatitude());
                }
            } else {
                setMapLocationView();
            }
        } else {
            setMapLocationView();
        }
    }

    private void setMapLocationView() {
        mStateView.setState(StateView.State.SUCCESS);
        mGymLayout.setVisibility(View.GONE);
        mNoDataLayout.setVisibility(View.GONE);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        CheckGymListResult.CheckGymData.CheckGym gymDto = (CheckGymListResult.CheckGymData.CheckGym) currentMarker.getObject();
        if (gymDto != null) {
            for (CheckGymListResult.CheckGymData.CheckGym gym : allGymList) {
                if (gym.getGymId() == gymDto.getGymId()) {
                    gym.setSelect(true);
                    gymDto.setSelect(true);
                } else {
                    gym.setSelect(false);
                }
            }
            mAMap.clear();
            setMapMarkView();
            showGymView(gymDto);
        }
        return false;
    }

    private void showGymView(final CheckGymListResult.CheckGymData.CheckGym mGymDto) {
        mGymLayout.setVisibility(View.VISIBLE);
        mNameTextView.setText(mGymDto.getGymName().trim());
        mAddressTextView.setText(mGymDto.getGymAddress().trim());
        String imageUrl = mGymDto.getImg();
        if (!StringUtils.isEmpty(imageUrl)) {
            HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
        }

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LookStoreMapActivity.this, GymCoursesActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGymDto.getGymId() + "");
                intent.putExtra(LikingLessonFragment.KEY_GYM_NAME, mGymDto.getGymName());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData) {
        mStateView.setState(StateView.State.SUCCESS);
        allGymList = checkGymData.getAllGymList();
        if (allGymList != null) {
            if (allGymList.size() > 0) {
                mNoDataLayout.setVisibility(View.GONE);
                mGymLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < allGymList.size(); i++) {
                    if (i == 0) {
                        allGymList.get(i).setSelect(true);
                    } else {
                        allGymList.get(i).setSelect(false);
                    }
                }
                setMapMarkView();
                if (!isLoaction) {//如果没有开启定位重新设置中心点
                    //因为没有定位，所以设置获取到的数据中第一个数据为该城市的中心点
                    LatLng latLng = new LatLng(allGymList.get(0).getLatitude(), allGymList.get(0).getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(latLng);
                    mAMap.moveCamera(cameraUpdate);
                }
                showGymView(allGymList.get(0));
            } else {
                mGymLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mGymLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
        }
    }


    //设置所有门店覆盖物
    private void setMapMarkView() {
        for (CheckGymListResult.CheckGymData.CheckGym gym : allGymList) {
            setMapMark(gym);
        }
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
