package com.goodchef.liking.module.course.group;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.module.data.remote.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 2017/05/09
 * desc: 我的 - 课程列表
 *
 * @author: chenlei
 * @version:1.0
 */

public interface MyTeamcourseContract {

    interface MyGroupCourseView extends BaseView {
        void updateMyGroupCoursesView(MyGroupCoursesResult.MyGroupCoursesData myGroupCoursesData);

        void updateLoadHomePage();
    }

    class MyGroupCoursesPresenter extends BasePresenter<MyGroupCourseView> {

        private CourseModel mCourseModel = null;

        public MyGroupCoursesPresenter(Context context, MyGroupCourseView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
        }

        public void getMyGroupList(int page) {
            mCourseModel.getMyGroupList(page)
                    .subscribe(new PagerLoadingObserver<MyGroupCoursesResult>(mContext, mView) {
                        @Override
                        public void onNext(MyGroupCoursesResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateMyGroupCoursesView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                    });
        }

        public void sendCancelCoursesRequest(String orderId) {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {

                        @Override
                        public void onNext(LikingResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                PopupUtils.showToast(mContext, R.string.cancel_success);
                                mView.updateLoadHomePage();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }
                    });
        }
    }
}
