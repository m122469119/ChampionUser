package com.goodchef.liking.module.course.group;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
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

interface MyTeamcourseContract {

    interface View extends BaseView {
        void updateMyGroupCoursesView(MyGroupCoursesResult.MyGroupCoursesData myGroupCoursesData);

        void updateLoadHomePage();

        void updateShareView(ShareData shareData);
    }

    class Presenter extends RxBasePresenter<View> {

        private CourseModel mCourseModel;
        private ShareModel mShareModel;

        public Presenter() {
            mCourseModel = new CourseModel();
            mShareModel = new ShareModel();
        }

        void getMyGroupList(int page) {
            mCourseModel.getMyGroupList(page)
                    .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<MyGroupCoursesResult>(mView) {
                        @Override
                        public void onNext(MyGroupCoursesResult result) {
                            super.onNext(result);
                            if (result == null) return;
                            mView.updateMyGroupCoursesView(result.getData());
                        }
                    }));
        }

        void sendCancelCoursesRequest(final Context context, String orderId) {
            mCourseModel.sendCancelCoursesRequest(orderId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult result) {
                            PopupUtils.showToast(context, R.string.cancel_success);
                            mView.updateLoadHomePage();
                        }
                    }));
        }

        //团体课分享
        void getGroupShareData(String scheduleId) {
            mShareModel.getGroupCoursesShare(scheduleId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {

                        @Override
                        public void onNext(ShareResult value) {
                            if (value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    }));
        }
    }
}
