package com.goodchef.liking.module.writeuserinfo;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
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

    interface CompleteUserInfoView extends BaseNetworkLoadView {
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
                    .subscribe(new ProgressObserver<UserImageResult>(mContext, R.string.loading_upload) {
                        @Override
                        public void onNext(UserImageResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateUploadImage(result.getData());
                            } else {
                                mView.uploadImageError();
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {
                            mView.handleNetworkFailure();
                        }

                    });
        }

        public void uploadMyUserInfoImage(String img) {
            mCompleteUserInfoModel.uploadUserImage(img)
                    .subscribe(new LikingBaseObserver<UserImageResult>(mContext) {
                        @Override
                        public void onNext(UserImageResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateUploadImage(result.getData());
                            } else {
                                mView.uploadImageError();
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {

                        }
                    });
        }

        //更新用户信息
        public void updateUserInfo(String name, String avatar, Integer gender, String birthday, String weight, String height) {
            mCompleteUserInfoModel.updateUserInfo(name, avatar, gender, birthday, weight, height)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressObserver<LikingResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(LikingResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateUserInfo();
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {
                            mView.handleNetworkFailure();
                        }
                    });
        }

        public void getUserInfo() {
            mCompleteUserInfoModel.getUserInfo()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LikingBaseObserver<UserInfoResult>(mContext) {
                        @Override
                        public void onNext(UserInfoResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateGetUserInfoView(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {
                            mView.handleNetworkFailure();
                        }

                    });
        }

    }

}
