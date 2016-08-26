package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.ShareResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.ShareView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 上午10:27
 */
public class SharePresenter extends BasePresenter<ShareView> {


    public SharePresenter(Context context, ShareView mainView) {
        super(context, mainView);
    }

    public void getPrivateShareData(String trainId) {
        LiKingApi.getPrivateCoursesShare(trainId, new RequestCallback<ShareResult>() {
            @Override
            public void onSuccess(ShareResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateShareView(result.getShareData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }



}
