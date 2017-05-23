package com.goodchef.liking.module.card.my;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;

import io.reactivex.functions.Consumer;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:54
 */

public interface MyCardContract {

    interface MyCardView extends BaseNetworkLoadView {
        void updateMyCardView(MyCardResult.MyCardData myCardData);
    }

    class MyCardPresenter extends BasePresenter<MyCardView> {

        private CardModel mCardModel = null;

        public MyCardPresenter(Context context, MyCardView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void sendMyCardRequest() {
            mCardModel.getMyCard()
                    .doOnNext(new Consumer<MyCardResult>() {
                        @Override
                        public void accept(MyCardResult myCardResult) throws Exception {

                        }
                    })
                    .subscribe(new LikingBaseObserver<MyCardResult>(mContext, mView) {
                        @Override
                        public void onNext(MyCardResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateMyCardView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.handleNetworkFailure();
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.handleNetworkFailure();
                        }
                    });
        }
    }
}
