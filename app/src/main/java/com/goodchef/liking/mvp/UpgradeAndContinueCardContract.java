package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.CardModel;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午10:21
 */

public interface UpgradeAndContinueCardContract {

    interface CardListView extends BaseNetworkLoadView {
        void updateCardListView(CardResult.CardData cardData);
        void showToast(String message);
    }

    class CardListPresenter extends BasePresenter<CardListView> {

        private CardModel mCardModel;

        public CardListPresenter(Context context, CardListView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void getCardList(String longitude, String latitude, String cityId, String districtId, String gymId, int type) {
            mCardModel.getCardList(longitude, latitude, cityId, districtId, gymId, type, new RequestCallback<CardResult>() {
                @Override
                public void onSuccess(CardResult result) {
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateCardListView(result.getCardData());
                    } else {
                        mView.showToast(result.getMessage());
                    }
                }

                @Override
                public void onFailure(RequestError error) {
                    mView.handleNetworkFailure();
                }
            });
        }
    }

}
