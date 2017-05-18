package com.goodchef.liking.module.card.my;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:54
 */

public interface MyCardContract {

    interface MyCardView extends BaseStateView {
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
                    .subscribe(new LikingBaseObserver<MyCardResult>(mContext, mView) {
                        @Override
                        public void onNext(MyCardResult result) {
                            mView.updateMyCardView(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                    });
        }
    }
}
