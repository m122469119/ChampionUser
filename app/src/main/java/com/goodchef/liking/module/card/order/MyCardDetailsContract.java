package com.goodchef.liking.module.card.order;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.MyOrderCardDetailsResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.card.CardModel;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午6:24
 */

interface MyCardDetailsContract {
    interface View extends BaseStateView {
        void updateMyCardDetailsView(MyOrderCardDetailsResult.OrderCardDetailsData orderCardDetailsData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CardModel mCardModel;

        public Presenter() {
            mCardModel = new CardModel();
        }

        void getMyCardDetails(String orderId) {
            mCardModel.getMyOrderCardDetails(orderId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<MyOrderCardDetailsResult>(mView) {

                        @Override
                        public void onNext(MyOrderCardDetailsResult result) {
                            if (null == result) return;
                            mView.updateMyCardDetailsView(result.getData());
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
