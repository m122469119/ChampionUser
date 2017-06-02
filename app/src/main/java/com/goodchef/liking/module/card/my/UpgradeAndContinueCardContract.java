package com.goodchef.liking.module.card.my;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.card.CardModel;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:21
 */

interface UpgradeAndContinueCardContract {

    interface View extends BaseStateView {
        void updateCardListView(CardResult.CardData cardData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CardModel mCardModel;

        public Presenter() {
            mCardModel = new CardModel();
        }

        void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
            mCardModel.getCardList(longitude, latitude, cityId, districtId, gymId, type)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CardResult>(mView) {

                        @Override
                        public void onNext(CardResult result) {
                            if(null == result) return;
                            mView.updateCardListView(result.getCardData());
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
    }

}
