package com.goodchef.liking.module.writeuserinfo;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.UserImageResult;
import com.goodchef.liking.data.remote.retrofit.result.UserInfoResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:37
 * version 1.0.0
 */

public interface CompleteUserInfoContract {

    interface View extends BaseStateView {
        void updateUploadImage(UserImageResult.UserImageData userImageData);

        void uploadImageError();

        void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData);

        void updateUserInfo();
    }

    class Presenter extends RxBasePresenter<View> {
        CompleteUserInfoModel mCompleteUserInfoModel;

        public Presenter() {
            mCompleteUserInfoModel = new CompleteUserInfoModel();
        }

        //上传头像
        public void uploadImage(Context context, String img) {
            mCompleteUserInfoModel.uploadUserImage(img)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<UserImageResult>(context, R.string.loading_upload, mView) {
                        @Override
                        public void onNext(UserImageResult result) {
                            if (result == null) return;
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
                    }));
        }

        //更新用户信息
        public void updateUserInfo(Context context, String avatar, Integer gender, String birthday, String weight, String height, String name) {
            mCompleteUserInfoModel.updateUserInfo(name, avatar, gender, birthday, weight, height)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<LikingResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(LikingResult result) {
                            mView.updateUserInfo();
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        public void getUserInfo() {
            mCompleteUserInfoModel.getUserInfo()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<UserInfoResult>(mView) {

                        @Override
                        public void onNext(UserInfoResult result) {
                            if (result == null) return;
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

                    }));
        }

    }

}
