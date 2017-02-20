package com.goodchef.liking.mvp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.thirdparty.map.LocationListener;
import com.aaron.android.thirdparty.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.github.promeg.pinyinhelper.Pinyin;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.ChangeCityFragmentMessage;
import com.goodchef.liking.eventmessages.ChangeGymActivityMessage;
import com.goodchef.liking.http.result.CityListResult;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.ChangeCityModel;
import com.goodchef.liking.mvp.model.IChangeCityModel;
import com.goodchef.liking.mvp.view.ChangeCityView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeCityPresenter extends BasePresenter<ChangeCityView> {

    AmapGDLocation mAmapGDLocation = null;
    private IChangeCityModel mModel;

    public String currentCityName;
    public String currentCityId;
    public String longitude;
    public String latitude;


    public ChangeCityPresenter(Context context, ChangeCityView mainView) {
        super(context, mainView);
        mModel = new ChangeCityModel();
    }

    public void getCitySearch(final String search){
        if (search == null || "".equals(search)) {
            mView.dismissWindow();
            return;
        }

        new AsyncTask<Void, List<City.RegionsData.CitiesData>, List<City.RegionsData.CitiesData>>(){
            @Override
            protected List<City.RegionsData.CitiesData> doInBackground(Void... params) {
                String replace = search.replace(" ", "").toUpperCase(Locale.CHINA);
                char[] chars = replace.toCharArray();
                boolean chinese = false;
                for (int i = 0; i < chars.length; i ++) {
                    if (i == 0)
                        chinese = Pinyin.isChinese(chars[i]);
                    else if (Pinyin.isChinese(chars[i]) == chinese){
                        return null;
                    }
                }

                List<City.RegionsData.CitiesData> citiesDataList = LiKingVerifyUtils.getCitiesDataList();
                List<City.RegionsData.CitiesData> result = new ArrayList<>();

                city:for (City.RegionsData.CitiesData citiesData : citiesDataList) {
                    if (Pinyin.isChinese(chars[0])){
                        String cityName = citiesData.getCityName();
                        if (cityName.contains(replace)){
                            result.add(citiesData);
                            continue;
                        }
                    } else {
//                        String pinyin = citiesData.getPinyin();
//                        String[] split = pinyin.split(",");
//                        String re = replace;
//                        for (String s : split){
//                            if (replace.length() > 0) {
//                                if (s.substring(0, 1).toUpperCase().equals(re.substring(0, 1))) {
//                                    re = re.substring(1, re.length());
//                                } else {
//                                    continue city;
//                                }
//                            }
//                        }
                        String pinyin = citiesData.getPinyin();
                        pinyin = pinyin.replace(",", "");

                        if (pinyin.contains(replace)) {
                            result.add(citiesData);
                            continue;
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<City.RegionsData.CitiesData> citiesDatas) {
                mView.showCityListWindow(citiesDatas);
            }
        }.execute();
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

    public void getCityList() {
        mModel.getCityList(new RequestCallback<CityListResult>() {
            @Override
            public void onSuccess(CityListResult result) {
                LiKingVerifyUtils.loadOpenCitysInfo(mContext, result.getData().getOpen_city());
                ChangeCityFragmentMessage message = ChangeCityFragmentMessage
                        .obtain(ChangeCityFragmentMessage.REFRESH_LIST_DATA);
                mView.postEvent(message);
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
