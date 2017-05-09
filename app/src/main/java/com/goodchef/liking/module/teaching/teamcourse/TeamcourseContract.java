package com.goodchef.liking.module.teaching.teamcourse;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.base.rxobserver.ProgressObserver;
import com.goodchef.liking.module.teaching.CourseModel;

/**
 * Created on 2017/05/09
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface TeamcourseContract {

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
                    .subscribe(new PagerLoadingObserver<MyGroupCoursesResult>(mView) {
                        @Override
                        public void onNext(MyGroupCoursesResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateMyGroupCoursesView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }

        public void sendCancelCoursesRequest(String orderId) {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(LikingResult result) {
                            super.onNext(result);
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
