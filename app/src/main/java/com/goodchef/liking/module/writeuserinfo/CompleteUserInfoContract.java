package com.goodchef.liking.module.writeuserinfo;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:37
 * version 1.0.0
 */

public interface CompleteUserInfoContract {

    interface CompleteUserInfoView extends BaseStateView {
        void updateUploadImage(UserImageResult.UserImageData userImageData);

        void uploadImageError();

        void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData);

        void updateUserInfo();
    }

    class CompleteUserInfoPresenter extends BasePresenter<CompleteUserInfoView> {
        CompleteUserInfoModel mCompleteUserInfoModel;

        public CompleteUserInfoPresenter(Context context, CompleteUserInfoView mainView) {
            super(context, mainView);
            mCompleteUserInfoModel = new CompleteUserInfoModel();
        }

        //上传头像
        public void uploadImage(String img) {
            mCompleteUserInfoModel.uploadUserImage(img)
                    .subscribe(new ProgressObserver<UserImageResult>(mContext, R.string.loading_upload, mView) {
                        @Override
                        public void onNext(UserImageResult result) {
                            if(result == null) return;
                            mView.updateUploadImage(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.uploadImageError();
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    });
        }

        public void uploadMyUserInfoImage(String img) {
            mCompleteUserInfoModel.uploadUserImage(img)
                    .subscribe(new LikingBaseObserver<UserImageResult>(mContext, mView) {
                        @Override
                        public void onNext(UserImageResult result) {
                            if(result == null) return;
                            mView.updateUploadImage(result.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.uploadImageError();
                        }

                    });
        }

        //更新用户信息
        public void updateUserInfo(String name, String avatar, Integer gender, String birthday, String weight, String height) {
            mCompleteUserInfoModel.updateUserInfo(name, avatar, gender, birthday, weight, height)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateUserInfo();
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    });
        }

        public void getUserInfo() {
            mCompleteUserInfoModel.getUserInfo()
                    .subscribe(new LikingBaseObserver<UserInfoResult>(mContext, mView) {
                        @Override
                        public void onNext(UserInfoResult result) {
                            if(result == null) return;
                            mView.updateGetUserInfoView(result.getData());
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

    }

}
