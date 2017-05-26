package com.goodchef.liking.module.course.personal.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
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

public interface MyPersonalCourseDetailsContract {

    interface MyPrivateCoursesDetailsView extends BaseStateView {
        void updateMyPrivateCoursesDetailsView(MyPrivateCoursesDetailsResult.MyPrivateCoursesDetailsData myPrivateCoursesDetailsData);

        void updateComplete();
    }

    class MyPrivateCoursesDetailsPresenter extends BasePresenter<MyPrivateCoursesDetailsView> {

        private CourseModel mCourseModel;

        public MyPrivateCoursesDetailsPresenter(Context context, MyPrivateCoursesDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        public void getMyPrivateCoursesDetails(String orderId) {

            mCourseModel.getMyPrivateCoursesDetails(orderId)
                    .subscribe(new LikingBaseObserver<MyPrivateCoursesDetailsResult>(mContext, mView) {
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
                    });
        }

        public void completeMyPrivateCourses(String orderId) {

            mCourseModel.completeMyPrivateCourses(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, orderId, mView) {

                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateComplete();
                        }
                    });
        }
    }
}
