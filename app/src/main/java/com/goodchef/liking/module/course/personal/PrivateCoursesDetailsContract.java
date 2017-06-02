package com.goodchef.liking.module.course.personal;

import android.content.Context;

import android.view.View;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.share.Share;
import com.aaron.share.weixin.WeixinShare;
import com.aaron.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.dialog.ShareCustomDialog;
import com.goodchef.liking.module.course.CourseModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;

/**
 * Created on 2017/05/16
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface PrivateCoursesDetailsContract {

    interface PrivateCoursesDetailsView extends BaseStateView {
        void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData);
        void updateShareView(ShareData shareData);
    }

    class PrivateCoursesDetailsPresenter extends BasePresenter<PrivateCoursesDetailsView> {

        private CourseModel mCourseModel = null;
        private ShareModel mShareModel = null;

        public PrivateCoursesDetailsPresenter(Context context, PrivateCoursesDetailsView mainView) {
            super(context, mainView);
            mCourseModel = new CourseModel();
            mShareModel = new ShareModel();
        }

        /***
         * 私教课详情
         *
         * @param trainerId 私教id
         */
        public void getPrivateCoursesDetails(String trainerId) {
            mCourseModel.getPrivateCoursesDetails(trainerId)
                    .subscribe(new LikingBaseObserver<PrivateCoursesResult>(mContext, mView) {
                        @Override
                        public void onNext(PrivateCoursesResult result) {
                            if (result == null) return;
                            mView.updatePrivateCoursesDetailsView(result.getPrivateCoursesData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    });
        }


        //私教课分享
        public void getPrivateShareData(String trainId) {
            mShareModel.getPrivateCoursesShare(trainId)
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
