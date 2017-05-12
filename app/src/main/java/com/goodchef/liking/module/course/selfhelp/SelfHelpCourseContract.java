package com.goodchef.liking.module.course.selfhelp;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.base.rxobserver.ProgressObserver;
import com.goodchef.liking.module.course.CourseModel;

/**
 * Created on 2017/05/11
 * desc: 自助团体课
 *
 * @author: chenlei
 * @version:1.0
 */

public interface SelfHelpCourseContract {

    interface SelfHelpGroupCoursesView extends BaseNetworkLoadView {
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
                    .subscribe(new LikingBaseObserver<SelfHelpGroupCoursesResult>() {
                                   @Override
                                   public void onNext(SelfHelpGroupCoursesResult result) {
                                       super.onNext(result);
                                       if (LiKingVerifyUtils.isValid(mContext, result)) {
                                           mView.updateSelfHelpGroupCoursesView(result.getData());
                                       } else {
                                           mView.showToast(result.getMessage());
                                       }
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
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading) {
                                   @Override
                                   public void onNext(LikingResult result) {
                                       super.onNext(result);
                                       if (LiKingVerifyUtils.isValid(mContext, result)) {
                                           mView.updateOrderView();
                                       } else if (result.getCode() == 22013) {
                                           mView.updateNoCardView(result.getMessage());
                                       } else {
                                           mView.updateSelectCourserView();//刷新选中的View(当前时刻-房间被其他人预约,后台返回码不唯一,刷新接口后刷新选中view)
                                           mView.showToast(result.getMessage());
                                       }
                                   }
                               }
                    );
        }
    }

}
