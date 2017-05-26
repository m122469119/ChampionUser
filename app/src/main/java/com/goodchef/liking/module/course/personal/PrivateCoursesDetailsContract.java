package com.goodchef.liking.module.course.personal;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesResult;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * Created on 2017/05/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface PrivateCoursesDetailsContract {

    interface PrivateCoursesDetailsView extends BaseStateView {
        void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData);
    }

    class PrivateCoursesDetailsPresenter extends BasePresenter<PrivateCoursesDetailsView> {

        private CourseModel mCourseModel = null;

        public PrivateCoursesDetailsPresenter(Context context, PrivateCoursesDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /***
         * 私教课详情
         *
         * @param trainerId 私教id
         */
        public void getPrivateCoursesDetails(String trainerId) {
            mCourseModel.getPrivateCoursesDetails(trainerId)
                    .subscribe(new LikingBaseObserver<PrivateCoursesResult>(mContext, mView) {
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
                    });
        }
    }
}
