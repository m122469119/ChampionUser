package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.data.OrderCardData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.OrderModel;

import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 上午11:18
 */

public interface CardOrderContract {

    interface CardOrderView extends BaseView {
        void updateCardOrderListView(List<OrderCardData> listData);
        void showToast(String message);
    }

    class CardOrderPresenter extends BasePresenter<CardOrderView> {

        private OrderModel mOrderModel;
        private BasePagerLoaderFragment mFragment;

        public CardOrderPresenter(Context context, CardOrderView mainView, BasePagerLoaderFragment fragment) {
            super(context, mainView);
            mFragment = fragment;
            mOrderModel = new OrderModel();
        }

        public void getCardOrderList(int page) {
            mOrderModel.getCardOrderList(page, new PagerRequestCallback<OrderCardListResult>(mFragment) {
                @Override
                public void onSuccess(OrderCardListResult result) {
                    super.onSuccess(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        List<OrderCardData> listData = result.getData().getOrderCardList();
                        mView.updateCardOrderListView(listData);
                    } else {
                        mView.showToast(result.getMessage());

                    }
                }

                @Override
                public void onFailure(RequestError error) {
                    super.onFailure(error);
                }
            });
        }
    }
}
