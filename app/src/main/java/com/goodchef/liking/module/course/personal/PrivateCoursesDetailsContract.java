package com.goodchef.liking.module.course.personal;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.share.ShareModel;

/**
 * Created on 2017/05/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

interface PrivateCoursesDetailsContract {

    interface View extends BaseStateView {
        void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData);
        void updateShareView(ShareData shareData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel;
        private ShareModel mShareModel;

        public Presenter() {
            mCourseModel = new CourseModel();
            mShareModel = new ShareModel();
        }

        /***
         * 私教课详情
         *
         * @param trainerId 私教id
         */
        void getPrivateCoursesDetails(String trainerId) {
            mCourseModel.getPrivateCoursesDetails(trainerId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<PrivateCoursesResult>(mView) {

                        @Override
                        public void onNext(PrivateCoursesResult result) {
                            if (result == null) return;
                            mView.updatePrivateCoursesDetailsView(result.getPrivateCoursesData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }


        //私教课分享
        public void getPrivateShareData(String trainId) {
            mShareModel.getPrivateCoursesShare(trainId)
                    .subscribe(new LikingBaseObserver<ShareResult>(mView) {
                        @Override
                        public void onNext(ShareResult value) {
                            if(value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    });
        }


    }
}
