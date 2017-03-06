package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.MyCardModel;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午5:54
 */

public interface MyCardContract {

    interface MyCardView extends BaseNetworkLoadView {
        void updateMyCardView(MyCardResult.MyCardData myCardData);
        void showToast(String message);
    }

    class MyCardPresenter extends BasePresenter<MyCardView> {

        private MyCardModel mMyCardModel;

        public MyCardPresenter(Context context, MyCardView mainView) {
            super(context, mainView);
            mMyCardModel = new MyCardModel();
        }

        public void sendMyCardRequest() {
            mMyCardModel.getMyCard(new RequestCallback<MyCardResult>() {
                @Override
                public void onSuccess(MyCardResult result) {
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateMyCardView(result.getData());
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
