package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.MyCardView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午4:41
 */
public class MyCardPresenter extends BasePresenter<MyCardView> {
    public MyCardPresenter(Context context, MyCardView mainView) {
        super(context, mainView);
    }

    public void sendMyCardRequest() {
        LiKingApi.getMyCard(Preference.getToken(), new RequestCallback<MyCardResult>() {
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
