package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.ConfirmBuyCardView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:48
 */
public class ConfirmBuyCardPresenter extends BasePresenter<ConfirmBuyCardView> {
    public ConfirmBuyCardPresenter(Context context, ConfirmBuyCardView mainView) {
        super(context, mainView);
    }

    public void confirmBuyCard(int type, int categoryId) {
        LiKingApi.confirmCard(Preference.getToken(), type, categoryId, new RequestUiLoadingCallback<ConfirmBuyCardResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(ConfirmBuyCardResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateConfirmBuyCardView(result.getData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    public void submitBuyCardData(int cardId, int type, String couponCode, String payType) {
        LiKingApi.submitBuyCardData(Preference.getToken(), cardId, type, couponCode, payType, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSubmitPayView(result.getPayData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
