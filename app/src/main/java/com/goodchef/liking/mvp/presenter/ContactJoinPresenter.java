package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.ContactJoinView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 下午2:33
 */
public class ContactJoinPresenter extends BasePresenter<ContactJoinView> {
    public ContactJoinPresenter(Context context, ContactJoinView mainView) {
        super(context, mainView);
    }

    public void joinAllpy(String name, String phone, String city, int type) {
        LiKingApi.joinApply(name, phone, city, type, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateContactJoinView();
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
