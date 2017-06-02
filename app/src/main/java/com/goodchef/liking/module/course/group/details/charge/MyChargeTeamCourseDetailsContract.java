package com.goodchef.liking.module.course.group.details.charge;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.course.CourseModel;

/**
 * Created on 2017/05/10
 * desc: 付费团体课详情
 *
 * @author: chenlei
 * @version:1.0
 */

interface MyChargeTeamCourseDetailsContract {

    interface View extends BaseStateView {
        void updateChargeGroupCoursesDetailsView(MyChargeGroupCoursesDetailsResult.MyChargeGroupCoursesDetails groupCoursesDetails);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel = null;

        public Presenter() {
            mCourseModel = new CourseModel();
        }

        /**
         * 获取我的付费团体课详情
         */
        void getChargeGroupCoursesDetails(String orderId) {
            mCourseModel.getChargeGroupCoursesDetails(orderId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<MyChargeGroupCoursesDetailsResult>(mView) {
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
                    }));
        }

    }
}
