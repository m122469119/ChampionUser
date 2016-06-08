package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;

import com.goodchef.liking.R;

/**
 * 说明:查看场馆
 * Author shaozucheng
 * Time:16/6/7 下午5:49
 */
public class LookStoreMapActivity extends AppBarActivity{

  //  MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_store);
        initData();
        setTitle("上海");
     //   mMapView.onCreate(savedInstanceState);
    }

    private void initData() {
        initView();

//        AMap aMap = mMapView.getMap();
//        //绘制marker
//        Marker marker = aMap.addMarker(new MarkerOptions()
//                .position(new LatLng(39.986919,116.353369))
//                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(),R.drawable.marker)))
//                .draggable(true));
    }

    private void initView() {
      //  mMapView = (MapView) findViewById(R.id.store_map);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  mMapView.onDestroy();
    }
}
