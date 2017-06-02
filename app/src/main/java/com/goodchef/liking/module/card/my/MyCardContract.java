package com.goodchef.liking.module.card.my;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.MyCardResult;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:54
 */

public interface MyCardContract {

    interface View extends BaseStateView {
        void updateMyCardView(MyCardResult.MyCardData myCardData);

    }

    class Presenter extends RxBasePresenter<View> {

        private CardModel mCardModel = null;

        public Presenter() {
            mCardModel = new CardModel();
        }

        public void sendMyCardRequest() {
            mCardModel.getMyCard()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<MyCardResult>(mView) {

                        @Override
                        public void onNext(MyCardResult result) {
                            if(result == null) return;
                            mView.updateMyCardView(result.getData());
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
