package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyCardDetailsView;
import com.goodchef.liking.module.data.local.LikingPreference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午3:10
 */
public class MyCardDetailsPresenter extends BasePresenter<MyCardDetailsView> {
    public MyCardDetailsPresenter(Context context, MyCardDetailsView mainView) {
        super(context, mainView);
    }

    public void getCardDetails(String orderId) {
        LiKingApi.getCardDetails(LikingPreference.getToken(), orderId, new RequestCallback<MyOrderCardDetailsResult>() {
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
