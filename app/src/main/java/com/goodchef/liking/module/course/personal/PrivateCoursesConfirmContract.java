package com.goodchef.liking.module.course.personal;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.utils.LikingCallUtil;

/**
 * Created on 2017/05/16
 * desc:私教课确认购买页
 *
 * @author: chenlei
 * @version:1.0
 */

public interface PrivateCoursesConfirmContract {

    interface PrivateCoursesConfirmView extends BaseStateView {
        void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData);

        void updateSubmitOrderCourses(PayResultData payData);

        void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData);
    }

    class PrivateCoursesConfirmPresenter extends BasePresenter<PrivateCoursesConfirmView> {

        private CourseModel mCourseModel = null;

        public PrivateCoursesConfirmPresenter(Context context, PrivateCoursesConfirmView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /**
         * 私教课确认预约订单
         *
         * @param gymId
         * @param trainerId 教练ID
         */
        public void orderPrivateCoursesConfirm(String gymId, String trainerId) {

            mCourseModel.orderPrivateCoursesConfirm(gymId, trainerId)
                    .subscribe(new LikingBaseObserver<PrivateCoursesConfirmResult>(mContext, mView) {
                        @Override
                        public void onNext(PrivateCoursesConfirmResult result) {
                            if (result == null) return;
                            mView.updatePrivateCoursesConfirm(result.getData());
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

        /**
         * 提交预约私教课
         *
         * @param courseId   课程id
         * @param couponCode 优惠券
         * @param payType    支付方式
         */
        public void submitPrivateCourses(String courseId, String couponCode, String payType, int selectTimes, String gymId) {

            mCourseModel.submitPrivateCourses(courseId, couponCode, payType, selectTimes, gymId)
                    .subscribe(new ProgressObserver<SubmitPayResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(SubmitPayResult result) {
                            if (result == null) return;
                            mView.updateSubmitOrderCourses(result.getPayData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_COURSES_ERROR:
                                    LikingCallUtil.showBuyCoursesErrorDialog(mContext, apiException.getErrorMessage());
                                    break;
                                default:
                                    super.apiError(apiException);
                            }
                        }
                    });
        }

        /***
         * 计算私教课金额
         *
         * @param courseId    课程id
         * @param selectTimes 选择的课次
         */
        public void orderCalculate(String courseId, String selectTimes) {

            mCourseModel.orderCalculate(courseId, selectTimes)
                    .subscribe(new LikingBaseObserver<OrderCalculateResult>(mContext, mView) {
                        @Override
                        public void onNext(OrderCalculateResult result) {
                            if (result == null) return;
                            mView.updateOrderCalculate(true, result.getData());
                        }

                    });
        }
    }
}
