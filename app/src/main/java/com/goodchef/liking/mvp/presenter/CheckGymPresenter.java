package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CheckGymView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/12 下午2:20
 */
public class CheckGymPresenter extends BasePresenter<CheckGymView> {

    public CheckGymPresenter(Context context, CheckGymView mainView) {
        super(context, mainView);
    }

    public void getGymList(int cityId){
        LiKingApi.getCheckGymList(cityId, new RequestUiLoadingCallback<CheckGymListResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(CheckGymListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext,result)){
                    mView.updateCheckGymView(result.getData());
                }else {
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
