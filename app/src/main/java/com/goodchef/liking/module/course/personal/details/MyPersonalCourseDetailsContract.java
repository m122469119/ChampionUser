package com.goodchef.liking.module.course.personal.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.course.CourseModel;

/**
 * Created on 2017/05/10
 * desc: 私教课
 *
 * @author: chenlei
 * @version:1.0
 */

public interface MyPersonalCourseDetailsContract {

    interface MyPrivateCoursesDetailsView extends BaseNetworkLoadView {
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
                    .subscribe(new LikingBaseObserver<MyPrivateCoursesDetailsResult>(mContext) {
                        @Override
                        public void onNext(MyPrivateCoursesDetailsResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateMyPrivateCoursesDetailsView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable e) {
                            mView.handleNetworkFailure();
                        }
                    });
        }

        public void completeMyPrivateCourses(String orderId) {

            mCourseModel.completeMyPrivateCourses(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, orderId) {
                        @Override
                        public void onError(ResponseThrowable responseThrowable) {

                        }

                        @Override
                        public void onNext(LikingResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateComplete();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }
                    });
        }
    }
}
