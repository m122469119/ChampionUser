package com.goodchef.liking.module.card.my;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.module.card.CardModel;

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
                    .subscribe(new BaseRequestObserver<MyCardResult>() {
                        @Override
                        public void onNext(MyCardResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateMyCardView(result.getData());
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
    }
}
