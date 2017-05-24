package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyDishesOrderView;
import com.goodchef.liking.module.data.local.LikingPreference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 上午10:22
 */
public class MyDishesOrderPresenter extends BasePresenter<MyDishesOrderView> {
    public MyDishesOrderPresenter(Context context, MyDishesOrderView mainView) {
        super(context, mainView);
    }

    public void myDishesOrderPay(String orderId, String payType) {
        LiKingApi.myDishesOrderPay(orderId, payType, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyDishesPayView(result.getPayData());
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

    public void cancelMyDishesOrder(String orderId){
        LiKingApi.cancelMyDishesOrder(LikingPreference.getToken(), orderId, new RequestUiLoadingCallback<LikingResult>(mContext,R.string.loading) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)){
                    mView.updateCancelDishesOrder();
                }else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    public void completeDishesOrder(String orderId){
        LiKingApi.completeMyDishesOrder(LikingPreference.getToken(), orderId, new RequestUiLoadingCallback<LikingResult>(mContext,R.string.loading) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)){
                    mView.updateCompleteDishesOrder();
                }else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
