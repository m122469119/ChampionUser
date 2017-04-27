package com.goodchef.liking.module.more;

import android.content.Context;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.LikingBaseRequestObserver;
import com.goodchef.liking.module.base.ProgressObserver;
import com.goodchef.liking.storage.Preference;

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
                    .subscribe(new LikingBaseRequestObserver<CheckUpdateAppResult>() {
                        @Override
                        public void onNext(CheckUpdateAppResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mMoreModel.saveUpdateInfo(result.getData());
                                mView.updateCheckUpdateAppView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }

        /**
         * 用户登出
         */
        void loginOut() {
            mMoreModel.userLogout(UrlList.sHostVersion, Preference.getToken(), "")
                    .subscribe(new ProgressObserver<BaseResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(BaseResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mMoreModel.clearUserInfo();
                                mView.updateLoginOut();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }
    }
}
