package com.goodchef.liking.module.teaching.teamcourse.details;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.base.rxobserver.ProgressObserver;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.teaching.CourseModel;

/**
 * Created on 2017/05/10
 * desc: 团体课详情
 *
 * @author: chenlei
 * @version:1.0
 */

public interface TeamCourseDetailsContract {

    interface GroupCourserDetailsView extends BaseNetworkLoadView {
        void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData);

        void updateOrderGroupCourses();

        void updateErrorNoCard(String errorMessage);

        void updateCancelOrderView();
    }

    class GroupCoursesDetailsPresenter extends BasePresenter<GroupCourserDetailsView> {

        private CourseModel mCourseModel;

        public String scheduleId;//排期id
        public int mCoursesState = -1;//课程状态
        public String orderId;//订单id

        public String quota;//预约人数
        public int isFree;//是否免费
        public int scheduleType = -1;
        public String price;//价格

        public GroupCoursesDetailsPresenter(Context context, GroupCourserDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /**
         * 团体课详情
         *
         * @param scheduleId
         */
        public void getGroupCoursesDetails(String scheduleId) {
            mCourseModel.getGroupCoursesDetails(scheduleId)
                    .subscribe(new LikingBaseObserver<GroupCoursesResult>() {
                        @Override
                        public void onNext(GroupCoursesResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                GroupCoursesResult.GroupLessonData data = result.getGroupLessonData();
                                if(data != null) {
                                    setQuota(data.getQuota());
                                    setScheduleType(data.getScheduleType());
                                    setIsFree(data.getIsFree());
                                    setPrice(data.getPrice());
                                }
                                mView.updateGroupLessonDetailsView(data);
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.handleNetworkFailure();
                        }
                    });
        }

        /**
         * 团体课预约
         *
         * @param gymId
         */
        public void orderGroupCourses(String gymId) {

            mCourseModel.orderGroupCourses(gymId, scheduleId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(LikingResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateOrderGroupCourses();
                            } else if (result.getCode() == LiKingRequestCode.BUY_COURSES_NO_CARD) {
                                mView.updateErrorNoCard(result.getMessage());
                            } else if (result.getCode() != LiKingRequestCode.BUY_COURSES_ERROR) {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }

        /**
         * 取消团体课
         */
        public void sendCancelCoursesRequest() {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(LikingResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateCancelOrderView();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }
                    });
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public void setCoursesState(int coursesState) {
            mCoursesState = coursesState;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setIsFree(int isFree) {
            this.isFree = isFree;
        }

        public void setScheduleType(int scheduleType) {
            this.scheduleType = scheduleType;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setQuota(String quota) {
            this.quota = quota;
        }
    }
}
