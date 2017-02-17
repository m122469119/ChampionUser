package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.ChangeGymActivityMessage;
import com.goodchef.liking.http.result.data.City;
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
                        mView.setTitle(mContext.getString(R.string.location_fail));
                    }
                }
                @Override
                public void start() {
                    mView.setLocationCityNameTextViewText("定位中...");
                    mView.setTitle("定位中...");
                }
                @Override
                public void end() {
                    LogUtils.i("dust", "定位结束...");
                }
            });
        }
        mAmapGDLocation.start();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAmapGDLocation != null) {
            mAmapGDLocation.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAmapGDLocation != null)
            mAmapGDLocation.destroy();
    }

    public void onLocationTextClick() {
        String locationText = mView.getLocationCityNameTextViewText().toString();
        if (locationText.equals(mContext.getString(R.string.re_location))){
            startLocation();
        } else {
            ChangeGymActivityMessage msg = ChangeGymActivityMessage
                    .obtain(ChangeGymActivityMessage.CHANGE_LEFT_CITY_TEXT);
            msg.msg1 = currentCityName;
            mView.postEvent(msg);
            mView.finish();
        }
    }

}
