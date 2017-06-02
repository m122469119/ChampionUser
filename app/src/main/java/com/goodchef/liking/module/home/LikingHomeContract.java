package com.goodchef.liking.module.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.retrofit.result.data.HomeAnnouncement;
import com.goodchef.liking.data.remote.retrofit.result.data.NoticeData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.utils.FileDownloaderManager;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Set;

import io.reactivex.functions.Consumer;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:59
 * version 1.0.0
 */

class LikingHomeContract {

    interface View extends BaseView {
        void showNoticesDialog(Set<NoticeData> noticeData);
    }

    public static class Presenter extends RxBasePresenter<View> {
        LikingHomeModel mLikingHomeModel;
        FileDownloaderManager mFileDownloaderManager;
        CheckUpdateAppResult.UpdateAppData mUpdateAppData;

        public Presenter(Context context) {
            mLikingHomeModel = new LikingHomeModel();
            mFileDownloaderManager = new FileDownloaderManager(context);
        }

        //检测更新app
        void getAppUpdate(final Context context) {
            mLikingHomeModel.getUpdateApp()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<CheckUpdateAppResult>(mView) {

                        @Override
                        public void onNext(CheckUpdateAppResult value) {
                            if (value == null) return;
                            if (value.getData() != null) {
                                mUpdateAppData = value.getData();
                                checkUpdateApp(context);
                            }
                        }
                    }));
        }

        /**
         * 处理更新业务
         *
         * @param context
         */
        void checkUpdateApp(Context context) {
            if (mUpdateAppData == null) {
                return;
            }
            int update = mUpdateAppData.getUpdate();
            if (update == NumberConstantUtil.ZERO) {//无更新
                LikingPreference.setUpdateApp(NumberConstantUtil.ZERO);
            } else if (update == NumberConstantUtil.ONE) {//有更新
                LikingPreference.setUpdateApp(NumberConstantUtil.ONE);
                LikingPreference.setNewApkName(mUpdateAppData.getLastestVer());
                if (LikingPreference.getIsUpdate()) {
                    LikingPreference.setIsUpdateApp(false);
                    showCheckUpdateDialog(context, false);
                }
            } else if (update == NumberConstantUtil.TWO) {//强制更新
                LikingPreference.setUpdateApp(NumberConstantUtil.TWO);
                LikingPreference.setNewApkName(mUpdateAppData.getLastestVer());
                showCheckUpdateDialog(context, true);
            }
        }

        /**
         * 更新app弹出框
         *
         * @param isForceUpdate
         */
        private void showCheckUpdateDialog(final Context context, final boolean isForceUpdate) {
            new RxPermissions((Activity) context)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (!aBoolean) return;
                            HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
                            android.view.View view = LayoutInflater.from(context).inflate(R.layout.item_textview, null, false);
                            TextView textView = (TextView) view.findViewById(R.id.dialog_custom_title);
                            textView.setText((mUpdateAppData.getTitle()));
                            builder.setCustomTitle(view);
                            builder.setMessage(mUpdateAppData.getContent());
                            if (!isForceUpdate) {
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            builder.setPositiveButton(R.string.dialog_app_update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!StringUtils.isEmpty(mUpdateAppData.getUrl())) {
                                        mFileDownloaderManager.downloadFile(mUpdateAppData.getUrl(), DiskStorageManager.getInstance().getApkFileStoragePath());
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
        }

        /**
         * 处理push对话框
         */
        void showPushDialog() {
            if (LikingPreference.isHomeAnnouncement()) {
                HomeAnnouncement homeAnnouncement = LikingPreference.getHomeAnnouncement();
                mView.showNoticesDialog(homeAnnouncement.getNoticeDatas());
                LikingPreference.clearHomeAnnouncement();
            }
        }


        /**
         * 初始化gymId
         */
        void initHomeGymId() {
            String id = LikingPreference.getLoginGymId();
            if (!StringUtils.isEmpty(id) && !NumberConstantUtil.STR_ZERO.equals(id)) {
                LikingHomeActivity.gymId = id;
            } else {
                LikingHomeActivity.gymId = NumberConstantUtil.STR_ZERO;
            }
        }


    }
}
