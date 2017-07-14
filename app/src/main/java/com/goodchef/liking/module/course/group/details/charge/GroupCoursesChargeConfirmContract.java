package com.goodchef.liking.module.course.group.details.charge;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.ChargeGroupConfirmResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.utils.LikingCallUtil;

/**
 * Created on 2017/05/16
 * desc: 收费团体课订单确认
 *
 * @author: chenlei
 * @version:1.0
 */

interface GroupCoursesChargeConfirmContract {

    interface View extends BaseStateView {
        void updateChargeGroupCoursesView(ChargeGroupConfirmResult.ChargeGroupConfirmData chargeGroupConfirmData);

        void updatePaySubmitView(PayResultData payResultData);

        void updateBuyCoursesErrorView();

        void updateErrorNoCard(String errorMessage);

        void updateBuyCoursesNotOnGym(String errorMessage);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel = null;

        public Presenter() {
            mCourseModel = new CourseModel();
        }

        /**
         * 团体课支付确认
         *
         * @param gymId
         * @param scheduleId
         */
        void getChargeGroupCoursesConfirmData(String gymId, String scheduleId) {

            mCourseModel.chargeGroupCoursesConfirm(gymId, scheduleId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ChargeGroupConfirmResult>(mView) {

                        @Override
                        public void onNext(ChargeGroupConfirmResult result) {
                            if (null == result) return;
                            mView.updateChargeGroupCoursesView(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_COURSES_ERROR:
                                    mView.updateBuyCoursesNotOnGym(apiException.getErrorMessage());
                                    break;
                                case LiKingRequestCode.BUY_COURSES_NO_CARD:
                                    mView.updateErrorNoCard(apiException.getErrorMessage());
                                    break;
                                default:
                                    super.apiError(apiException);
                                    mView.updateBuyCoursesErrorView();
                            }

                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        /***
         * 付费团体课提交订单获取支付数据
         *
         * @param scheduleId 排期id
         * @param couponCode 优惠券吗
         * @param payType    支付方式
         */
        void chargeGroupCoursesImmediately(final Context context, String gymId, String scheduleId, String couponCode, String payType) {

            mCourseModel.chargeGroupCoursesImmediately(gymId, scheduleId, couponCode, payType)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<SubmitPayResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(SubmitPayResult result) {
                            if (result == null) return;
                            mView.updatePaySubmitView(result.getPayData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_COURSES_ERROR:
                                    LikingCallUtil.showBuyCoursesErrorDialog(context, apiException.getErrorMessage());
                                default:
                                    super.apiError(apiException);
                            }
                        }
                    }));
        }
    }


}
