package com.goodchef.liking.module.train;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.common.utils.DateUtils;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportStatsResult;
import com.goodchef.liking.data.remote.retrofit.result.SportUserStatResult;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;
import com.goodchef.liking.utils.ShareUtils;

import java.util.Date;
import java.util.List;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

interface SportDataContract {

    interface View extends BaseStateView {
        void updateSportStatsView(SportStatsResult value);

        void updateSportListView(SportListResult value);

        void updateSportUserStatView(SportUserStatResult value);
    }

    class Presenter extends RxBasePresenter<View> {

        public final static int TYPE_TIME_DAY = SportDataModel.TYPE_TIME_DAY;
        public final static int TYPE_TIME_WEEK = SportDataModel.TYPE_TIME_WEEK;
        public final static int TYPE_TIME_MONTH = SportDataModel.TYPE_TIME_MONTH;

        SportDataModel mModel;
        ShareModel mShareModel;

        public Presenter(int type) {
            mModel = new SportDataModel(type);
            mShareModel = new ShareModel();
        }

        public SportDataEntity getDate4Index(int position) {
            return mModel.getDate4Index(position);
        }

        public List<SportDataEntity> getSportDatas() {
            return mModel.getSportDatas();
        }

        public void getSportStats() {
            mModel.getSportStats().subscribe(addObserverToCompositeDisposable(
                    new LikingBaseObserver<SportStatsResult>(mView) {
                        @Override
                        public void onNext(SportStatsResult value) {
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
            if (date4Index == null) {
                return;
            }
            String yyyyMMdd = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getStartTime()));
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

        public void getSportUserStatsResult(int pos) {
            SportDataEntity date4Index = mModel.getDate4Index(pos);
            if (date4Index == null) {
                return;
            }
            try {
                String yyyyMMddStart = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getStartTime()));
                String yyyyMMddEnd = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getEndTime()));
                mModel.getSportUserStatsResult(yyyyMMddStart, yyyyMMddEnd).subscribe(addObserverToCompositeDisposable(
                        new LikingBaseObserver<SportUserStatResult>(mView) {
                            @Override
                            public void onNext(SportUserStatResult value) {
                                mView.updateSportUserStatView(value);
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
            } catch (Exception e) {
            }

        }

        public void getSportShare(final Context context, int pos) {
            SportDataEntity date4Index = mModel.getDate4Index(pos);
            if (date4Index == null) {
                return;
            }
            try {
                String yyyyMMddStart = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getStartTime()));
                String yyyyMMddEnd = DateUtils.formatDate("yyyyMMdd", new Date(date4Index.getEndTime()));
                mShareModel.getSportshare(yyyyMMddStart, yyyyMMddEnd)
                        .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {
                            @Override
                            public void onNext(ShareResult value) {
                                ShareUtils.showShareDialog(context, value.getShareData());
                            }
                        }));
            } catch (Exception e) {
            }
        }

    }

}
