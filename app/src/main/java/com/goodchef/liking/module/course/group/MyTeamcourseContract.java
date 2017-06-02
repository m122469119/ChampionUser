package com.goodchef.liking.module.course.group;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.MyGroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;
import com.goodchef.liking.module.share.ShareModel;

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

        void updateShareView(ShareData shareData);
    }

    class MyGroupCoursesPresenter extends BasePresenter<MyGroupCourseView> {

        private ShareModel mShareModel;
        private CourseModel mCourseModel = null;

        public MyGroupCoursesPresenter(Context context, MyGroupCourseView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
            mShareModel = new ShareModel();
        }

        public void getMyGroupList(int page) {
            mCourseModel.getMyGroupList(page)
                    .subscribe(new PagerLoadingObserver<MyGroupCoursesResult>(mContext, mView) {
                        @Override
                        public void onNext(MyGroupCoursesResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            mView.updateMyGroupCoursesView(result.getData());
                        }
                    });
        }

        public void sendCancelCoursesRequest(String orderId) {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult result) {
                            PopupUtils.showToast(mContext, R.string.cancel_success);
                            mView.updateLoadHomePage();
                        }
                    });
        }

        //团体课分享
        public void getGroupShareData(String scheduleId) {
            mShareModel.getGroupCoursesShare(scheduleId)
                    .subscribe(new LikingBaseObserver<ShareResult>(mContext, mView) {
                        @Override
                        public void onNext(ShareResult value) {
                            if(value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    });
        }
    }
}
