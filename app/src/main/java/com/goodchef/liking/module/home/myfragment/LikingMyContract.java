package com.goodchef.liking.module.home.myfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.activity.BodyTestDataActivity;
import com.goodchef.liking.activity.EveryDaySportActivity;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.http.result.MyUserOtherInfoResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.module.brace.BingBraceletActivity;
import com.goodchef.liking.module.brace.MyBraceletActivity;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.module.train.UserExerciseModel;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:31
 * version 1.0.0
 */

public class LikingMyContract {

    interface LikingMyView extends BaseStateView {
        void updateInfoData(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData);

        void updateExerciseData(UserExerciseResult.ExerciseData exerciseData);
    }

    public static class LikingMyPresenter extends BasePresenter<LikingMyView> {
        LikingMyModel mLikingMyModel;
        UserExerciseModel mUserExerciseModel;
        private BleUtils mBleUtils;//蓝牙Util

        public LikingMyPresenter(Context context, LikingMyView mainView) {
            super(context, mainView);
            mLikingMyModel = new LikingMyModel();
            mUserExerciseModel = new UserExerciseModel();
            mBleUtils = new BleUtils();
        }

        public void getUserData() {
            mLikingMyModel.getMyUserInfoData().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new LikingBaseObserver<MyUserOtherInfoResult>(mContext, mView) {
                @Override
                public void onNext(MyUserOtherInfoResult value) {
                    if (value == null) return;
                    if (value.getData() != null) {
                        mView.updateInfoData(value.getData());
                    }
                }
            });
        }


        public void getUserExerciseData() {
            mUserExerciseModel.getExerciseData().subscribe(new LikingBaseObserver<UserExerciseResult>(mContext, mView) {
                @Override
                public void onNext(UserExerciseResult value) {
                    if (value == null) return;
                    if (value.getExerciseData() != null) {
                        mView.updateExerciseData(value.getExerciseData());
                    }
                }

                @Override
                public void networkError(Throwable throwable) {
                    super.networkError(throwable);
                    mView.changeStateView(StateView.State.FAILED);
                }
            });
        }

        /**
         * 跳转到我的手环
         */
        public void jumpBraceletActivity(String mBraceletMac, String UUID) {
            if (LikingPreference.isLogin()) {
                if (!StringUtils.isEmpty(mBraceletMac)) {
                    LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
                }
                if (LikingPreference.isBind()) {//绑定过手环
                    if (initBlueTooth()) {
                        UMengCountUtil.UmengCount(mContext, UmengEventId.MYBRACELETACTIVITY);
                        Intent intent = new Intent(mContext, MyBraceletActivity.class);
                        if (!StringUtils.isEmpty(mBraceletMac)) {
                            intent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                        }
                        intent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                        intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, "");
                        intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, "");
                        intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "LikingMyFragment");
                        mContext.startActivity(intent);
                    }
                } else {//没有绑过
                    UMengCountUtil.UmengCount(mContext, UmengEventId.BINGBRACELETACTIVITY);
                    Intent intent = new Intent(mContext, BingBraceletActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        intent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                    }
                    intent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                    mContext.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        }


        public void jumpBracelet(String mBraceletMac,String UUID) {
            if (LikingPreference.isLogin()) {
                if (LikingPreference.isBind()) {
                    UMengCountUtil.UmengCount(mContext, UmengEventId.EVERYDAYSPORTACTIVITY);
                    Intent everydaySportIntent = new Intent(mContext, EveryDaySportActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        everydaySportIntent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                        LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
                    }
                    everydaySportIntent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                    mContext.startActivity(everydaySportIntent);
                } else {
                    jumpBodyTestActivity();
                }
            } else {
                Intent intent = new Intent(mContext,LoginActivity.class);
                mContext.startActivity(intent);
            }
        }

        /**
         * 跳转到体测评分界面
         */
        public void jumpBodyTestActivity() {
            UMengCountUtil.UmengCount(mContext, UmengEventId.BODYTESTDATAACTIVITY);
            Intent intent = new Intent(mContext, BodyTestDataActivity.class);
            intent.putExtra(BodyTestDataActivity.BODY_ID, "");
            intent.putExtra(BodyTestDataActivity.SOURCE, "other");
            mContext.startActivity(intent);
        }


        /**
         * 初始化蓝牙
         */
        public boolean initBlueTooth() {
            if (!mBleUtils.isOpen()) {
                openBluetooth();
                return false;
            } else {
                return true;
            }
        }

        /**
         * 打开蓝牙
         */
        public void openBluetooth() {
            mBleUtils.openBlueTooth((Activity) mContext);
        }


    }

}
