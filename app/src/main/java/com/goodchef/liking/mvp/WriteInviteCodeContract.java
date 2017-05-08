package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:42
 * version 1.0.0
 */

public interface WriteInviteCodeContract {

    interface WriteInviteCodeView extends BaseView {
        void updateWriteInviteCodeView();

        void updateErrorPromptView(String message);

    }

    class WriteInviteCodePresenter extends BasePresenter<WriteInviteCodeView> {

        public WriteInviteCodePresenter(Context context, WriteInviteCodeView mainView) {
            super(context, mainView);
        }

        public void sendConfirmRequest(String writeCode) {
            LiKingApi.exchangeInviteCode(Preference.getToken(), writeCode, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading) {
                @Override
                public void onSuccess(LikingResult likingResult) {
                    super.onSuccess(likingResult);
                    if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                        mView.showToast(mContext.getString(R.string.exchange_success));
                        mView.updateWriteInviteCodeView();
                    } else {
                        mView.updateErrorPromptView(likingResult.getMessage());

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
