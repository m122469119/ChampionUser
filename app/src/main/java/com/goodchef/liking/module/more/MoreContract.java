package com.goodchef.liking.module.more;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 17/3/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

interface MoreContract {
    interface View extends BaseView {
        void updateCheckUpdateAppView(CheckUpdateAppResult.UpdateAppData updateAppData);

        void updateLoginOut();
    }

    class Presenter extends RxBasePresenter<View> {
        private MoreModel mMoreModel;

        Presenter() {
            mMoreModel = new MoreModel();
        }

        /**
         * 检查应用更新
         */
        void checkAppUpdate() {
            mMoreModel.getCheckUpdateAppResult()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CheckUpdateAppResult>(mView) {

                        @Override
                        public void onNext(CheckUpdateAppResult result) {
                            if (result == null) return;
                            mMoreModel.saveUpdateInfo(result.getData());
                            mView.updateCheckUpdateAppView(result.getData());
                        }

                    }));
        }

        /**
         * 用户登出
         * @param context
         */
        void loginOut(Context context) {
            mMoreModel.userLogout(LikingNewApi.sHostVersion, LikingPreference.getToken(), "")
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult likingResult) {
                            mView.updateLoginOut();
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.LOGOUT_FAILURE:
                                    break;
                                default:
                            }
                            super.apiError(apiException);
                        }
                    }));
        }
    }
}
