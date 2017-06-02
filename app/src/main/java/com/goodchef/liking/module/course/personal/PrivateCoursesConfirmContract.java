package com.goodchef.liking.module.course.personal;

import android.content.Context;
import android.content.DialogInterface;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.OrderCalculateResult;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.utils.LikingCallUtil;

import de.greenrobot.event.EventBus;

/**
 * Created on 2017/05/16
 * desc:私教课确认购买页
 *
 * @author: chenlei
 * @version:1.0
 */

interface PrivateCoursesConfirmContract {

    interface View extends BaseStateView {
        void updatePrivateCoursesConfirm(PrivateCoursesConfirmResult.PrivateCoursesConfirmData coursesConfirmData);

        void updateSubmitOrderCourses(PayResultData payData);

        void updateOrderCalculate(boolean isSuccess, OrderCalculateResult.OrderCalculateData orderCalculateData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel = null;

        public Presenter() {
            mCourseModel = new CourseModel();
        }

        /**
         * 私教课确认预约订单
         *
         * @param context
         * @param trainerId 教练ID
         * @param gymId
         */
        void orderPrivateCoursesConfirm(final Context context, String trainerId, String gymId) {

            mCourseModel.orderPrivateCoursesConfirm(gymId, trainerId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<PrivateCoursesConfirmResult>(mView) {

                        @Override
                        public void onNext(PrivateCoursesConfirmResult result) {
                            if (result == null) return;
                            mView.updatePrivateCoursesConfirm(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.PRIVATE_COURSES_CONFIRM:
                                    HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
                                    builder.setMessage(apiException.getErrorMessage());
                                    builder.setPositiveButton(R.string.diaog_got_it, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EventBus.getDefault().post(new CoursesErrorMessage());
                                        }
                                    });
                                    builder.create().setCancelable(false);
                                    builder.create().show();
                                    break;
                                default:
                                    super.apiError(apiException);
                            }
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        /**
         * 提交预约私教课
         *
         * @param context
         * @param couponCode 优惠券
         * @param payType    支付方式
         * @param courseId   课程id
         */
        void submitPrivateCourses(final Context context, String couponCode, String payType, int selectTimes, String gymId, String courseId) {

            mCourseModel.submitPrivateCourses(courseId, couponCode, payType, selectTimes, gymId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<SubmitPayResult>(context, R.string.loading_data, mView) {

                        @Override
                        public void onNext(SubmitPayResult result) {
                            if (result == null) return;
                            mView.updateSubmitOrderCourses(result.getPayData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_COURSES_ERROR:
                                    LikingCallUtil.showBuyCoursesErrorDialog(context, apiException.getErrorMessage());
                                    break;
                                default:
                                    super.apiError(apiException);
                            }
                        }
                    }));
        }

        /***
         * 计算私教课金额
         *
         * @param courseId    课程id
         * @param selectTimes 选择的课次
         */
        public void orderCalculate(String courseId, String selectTimes) {

            mCourseModel.orderCalculate(courseId, selectTimes)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<OrderCalculateResult>(mView) {

                        @Override
                        public void onNext(OrderCalculateResult result) {
                            if (result == null) return;
                            mView.updateOrderCalculate(true, result.getData());
                        }

                    }));
        }
    }
}
