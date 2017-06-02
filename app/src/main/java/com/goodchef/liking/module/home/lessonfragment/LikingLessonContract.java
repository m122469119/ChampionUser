package com.goodchef.liking.module.home.lessonfragment;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BannerResult;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:01
 * version 1.0.0
 */

interface LikingLessonContract {

    interface View extends BaseView {
        void updateCourseView(CoursesResult.Courses courses);

        void updateBanner(BannerResult.BannerData bannerData);
    }

    class Presenter extends RxBasePresenter<View> {
        LikingLessonModel mLikingLessonModel;

        public Presenter() {
            mLikingLessonModel = new LikingLessonModel();
        }

        /**
         * 获取banner
         */
        void getBanner() {
            mLikingLessonModel.getBanner()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<BannerResult>(mView) {

                        @Override
                        public void onNext(BannerResult value) {
                            if (value == null) return;
                            LogUtils.d("aaron", "mLikingLessonModel.onNext: View = " + mView);
                            if (value.getBannerData() != null) {
                                mView.updateBanner(value.getBannerData());
                            }
                        }
                    }));
        }

        /**
         * 获取首页数据
         */
        void getHomeData(String longitude, String latitude, String cityId, String districtId, int currentPage, String gymId) {
            mLikingLessonModel.getHomeData(LikingPreference.getToken(), longitude, latitude, cityId, districtId, currentPage, gymId)
                    .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<CoursesResult>(mView) {
                        @Override
                        public void onNext(CoursesResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            if (result.getCourses() != null) {
                                mView.updateCourseView(result.getCourses());
                            }
                        }
                    }));
        }
    }
}
