package com.goodchef.liking.module.train;

import android.content.Context;
import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportWeekResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;
import com.goodchef.liking.utils.ShareUtils;

import java.util.Date;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

interface SportDataContract {
    interface View extends BaseStateView {
        void updateSportStatsView(SportWeekResult value);

        void updateSportListView(SportListResult value);
    }

    class Presenter extends RxBasePresenter<View> {
        SportDataModel mModel;
        ShareModel mShareModel;

        public Presenter() {
            mModel = new SportDataModel();
            mShareModel = new ShareModel();
        }

        public SportDataEntity getDate4Index(int position) {
            return mModel.getDate4Index(position);
        }


        public void getSportStats() {
            mModel.getSportStats().subscribe(addObserverToCompositeDisposable(
                    new LikingBaseObserver<SportWeekResult>(mView) {
                        @Override
                        public void onNext(SportWeekResult value) {
                            mView.updateSportStatsView(value);

                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);

                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                        }
                    }));
        }

        public void getSportList(int pos) {
            SportDataEntity date4Index = mModel.getDate4Index(pos);
            if (date4Index == null) {return;}
            String yyyyMMdd = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getTimstamp()));
            mModel.getSportListResult("", yyyyMMdd).subscribe(addObserverToCompositeDisposable(
                    new LikingBaseObserver<SportListResult>(mView) {
                        @Override
                        public void onNext(SportListResult value) {
                            mView.updateSportListView(value);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                        }
                    }));

        }

        public void getSportShare(final Context context){
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