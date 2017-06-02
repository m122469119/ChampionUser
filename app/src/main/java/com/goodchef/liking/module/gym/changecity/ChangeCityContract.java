package com.goodchef.liking.module.gym.changecity;

import android.content.Context;
import android.os.AsyncTask;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.map.LocationListener;
import com.aaron.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.github.promeg.pinyinhelper.Pinyin;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LikingBaseRequestHelper;
import com.goodchef.liking.eventmessages.ChangeCityFragmentMessage;
import com.goodchef.liking.eventmessages.ChangeGymActivityMessage;
import com.goodchef.liking.data.remote.retrofit.result.CityListResult;
import com.goodchef.liking.data.remote.retrofit.result.data.City;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.gym.GymModel;
import com.goodchef.liking.utils.CityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 2017/3/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

interface ChangeCityContract {

    interface View extends BaseView{


        void showCityListWindow(List<City.RegionsData.CitiesData> list);

        void dismissWindow();

        void setLocationCityNameTextViewText(String text);

        CharSequence getLocationCityNameTextViewText();

        void setTitle(String text);

        void postEvent(BaseMessage object);

        void finish();

        String getDefaultCityName();
    }

    class Presenter extends RxBasePresenter<View> {

        private static final String TAG = "ChangeCityContractPresenter";
        private AmapGDLocation mAmapGDLocation = null;
        private GymModel mModel;

        String currentCityName;
        public String longitude;
        public String latitude;
        private boolean isLocation = false;


        public Presenter() {
            mModel = new GymModel();
        }

        void getCitySearch(final String search) {
            if (search == null || "".equals(search)) {
                mView.dismissWindow();
                return;
            }

            new AsyncTask<Void, List<City.RegionsData.CitiesData>, List<City.RegionsData.CitiesData>>() {
                @Override
                protected List<City.RegionsData.CitiesData> doInBackground(Void... params) {
                    String replace = search.replace(" ", "").toUpperCase(Locale.CHINA);
                    char[] chars = replace.toCharArray();
                    boolean chinese = false;
                    for (int i = 0; i < chars.length; i++) {
                        if (i == 0)
                            chinese = Pinyin.isChinese(chars[i]);
                        else if (Pinyin.isChinese(chars[i]) != chinese)
                            return null;
                    }
                    List<City.RegionsData.CitiesData> citiesDataList = LikingBaseRequestHelper.getCitiesDataList();
                    List<City.RegionsData.CitiesData> result = new ArrayList<>();

                    for (City.RegionsData.CitiesData citiesData : citiesDataList) {
                        if (Pinyin.isChinese(chars[0])) {
                            if (compareStrings(citiesData.getCityName(), replace)) {
                                result.add(citiesData);
                            }
                        } else {
                            String pinyin = citiesData.getPinyin().replace(",", "");
                            if (compareStrings(pinyin, replace)) {
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

        boolean compareStrings(String A, String B) {
            // write your code here
            if (null == A || null == B || A.length() < B.length()) {
                return false;
            }
            if (A.equals(B)) {
                return true;
            }

            char[] charsA = A.toCharArray();
            char[] charsB = B.toCharArray();

            if (!String.valueOf(charsA[0]).equals(String.valueOf(charsB[0]))) {
                return false;
            }

            for (char aCharsB : charsB) {
                if (A.length() == 0) {
                    return false;
                }
                String s = String.valueOf(aCharsB);
                if (!A.contains(s)) {
                    return false;
                }
                A = A.substring(A.indexOf(s) + 1, A.length());
            }
            return true;
        }


        /***
         * 初始化定位
         */
        void startLocation(final Context context) {
            if (mAmapGDLocation != null && mAmapGDLocation.isStart())
                return;
            if (mAmapGDLocation == null) {
                mAmapGDLocation = new AmapGDLocation(context);
                LogUtils.i("cdust", "...new AmapGDLocation(mContext)....");
                mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
                    @Override
                    public void receive(AMapLocation object) {
                        if (object != null && object.getErrorCode() == 0) {//定位成功
                            isLocation = true;
                            currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                            longitude = CityUtils.getLongitude(context, object.getCityCode(), object.getLongitude());
                            latitude = CityUtils.getLatitude(context, object.getCityCode(), object.getLatitude());
                            String cityId = CityUtils.getCityId(context, object.getCityCode());
                            String districtId = object.getAdCode();

                            mView.setLocationCityNameTextViewText(currentCityName);
                            mView.setTitle(currentCityName);
                            saveLocationInfo(cityId, districtId, longitude, latitude, currentCityName, true);
                            LogUtils.i("cdust", "....定位成功..." + currentCityName + cityId + districtId);
                        } else {//定位失败
                            isLocation = false;
                            mView.setLocationCityNameTextViewText(context.getString(R.string.re_location));
                            mView.setTitle(mView.getDefaultCityName());

                            String cityId = CityUtils.getCityId(context, object.getCityCode());
                            String districtId = CityUtils.getDistrictId(context, object.getCityCode(), object.getDistrict());
                            saveLocationInfo(cityId, districtId, "0", "0", currentCityName, false);
                        }
                    }

                    @Override
                    public void start() {
                        mView.setLocationCityNameTextViewText("定位中...");
                        mView.setTitle("定位中...");
                    }

                    @Override
                    public void end() {
                        LogUtils.i("cdust", "定位结束...");
                    }
                });
            }
            mAmapGDLocation.start();
        }

        private void saveLocationInfo(String cityId, String districtId, String longitude, String latitude, String cityName, boolean isLocation) {
            LocationData locationData = new LocationData(cityId, districtId, longitude, latitude, cityName, isLocation);
            LikingPreference.setLocationData(locationData);
        }

        void stopLocation() {
            if (mAmapGDLocation != null) {
                mAmapGDLocation.destroy();
            }
        }

        void destroyLocation() {
            if (mAmapGDLocation != null)
                mAmapGDLocation.destroy();
            mAmapGDLocation =null;
        }

        void onLocationTextClick(Context context) {
            String locationText = mView.getLocationCityNameTextViewText().toString();
            if (locationText.equals(context.getString(R.string.re_location))) {
                if (mAmapGDLocation != null){
                    mAmapGDLocation.destroy();
                    mAmapGDLocation = null;
                }

                if (!isLocation) {
                    LogUtils.i(TAG,"=----------startLocation-----");
                    startLocation(context);
                }

            } else {
                ChangeGymActivityMessage msg = ChangeGymActivityMessage
                        .obtain(ChangeGymActivityMessage.CHANGE_LEFT_CITY_TEXT);
                msg.msg1 = currentCityName;
                mView.postEvent(msg);
                mView.finish();
            }
        }

        void getCityList(final Context context) {
            mModel.getCityList()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CityListResult>(mView) {

                        @Override
                        public void onNext(CityListResult value) {
                            if (value == null) return;
                            LikingBaseRequestHelper.loadOpenCitysInfo(context, value.getData().getOpen_city());
                            ChangeCityFragmentMessage message = ChangeCityFragmentMessage
                                    .obtain(ChangeCityFragmentMessage.REFRESH_LIST_DATA);
                            mView.postEvent(message);
                        }
                    }));
        }

    }

}