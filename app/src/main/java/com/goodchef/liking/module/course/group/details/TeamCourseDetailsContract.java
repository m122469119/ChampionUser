package com.goodchef.liking.module.course.group.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.GroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.share.ShareModel;
import com.goodchef.liking.utils.LikingCallUtil;

/**
 * Created on 2017/05/10
 * desc: 团体课详情
 *
 * @author: chenlei
 * @version:1.0
 */

interface TeamCourseDetailsContract {

    interface View extends BaseStateView {
        void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData);

        void updateOrderGroupCourses();

        void updateErrorNoCard(String errorMessage);

        void updateCancelOrderView();

        void updateShareView(ShareData shareData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel;
        private ShareModel mShareModel;

        public String scheduleId;//排期id
        int mCoursesState = -1;//课程状态
        public String orderId;//订单id

        String quota;//预约人数
        int isFree;//是否免费
        int scheduleType = -1;
        public String price;//价格

        public Presenter() {
            mCourseModel = new CourseModel();
            mShareModel = new ShareModel();
        }

        /**
         * 团体课详情
         *
         * @param scheduleId
         */
        void getGroupCoursesDetails(String scheduleId) {
            mCourseModel.getGroupCoursesDetails(scheduleId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<GroupCoursesResult>(mView) {

                        @Override
                        public void onNext(GroupCoursesResult result) {
                            if (null == result) return;
                            GroupCoursesResult.GroupLessonData data = result.getGroupLessonData();
                            if (data != null) {
                                setQuota(data.getQuota());
                                setScheduleType(data.getScheduleType());
                                setIsFree(data.getIsFree());
                                setPrice(data.getPrice());
                            }
                            mView.updateGroupLessonDetailsView(data);
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

        /**
         * 团体课预约
         *
         * @param gymId
         */
        void orderGroupCourses(final Context context, String gymId) {

            mCourseModel.orderGroupCourses(gymId, scheduleId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading_data, mView) {

                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateOrderGroupCourses();
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.BUY_COURSES_NO_CARD:
                                    mView.updateErrorNoCard(apiException.getMessage());
                                    break;
                                case LiKingRequestCode.BUY_COURSES_ERROR:
                                    LikingCallUtil.showBuyCoursesErrorDialog(context, apiException.getErrorMessage());
                                    break;
                                default:
                                    super.apiError(apiException);
                            }
                        }

                    }));
        }

        //团体课分享
        public void getGroupShareData(String scheduleId) {
            mShareModel.getGroupCoursesShare(scheduleId)
                    .subscribe(new LikingBaseObserver<ShareResult>(mView) {
                        @Override
                        public void onNext(ShareResult value) {
                            if(value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    });
        }


        /**
         * 取消团体课
         */
        void sendCancelCoursesRequest(Context context) {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateCancelOrderView();
                        }
                    }));
        }

        //团体课分享
        public void getGroupShareData() {
            mShareModel.getGroupCoursesShare(scheduleId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {

                        @Override
                        public void onNext(ShareResult value) {
                            if (value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    }));
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        void setCoursesState(int coursesState) {
            mCoursesState = coursesState;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        void setIsFree(int isFree) {
            this.isFree = isFree;
        }

        void setScheduleType(int scheduleType) {
            this.scheduleType = scheduleType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        void setQuota(String quota) {
            this.quota = quota;
        }
    }
}
