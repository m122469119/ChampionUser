package com.goodchef.liking.module.card.order;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.data.OrderCardData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.LikingBaseRequestObserver;
import com.goodchef.liking.module.card.CardModel;

import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午11:18
 */

public interface CardOrderContract {

    interface CardOrderView extends BaseView {
        void updateCardOrderListView(List<OrderCardData> listData);
    }

    class CardOrderPresenter extends BasePresenter<CardOrderView> {

        private CardModel mCardModel;

        public CardOrderPresenter(Context context, CardOrderView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void getCardOrderList(int page) {
            mCardModel.getCardOrderList(page)
            .subscribe(new LikingBaseRequestObserver<OrderCardListResult>() {
                @Override
                public void onNext(OrderCardListResult result) {
                    super.onNext(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        List<OrderCardData> listData = result.getData().getOrderCardList();
                        mView.updateCardOrderListView(listData);
                    } else {
                        mView.showToast(result.getMessage());

                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
    }
}
