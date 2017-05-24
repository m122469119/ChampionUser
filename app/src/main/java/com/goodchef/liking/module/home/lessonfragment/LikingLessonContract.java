package com.goodchef.liking.module.home.lessonfragment;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:01
 * version 1.0.0
 */

public class LikingLessonContract {

    interface LikingLessonView extends BaseView {
        void updateCourseView(CoursesResult.Courses courses);

        void updateBanner(BannerResult.BannerData bannerData);
    }

    public static class LikingLessonPresenter extends BasePresenter<LikingLessonView> {
        LikingLessonModel mLikingLessonModel;

        public LikingLessonPresenter(Context context, LikingLessonView mainView) {
            super(context, mainView);
            mLikingLessonModel = new LikingLessonModel();
        }

        /**
         * 获取banner
         */
        public void getBanner() {
            mLikingLessonModel.getBanner()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LikingBaseObserver<BannerResult>(mContext, mView) {
                        @Override
                        public void onNext(BannerResult value) {
                            if (value == null) return;
                            if (value.getBannerData() != null) {
                                mView.updateBanner(value.getBannerData());
                            }
                        }
                    });
        }

        /**
         * 获取首页数据
         */
        public void getHomeData(String longitude, String latitude, String cityId, String districtId, int currentPage, String gymId) {
            mLikingLessonModel.getHomeData(LikingPreference.getToken(), longitude, latitude, cityId, districtId, currentPage, gymId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new PagerLoadingObserver<CoursesResult>(mContext, mView) {
                        @Override
                        public void onNext(CoursesResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            if (result.getCourses() != null) {
                                mView.updateCourseView(result.getCourses());
                            }
                        }
                    });
        }
    }
}
