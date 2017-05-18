package com.goodchef.liking.module.more;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 17/3/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class MoreContract {
    interface MoreView extends BaseView {
        void updateCheckUpdateAppView(CheckUpdateAppResult.UpdateAppData updateAppData);

        void updateLoginOut();
    }

    static class MorePresenter extends BasePresenter<MoreView> {
        private MoreModel mMoreModel;

        MorePresenter(Context context, MoreView mainView) {
            super(context, mainView);
            mMoreModel = new MoreModel();
        }

        /**
         * 检查应用更新
         */
        void checkAppUpdate() {
            mMoreModel.getCheckUpdateAppResult()
                    .subscribe(new LikingBaseObserver<CheckUpdateAppResult>(mContext, mView) {
                        @Override
                        public void onNext(CheckUpdateAppResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mMoreModel.saveUpdateInfo(result.getData());
                                mView.updateCheckUpdateAppView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                    });
        }

        /**
         * 用户登出
         */
        void loginOut() {
            mMoreModel.userLogout(UrlList.sHostVersion, LikingPreference.getToken(), "")
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                                mMoreModel.clearUserInfo();
                                mView.updateLoginOut();
                            } else {
                                mView.showToast(likingResult.getMessage());
                            }
                        }

                    });
        }
    }
}
