package com.goodchef.liking.module.gym.list;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.CheckGymListResult;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.gym.GymModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/05/15
 * desc: 场馆列表
 *
 * @author: chenlei
 * @version:1.0
 */

interface GymListContract {

    interface View extends BaseStateView {
        void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData);
    }

    class Presenter extends RxBasePresenter<View> {

        private String cityId;//选择的城市id
        private String gymId;//场馆id
        private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡
        private boolean islocation;

        private String longitude = "0";
        private String latitude = "0";

        private List<CheckGymListResult.CheckGymData.CheckGym> allGymList;
        private CheckGymListResult.CheckGymData.MyGymData mMyGym;

        private GymModel mGymModel = null;

        public Presenter() {
            mGymModel = new GymModel();
            setLocationData(mGymModel.getCurrLocation());
        }

        /**
         * 获取场馆列表
         */
        void getGymList() {
            if (StringUtils.isEmpty(cityId)) {
                return;
            }
            mGymModel.getCheckGymList(Integer.parseInt(cityId), longitude, latitude)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CheckGymListResult>(mView) {

                        @Override
                        public void onNext(CheckGymListResult result) {
                            if (result == null) return;
                            mView.updateCheckGymView(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        /***
         * 设置默认选中首页所在的场馆
         */
        void setDefaultCheck(List<CheckGymListResult.CheckGymData.CheckGym> allGymList) {
            for (int i = 0; i < allGymList.size(); i++) {
                if (i == 0) {
                    if (islocation) {
                        allGymList.get(0).setIslocation(true);
                    } else {
                        allGymList.get(0).setIslocation(false);
                    }
                    allGymList.get(0).setReCently(true);
                }
                if (String.valueOf(allGymList.get(i).getGymId()).equals(gymId)) {
                    allGymList.get(i).setSelect(true);
                } else {
                    allGymList.get(i).setSelect(false);
                }
            }
        }

        /**
         * 当前位置
         */
        private void setLocationData(LocationData locationData) {
            setLongitude(locationData.getLongitude());
            setLatitude(locationData.getLatitude());
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String selectCityId) {
            this.cityId = selectCityId;
        }

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
        }

        int getTabIndex() {
            return tabIndex;
        }

        void setTabIndex(int tabIndex) {
            this.tabIndex = tabIndex;
        }

        public boolean islocation() {
            return islocation;
        }

        void setIslocation(boolean islocation) {
            this.islocation = islocation;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        List<CheckGymListResult.CheckGymData.CheckGym> getAllGymList() {
            if (allGymList == null) return new ArrayList<>();
            return allGymList;
        }

        void setAllGymList(List<CheckGymListResult.CheckGymData.CheckGym> allGymList) {
            this.allGymList = allGymList;
        }

        CheckGymListResult.CheckGymData.MyGymData getMyGym() {
            return mMyGym;
        }

        void setMyGym(CheckGymListResult.CheckGymData.MyGymData myGym) {
            mMyGym = myGym;
        }

    }
}
