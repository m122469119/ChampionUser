package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyUserOtherInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyUserInfoOtherView;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:58
 * version 1.0.0
 */

public class MyUserInfoOtherPresenter extends BasePresenter<MyUserInfoOtherView> {
    public MyUserInfoOtherPresenter(Context context, MyUserInfoOtherView mainView) {
        super(context, mainView);
    }

    public void getMyserInfoOther() {
        LiKingApi.getMyUserInfoData(new RequestCallback<MyUserOtherInfoResult>() {
            @Override
            public void onSuccess(MyUserOtherInfoResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyUserInfoOtherView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }
}
