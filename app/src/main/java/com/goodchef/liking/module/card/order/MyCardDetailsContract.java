package com.goodchef.liking.module.card.order;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午6:24
 */

public interface MyCardDetailsContract {
    interface MyCardDetailsView extends BaseStateView {
        void updateMyCardDetailsView(MyOrderCardDetailsResult.OrderCardDetailsData orderCardDetailsData);
    }

    class MyCardDetailsPresenter extends BasePresenter<MyCardDetailsView> {

        private CardModel mCardModel;

        public MyCardDetailsPresenter(Context context, MyCardDetailsView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void getMyCardDetails(String orderId) {
            mCardModel.getMyOrderCardDetails(orderId)
                    .subscribe(new LikingBaseObserver<MyOrderCardDetailsResult>(mContext, mView) {
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
                    });
        }
    }
}
