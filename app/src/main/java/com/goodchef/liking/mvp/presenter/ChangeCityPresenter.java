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
                    else if (Pinyin.isChinese(chars[i]) != chinese)
                        return null;
                }
                List<City.RegionsData.CitiesData> citiesDataList = LiKingVerifyUtils.getCitiesDataList();
                List<City.RegionsData.CitiesData> result = new ArrayList<>();

                for (City.RegionsData.CitiesData citiesData : citiesDataList) {
                    if (Pinyin.isChinese(chars[0])){
                        if (compareStrings(citiesData.getCityName(), replace)){
                            result.add(citiesData);
                        }
                    } else {
                        String pinyin = citiesData.getPinyin().replace(",", "");
                        if (compareStrings(pinyin, replace)){
                            result.add(citiesData);
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<City.RegionsData.CitiesData> reslut) {
                mView.showCityListWindow(reslut);
            }
        }.execute();
    }

    public boolean compareStrings(String A, String B) {
        // write your code here
        if (null == A || null == B || A.length() < B.length()) {
            return false;
        }
        if (A.equals(B)) {
            return true;
        }

        char[] charsA = A.toCharArray();
        char[] charsB = B.toCharArray();

        if (!String.valueOf(charsA[0]).equals(String.valueOf(charsB[0]))){
            return false;
        }

        for (int i = 0; i < charsB.length; i ++){
            if (A.length() == 0){
                return false;
            }
            String s = String.valueOf(charsB[i]);
            if (!A.contains(s)){
                return false;
            }
            A = A.substring(A.indexOf(s), A.length());
        }
        return true;
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
                        mView.setTitle(currentCityName);
                    } else {//定位失败
                        mView.setLocationCityNameTextViewText(mContext.getString(R.string.re_location));
                        mView.setTitle(mView.getDefaultCityName());
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
            if (mAmapGDLocation != null)
                mAmapGDLocation.destroy();
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
