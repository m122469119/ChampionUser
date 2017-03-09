package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

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
            LiKingApi.exchangeInviteCode(Preference.getToken(), writeCode, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading) {
                @Override
                public void onSuccess(BaseResult result) {
                    super.onSuccess(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        PopupUtils.showToast(mContext.getString(R.string.exchange_success));
                        mView.updateWriteInviteCodeView();
                    } else {
                        mView.updateErrorPromptView(result.getMessage());

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
