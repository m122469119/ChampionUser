package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.mvp.model.AmapGDLocationModel;
import com.goodchef.liking.mvp.model.ChangeGymModel;

import java.util.List;

/**
 * Created on 2017/3/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface ChangeGymContract {

    interface ChangeGymView extends BaseView{
        void setRightTitleTextViewText(String text);

        void setCityHeadTextTextColor(int color);

        void setCityHeadView();
    }

    class ChangeGymPresenter extends BasePresenter<ChangeGymView>{

        ChangeGymModel mModel;
        AmapGDLocationModel mAmapGDLocationModel;

        private boolean isLocation;//是否定位
        private boolean isSecondLocation = false;

        private String selectCityName;//选择的城市名称
        private String cityId;//选择的城市id

        public ChangeGymPresenter(Context context, ChangeGymView mainView) {
            super(context, mainView);
            mModel = new ChangeGymModel();
            mAmapGDLocationModel = new AmapGDLocationModel();
        }

        public void initTitleLocation(){
            mAmapGDLocationModel.initTitleLocation(mContext, new AmapGDLocationModel.Callback() {
                @Override
                public void receive(AMapLocation object) {
                    if (object != null && object.getErrorCode() == 0) {//定位成功
                        isLocation = true;
                        if (isSecondLocation) {
                            postEvent(new RefreshChangeCityMessage(doLocationCity(),
                                    mAmapGDLocationModel.longitude, mAmapGDLocationModel.latitude));
                        }
                    } else {//定位失败
                        isLocation = false;
                    }
                    mView.setCityHeadView();
                }

                @Override
                public void start() {
                    isLocation = false;
                }

                @Override
                public void end() {

                }
            });
        }

        public void compareCurrentCity(String cityName) {
            if (!StringUtils.isEmpty(mAmapGDLocationModel.currentCityName)) {
                if (mAmapGDLocationModel.currentCityName.equals(cityName) || mAmapGDLocationModel.currentCityName.contains(cityName)) {
                    mView.setCityHeadTextTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                } else {
                    mView.setCityHeadTextTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
                }
            }
        }

        public String doLocationCity() {
            return mModel.doLocationCity(mAmapGDLocationModel.currentCityName,
                    mAmapGDLocationModel.currentCityId);
        }

        public void stopLocation() {
            if (mAmapGDLocationModel.mAmapGDLocation != null) {
                mAmapGDLocationModel.mAmapGDLocation.stop();
            }
        }

        public void detroyLocation() {
            if (mAmapGDLocationModel.mAmapGDLocation != null) {
                mAmapGDLocationModel.mAmapGDLocation.destroy();
            }
        }

        public boolean isLocation() {
            return isLocation;
        }

        public boolean isSecondLocation(){
            return isSecondLocation;
        }

        public String getCurrentCityName() {
            return mAmapGDLocationModel.currentCityName;
        }

        public String getLatitude() {
            return mAmapGDLocationModel.latitude;
        }

        public String getLongitude() {
            return mAmapGDLocationModel.longitude;
        }

        public void setIsSecondLocation(boolean b) {
            isSecondLocation = b;
        }

        public void setIsLoaction(boolean b) {
            isLocation = b;
        }

        public List<CityData> getCityDataList() {
            return mModel.cityDataList;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public void setSelectCityName(String selectCityName) {
            this.selectCityName = selectCityName;
        }

        public String getSelectCityName() {
            return selectCityName;
        }

        public String getCityId() {
            return cityId;
        }

    }
}
