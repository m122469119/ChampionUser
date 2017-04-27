package com.goodchef.liking.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BuyCardConfirmActivity;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
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

    public void confirmBuyCard(int type, int categoryId, String gymId) {
        LiKingApi.confirmCard(Preference.getToken(), type, categoryId, gymId, new RequestCallback<ConfirmBuyCardResult>() {
            @Override
            public void onSuccess(ConfirmBuyCardResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateConfirmBuyCardView(result.getData());
                } else {
                    if (result.getCode() == LiKingRequestCode.BUY_CARD_CONFIRM) {
                        HBaseDialog.Builder builder = new HBaseDialog.Builder(mContext);
                        builder.setMessage(result.getMessage());
                        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mContext != null && mContext instanceof BuyCardConfirmActivity) {
                                    dialog.dismiss();
                                    postEvent(new BuyCardListMessage());
                                    ((BuyCardConfirmActivity) mContext).finish();
                                }
                            }
                        });
                        builder.create().setCancelable(false);
                        builder.create().show();
                    } else if (result.getCode() == 240000 || result.getCode() == 240001) {
                        mView.updateErrorView(result.getMessage());
                    } else {
                        mView.showToast(result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }

    public void submitBuyCardData(int cardId, int type, String couponCode, String payType, String gymId) {
        LiKingApi.submitBuyCardData(Preference.getToken(), cardId, type, couponCode, payType, gymId, new RequestUiLoadingCallback<SubmitPayResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(SubmitPayResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateSubmitPayView(result.getPayData());
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
