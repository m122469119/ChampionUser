package com.goodchef.liking.module.home.myfragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.bodytest.BodyTestDataActivity;
import com.goodchef.liking.module.brace.bind.BingBraceletActivity;
import com.goodchef.liking.module.brace.braceletdata.BraceletDataActivity;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.module.train.UserExerciseModel;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:31
 * version 1.0.0
 */

class LikingMyContract {

    interface View extends BaseStateView {
        void updateInfoData(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData);

        void updateExerciseData(UserExerciseResult.ExerciseData exerciseData);

        void jumpMyBraceletActivity();
    }

    public static class Presenter extends RxBasePresenter<View> {
        private static final String TAG = "LikingMyContractPresenter";
        LikingMyModel mLikingMyModel;
        UserExerciseModel mUserExerciseModel;
        private BleUtils mBleUtils;//蓝牙Util

        public Presenter() {
            mLikingMyModel = new LikingMyModel();
            mUserExerciseModel = new UserExerciseModel();
            mBleUtils = new BleUtils();
        }

        void getUserData() {
            mLikingMyModel.getMyUserInfoData()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<MyUserOtherInfoResult>(mView) {

                        @Override
                public void onNext(MyUserOtherInfoResult value) {
                    if (value == null) return;
                    if (value.getData() != null) {
                        mView.updateInfoData(value.getData());
                    }
                }
            }));
        }


        void getUserExerciseData() {
            mUserExerciseModel.getExerciseData().subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<UserExerciseResult>(mView) {

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
            }));
        }

        /**
         * 跳转到我的手环
         */
        void jumpBraceletActivity(Context context, Fragment f, String UUID, String mBraceletMac) {
            if (LikingPreference.isLogin()) {
                if (!StringUtils.isEmpty(mBraceletMac)) {
                    LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
                }
                if (LikingPreference.isBind()) {//绑定过手环
                    if (initBlueTooth(f)) {
                        mView.jumpMyBraceletActivity();
                    }
                } else {//没有绑过
                    UMengCountUtil.UmengCount(context, UmengEventId.BINGBRACELETACTIVITY);
                    Intent intent = new Intent(context, BingBraceletActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        intent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                    }
                    intent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                    context.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        }


        void jumpBracelet(Context context, String UUID, String mBraceletMac) {
            if (LikingPreference.isLogin()) {
                if (LikingPreference.isBind()) {
                    UMengCountUtil.UmengCount(context, UmengEventId.EVERYDAYSPORTACTIVITY);
                    Intent everydaySportIntent = new Intent(context, BraceletDataActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        everydaySportIntent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                        LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
                    }
                    everydaySportIntent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                    context.startActivity(everydaySportIntent);
                } else {
                    jumpBodyTestActivity(context);
                }
            } else {
                Intent intent = new Intent(context,LoginActivity.class);
                context.startActivity(intent);
            }
        }

        /**
         * 跳转到体测评分界面
         * @param context
         */
        void jumpBodyTestActivity(Context context) {
            UMengCountUtil.UmengCount(context, UmengEventId.BODYTESTDATAACTIVITY);
            Intent intent = new Intent(context, BodyTestDataActivity.class);
            intent.putExtra(BodyTestDataActivity.BODY_ID, "");
            intent.putExtra(BodyTestDataActivity.SOURCE, "other");
            context.startActivity(intent);
        }


        /**
         * 初始化蓝牙
         * @param
         */
        boolean initBlueTooth(Fragment fragment) {
            if (!mBleUtils.isOpen()) {
                openBluetooth(fragment);
                return false;
            } else {
                return true;
            }
        }

        /**
         * 打开蓝牙
         */
        void openBluetooth(Fragment context) {
            mBleUtils.openBlueTooth( context);
        }

    }

}
