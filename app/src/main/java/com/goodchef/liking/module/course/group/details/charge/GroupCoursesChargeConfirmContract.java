package com.goodchef.liking.module.course.group.details.charge;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

import de.greenrobot.event.EventBus;

/**
 * Created on 2017/05/16
 * desc: 收费团体课订单确认
 *
 * @author: chenlei
 * @version:1.0
 */

public interface GroupCoursesChargeConfirmContract {

    interface ChargeGroupCoursesView extends BaseNetworkLoadView {
        void updateChargeGroupCoursesView(ChargeGroupConfirmResult.ChargeGroupConfirmData chargeGroupConfirmData);

        void updatePaySubmitView(PayResultData payResultData);

        void updateBuyCoursesErrorView();

        void updateErrorNoCard(String errorMessage);

        void updateBuyCoursesNotOnGym(String errorMessage);
    }

    class ChargeGroupCoursesConfirmPresenter extends BasePresenter<ChargeGroupCoursesView> {

        private CourseModel mCourseModel = null;

        public ChargeGroupCoursesConfirmPresenter(Context context, ChargeGroupCoursesView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /**
         * 团体课支付确认
         *
         * @param gymId
         * @param scheduleId
         */
        public void getChargeGroupCoursesConfirmData(String gymId, String scheduleId) {

            mCourseModel.chargeGroupCoursesConfirm(gymId, scheduleId)
                    .subscribe(new LikingBaseObserver<ChargeGroupConfirmResult>(mContext, mView) {
                        @Override
                        public void onNext(ChargeGroupConfirmResult result) {
                            if (result.getCode() == 0) {
                                mView.updateChargeGroupCoursesView(result.getData());
                            } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_ERROR) {
                                mView.updateBuyCoursesNotOnGym(result.getMessage());
                            } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_NO_CARD) {
                                mView.updateErrorNoCard(result.getMessage());
                            } else if (result.getCode() == LiKingRequestCode.LOGIN_TOKEN_INVALID) {
                                mView.updateBuyCoursesErrorView();
                                mView.showToast(result.getMessage());
                                EventBus.getDefault().post(new LoginOutFialureMessage());
                            } else {
                                mView.showToast(result.getMessage());
                                mView.updateBuyCoursesErrorView();
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {
                            mView.handleNetworkFailure();
                        }

                    });
        }

        /***
         * 付费团体课提交订单获取支付数据
         *
         * @param scheduleId 排期id
         * @param couponCode 优惠券吗
         * @param payType    支付方式
         */
        public void chargeGroupCoursesImmediately(String gymId, String scheduleId, String couponCode, String payType) {

            mCourseModel.chargeGroupCoursesImmediately(gymId, scheduleId, couponCode, payType)
                    .subscribe(new ProgressObserver<SubmitPayResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(SubmitPayResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updatePaySubmitView(result.getPayData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {

                        }
                    });
        }
    }


}
