package com.goodchef.liking.module.course.group.details.charge;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.course.CourseModel;

/**
 * Created on 2017/05/10
 * desc: 付费团体课详情
 *
 * @author: chenlei
 * @version:1.0
 */

public interface MyChargeTeamCourseDetailsContract {

    interface ChargeGroupCoursesDetailsView extends BaseStateView {
        void updateChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails);
    }

    class MyChargeGroupCoursesDetailsPresenter extends BasePresenter<ChargeGroupCoursesDetailsView> {

        private CourseModel mCourseModel = null;

        public MyChargeGroupCoursesDetailsPresenter(Context context, ChargeGroupCoursesDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /**
         * 获取我的付费团体课详情
         */
        public void getChargeGroupCoursesDetails(String orderId) {
            mCourseModel.getChargeGroupCoursesDetails(orderId)
                    .subscribe(new LikingBaseObserver<MyChargeGroupCoursesDetailsResult>(mContext, mView) {
                        @Override
                        public void onNext(MyChargeGroupCoursesDetailsResult result) {
                            if(null == result) return;
                            mView.updateChargeGroupCoursesDetailsView(result.getData());
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

    }
}
