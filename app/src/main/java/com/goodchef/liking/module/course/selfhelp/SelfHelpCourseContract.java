package com.goodchef.liking.module.course.selfhelp;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.utils.LikingCallUtil;

/**
 * Created on 2017/05/11
 * desc: 自助团体课
 *
 * @author: chenlei
 * @version:1.0
 */

public interface SelfHelpCourseContract {

    interface SelfHelpGroupCoursesView extends BaseStateView {
        void updateSelfHelpGroupCoursesView(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData selfHelpGroupCoursesData);

        void updateOrderView();

        void updateNoCardView(String message);

        void updateSelectCourserView();
    }

    class SelfHelpGroupCoursesPresenter extends BasePresenter<SelfHelpGroupCoursesView> {

        private CourseModel mCourseModel = null;

        public SelfHelpGroupCoursesPresenter(Context context, SelfHelpGroupCoursesView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        /***
         * 获取自助团体课时间表
         *
         * @param gymId 场馆ID
         */
        public void getSelfHelpCourses(String gymId) {
            mCourseModel.getSelfHelpScheduleInfo(gymId)
                    .subscribe(new LikingBaseObserver<SelfHelpGroupCoursesResult>(mContext, mView) {

                                   @Override
                                   public void onNext(SelfHelpGroupCoursesResult result) {
                                       if(result == null) return;
                                       mView.updateSelfHelpGroupCoursesView(result.getData());
                                   }
                               }
                    );
        }

        /***
         * 自助团体课 - 预约
         *
         * @param gymId       场馆id
         * @param roomId      操房id
         * @param coursesId   课程id
         * @param coursesDate 日期
         * @param startTime   开始时间
         * @param endTime     结束时间
         * @param price       价格
         * @param peopleNum   人数
         */
        public void sendOrderRequest(String gymId, String roomId, String coursesId, String coursesDate, String startTime, String endTime, String price, String peopleNum) {

            mCourseModel.joinSelfHelpCourses(gymId, roomId, coursesId, coursesDate, startTime, endTime, price, peopleNum)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading, mView) {

                                   @Override
                                   public void onNext(LikingResult result) {
                                       mView.updateOrderView();
                                   }

                                   @Override
                                   public void apiError(ApiException apiException) {
                                       switch (apiException.getErrorCode()) {
                                           case LiKingRequestCode.BUY_COURSES_NO_CARD:
                                               mView.updateNoCardView(apiException.getMessage());
                                               break;
                                           case LiKingRequestCode.BUY_COURSES_ERROR:
                                               LikingCallUtil.showBuyCoursesErrorDialog(mContext, apiException.getErrorMessage());
                                               break;
                                           default:
                                               mView.updateSelectCourserView();//刷新选中的View(当前时刻-房间被其他人预约,后台返回码不唯一,刷新接口后刷新选中view)
                                               super.apiError(apiException);
                                       }
                                   }

                                   @Override
                                   public void networkError(Throwable throwable) {
                                       super.networkError(throwable);
                                   }
                               }
                    );
        }
    }

}
