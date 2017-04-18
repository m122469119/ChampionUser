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
import com.goodchef.liking.storage.Preference;

/**
 * 说明:分享
 * Author shaozucheng
 * Time:16/8/26 上午10:27
 */
public class SharePresenter extends BasePresenter<ShareView> {


    public SharePresenter(Context context, ShareView mainView) {
        super(context, mainView);
    }

    //私教课分享
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


    //团体课分享
    public void getGroupShareData(String scheduleId) {
        LiKingApi.getGroupCoursesShare(scheduleId, new RequestCallback<ShareResult>() {
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


    //我的运动数据分享
    public void getUserShareData() {
        LiKingApi.getUserShare(Preference.getToken(), new RequestCallback<ShareResult>() {
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
