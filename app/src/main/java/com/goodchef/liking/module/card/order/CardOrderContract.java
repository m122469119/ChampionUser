package com.goodchef.liking.module.card.order;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.OrderCardListResult;
import com.goodchef.liking.data.remote.retrofit.result.data.OrderCardData;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.card.CardModel;

import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午11:18
 */

interface CardOrderContract {

    interface View extends BaseView {
        void updateCardOrderListView(List<OrderCardData> listData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CardModel mCardModel;

        public Presenter() {
            mCardModel = new CardModel();
        }

        void getCardOrderList(int page) {
            mCardModel.getCardOrderList(page)
                    .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<OrderCardListResult>(mView) {

                        @Override
                        public void onNext(OrderCardListResult result) {
                            super.onNext(result);
                            if(null == result) return;
                            List<OrderCardData> listData = result.getData().getOrderCardList();
                            mView.updateCardOrderListView(listData);
                        }
                    }));
        }
    }
}
