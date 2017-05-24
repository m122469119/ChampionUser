package com.goodchef.liking.module.card.my;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.data.remote.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:21
 */

public interface UpgradeAndContinueCardContract {

    interface CardListView extends BaseStateView {
        void updateCardListView(CardResult.CardData cardData);
    }

    class CardListPresenter extends BasePresenter<CardListView> {

        private CardModel mCardModel;

        public CardListPresenter(Context context, CardListView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
            mCardModel.getCardList(longitude, latitude, cityId, districtId, gymId, type)
                    .subscribe(new LikingBaseObserver<CardResult>(mContext, mView) {
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
                    });
        }
    }

}
