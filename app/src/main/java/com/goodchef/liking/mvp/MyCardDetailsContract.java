package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.CardModel;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午6:24
 */

public interface MyCardDetailsContract {
    interface MyCardDetailsView extends BaseNetworkLoadView {
        void updateMyCardDetailsView(MyOrderCardDetailsResult.OrderCardDetailsData orderCardDetailsData);
        void showToast(String message);
    }

    class MyCardDetailsPresenter extends BasePresenter<MyCardDetailsView> {

        private CardModel mCardModel;

        public MyCardDetailsPresenter(Context context, MyCardDetailsView mainView) {
            super(context, mainView);
            mCardModel = new CardModel();
        }

        public void getCardDetails(String orderId) {
            mCardModel.getCardDetails(orderId, new RequestCallback<MyOrderCardDetailsResult>() {
                @Override
                public void onSuccess(MyOrderCardDetailsResult result) {
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateMyCardDetailsView(result.getData());
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
