package com.goodchef.liking.module.card.buy;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.retrofit.result.data.GymData;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.module.home.LikingHomeActivity;

/**
 * Created on 2017/05/19
 * desc: 购卡列表
 *
 * @author: chenlei
 * @version:1.0
 */

interface BuyCardContract {

    interface View extends BaseStateView {
        void updateCardListView(CardResult.CardData cardData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CardModel mCardModel = null;
        private String longitude = "0";
        private String latitude = "0";
        private String cityId = "310100";
        private String districtId = "310104";

        private GymData mGymData;

        public Presenter() {
            mCardModel = new CardModel();
        }

        void getCardList(int buyType) {
            getLocationData();
            if (longitude.equals("0.0") || latitude.equals("0.0")) {
                getCardList("0", "0", cityId, districtId, LikingHomeActivity.gymId, buyType);
            } else {
                getCardList(longitude, latitude, cityId, districtId, LikingHomeActivity.gymId, buyType);
            }
        }

        private void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
            mCardModel.getCardList(longitude, latitude, cityId, districtId, gymId, type)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CardResult>(mView) {

                        @Override
                        public void onNext(CardResult value) {
                            if (value == null) return;
                            mView.updateCardListView(value.getCardData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        void getLocationData() {
            LocationData locationData = LikingPreference.getLocationData();
            if (locationData != null) {
                longitude = locationData.getLongitude() + "";
                latitude = locationData.getLatitude() + "";
                cityId = locationData.getCityId();
                districtId = locationData.getDistrictId();
            }
        }

        public String getGymId() {
            if (mGymData != null) {
                return mGymData.getGymId();
            }
            return "";
        }

        public GymData getGymData() {
            return mGymData;
        }

        void setGymData(GymData gymData) {
            mGymData = gymData;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

    }
}
