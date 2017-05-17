package com.goodchef.liking.module.course.group.details.charge;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
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

    interface ChargeGroupCoursesDetailsView extends BaseNetworkLoadView {
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
                    .subscribe(new LikingBaseObserver<MyChargeGroupCoursesDetailsResult>(mContext) {
                        @Override
                        public void onNext(MyChargeGroupCoursesDetailsResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateChargeGroupCoursesDetailsView(result.getData());
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

    }
}
