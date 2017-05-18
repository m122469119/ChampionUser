package com.goodchef.liking.module.course.personal;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;

/**
 * Created on 2017/05/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface PrivateCoursesDetailsContract {

    interface PrivateCoursesDetailsView extends BaseNetworkLoadView {
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
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updatePrivateCoursesDetailsView(result.getPrivateCoursesData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable responseThrowable) {
                            mView.handleNetworkFailure();
                        }

                    });
        }
    }
}
