package com.goodchef.liking.module.gym.list;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;
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

public interface GymListContract {

    interface CheckGymView extends BaseNetworkLoadView {
        void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData);
    }

    class CheckGymPresenter extends BasePresenter<CheckGymView> {

        private String selectCityId;//选择的城市id
        private String gymId;//场馆id
        private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡
        private boolean islocation;

        private String longitude = "0";
        private String latitude = "0";

        private List<CheckGymListResult.CheckGymData.CheckGym> allGymList;
        private CheckGymListResult.CheckGymData.MyGymData mMyGym;

        private GymModel mGymModel = null;

        public CheckGymPresenter(Context context, CheckGymView mainView) {
            super(context, mainView);
            mGymModel = new GymModel();
            setLocationData(mGymModel.getCurrLocation());
        }

        /**
         * 获取场馆列表
         */
        public void getGymList() {
            if(StringUtils.isEmpty(selectCityId)) {
                return;
            }
            mGymModel.getCheckGymList(Integer.parseInt(selectCityId), longitude, latitude)
                    .subscribe(new LikingBaseObserver<CheckGymListResult>() {
                        @Override
                        public void onNext(CheckGymListResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateCheckGymView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.handleNetworkFailure();
                        }
                    });
        }

        /***
         * 设置默认选中首页所在的场馆
         */
        public void setDefaultCheck(List<CheckGymListResult.CheckGymData.CheckGym> allGymList) {
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

        public String getSelectCityId() {
            return selectCityId;
        }

        public void setSelectCityId(String selectCityId) {
            this.selectCityId = selectCityId;
        }

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
        }

        public int getTabIndex() {
            return tabIndex;
        }

        public void setTabIndex(int tabIndex) {
            this.tabIndex = tabIndex;
        }

        public boolean islocation() {
            return islocation;
        }

        public void setIslocation(boolean islocation) {
            this.islocation = islocation;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public List<CheckGymListResult.CheckGymData.CheckGym> getAllGymList() {
            if(allGymList == null) return new ArrayList<>();
            return allGymList;
        }

        public void setAllGymList(List<CheckGymListResult.CheckGymData.CheckGym> allGymList) {
            this.allGymList = allGymList;
        }

        public CheckGymListResult.CheckGymData.MyGymData getMyGym() {
            return mMyGym;
        }

        public void setMyGym(CheckGymListResult.CheckGymData.MyGymData myGym) {
            mMyGym = myGym;
        }

    }
}
