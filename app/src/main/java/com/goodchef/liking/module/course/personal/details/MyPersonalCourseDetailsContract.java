package com.goodchef.liking.module.course.personal.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 2017/05/10
 * desc: 私教课
 *
 * @author: chenlei
 * @version:1.0
 */

interface MyPersonalCourseDetailsContract {

    interface View extends BaseStateView {
        void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData myPrivateCoursesDetailsData);

        void updateComplete();
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel;

        public Presenter() {
            mCourseModel = new CourseModel();
        }

        void getMyPrivateCoursesDetails(String orderId) {

            mCourseModel.getMyPrivateCoursesDetails(orderId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<MyPrivateCoursesDetailsResult>(mView) {

                        @Override
                        public void onNext(MyPrivateCoursesDetailsResult result) {
                            if (result == null) return;
                            mView.updateMyPrivateCoursesDetailsView(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        public void completeMyPrivateCourses(Context context, String orderId) {

            mCourseModel.completeMyPrivateCourses(orderId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, orderId, mView) {

                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateComplete();
                        }
                    }));
        }
    }
}
