package com.goodchef.liking.module.card.my;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.card.CardModel;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:21
 */

public interface UpgradeAndContinueCardContract {

    interface CardListView extends BaseNetworkLoadView {
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
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateCardListView(result.getCardData());
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
