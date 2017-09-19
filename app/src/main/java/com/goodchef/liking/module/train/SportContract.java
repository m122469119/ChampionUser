package com.goodchef.liking.module.train;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;
import com.goodchef.liking.utils.ShareUtils;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

interface SportContract {
    interface View extends BaseStateView {
    }

    class Presenter extends RxBasePresenter<View> {
        ShareModel mShareModel;

        public Presenter() {
            mShareModel = new ShareModel();
        }

        public void getSportShare(final Context context) {
            mShareModel.getSportshare()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {
                        @Override
                        public void onNext(ShareResult value) {
                            ShareUtils.showShareDialog(context, value.getShareData());
                        }
                    }));
        }

    }

}
