package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.mvp.view.ChangeCityView;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeCityPresenter extends BasePresenter<ChangeCityView> {

    AmapGDLocation mAmapGDLocation = null;

    public String currentCityName;
    public String currentCityId;
    public String longitude;
    public String latitude;

    public ChangeCityPresenter(Context context, ChangeCityView mainView) {
        super(context, mainView);
    }

    public void getCitySearch(String search){

    }

    /***
     * 初始化定位
     */
    public void startLocation() {
        if (mAmapGDLocation != null && mAmapGDLocation.isStart())
            return;
        if (mAmapGDLocation == null){
            mAmapGDLocation = new AmapGDLocation(mContext);
            mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
                @Override
                public void receive(AMapLocation object) {
                    if (object != null && object.getErrorCode() == 0) {//定位成功
                        currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                        currentCityId = object.getCityCode();
                        longitude = object.getLongitude() + "";
                        latitude = object.getLatitude() + "";
                        mView.setLocationCityNameTextViewText(currentCityName);
                        mView.setTitle(mContext.getString(R.string.current_city) + currentCityName);
                    } else {//定位失败
                        mView.setLocationCityNameTextViewText(mContext.getString(R.string.re_location));
                        mView.setTitle(mContext.getString(R.string.current_city));
                    }
                }
                @Override
                public void start() {
                }
                @Override
                public void end() {
                    LogUtils.i("dust", "定位结束...");
                }
            });
        }
        mAmapGDLocation.start();
    }

}
