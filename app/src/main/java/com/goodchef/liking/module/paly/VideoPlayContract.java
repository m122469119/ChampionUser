package com.goodchef.liking.module.paly;

import android.content.Context;
import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;
import com.goodchef.liking.utils.ShareUtils;

interface VideoPlayContract {
    interface View extends BaseStateView {

    }

    class Presenter extends RxBasePresenter<View> {

        ShareModel mModel;

        public Presenter() {
            mModel = new ShareModel();
        }


        public void getVideoShare(final Context context, String url, String name){
            mModel.getSmartSpotShare(url, name).subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {
                @Override
                public void onNext(ShareResult value) {
                    ShareUtils.showShareDialog(context, value.getShareData());
                }
            }));
        }


    }
}
