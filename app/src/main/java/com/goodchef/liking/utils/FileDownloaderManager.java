package com.goodchef.liking.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:40
 * version 1.0.0
 */

public class FileDownloaderManager {
    private ProgressBar mDownloadApkProgressBar;
    private TextView mDownloadApkProgressTextView;
    private AlertDialog mDownloadApkDialog;
    private DownloadNewApkBroadcast mDownloadNewApkBroadcast;
    private Context mContext;
    public static boolean isCreate = true;

    public FileDownloaderManager(Context context) {
        mContext = context;
    }

    public void downloadFile(String downloadUrl, String downloadPath) {
        // boolean update = Preference.getDownloadAppFile();
        if (isCreate) {
            LogUtils.d("socket", "FileDownloaderManager");
            //   Preference.setDownloadAppFile(true);
            isCreate = false;
            View downloadApkDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_download_apk, null, false);
            mDownloadApkProgressBar = (ProgressBar) downloadApkDialogView.findViewById(R.id.progressbar_download_apk);
            mDownloadApkProgressTextView = (TextView) downloadApkDialogView.findViewById(R.id.textview_download_apk);
            mDownloadApkDialog = new AlertDialog.Builder(mContext)
                    .setView(downloadApkDialogView)
                    .create();
            mDownloadApkDialog.setCancelable(false);
            mDownloadApkDialog.setCanceledOnTouchOutside(false);
            mDownloadApkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
            DialogUtils.resetDialogScreenPosition(mDownloadApkDialog, Gravity.CENTER, 0, 0);
            mDownloadApkDialog.show();
            registerDownloadNewApkBroadcast();
            startDownloadApk(downloadUrl, downloadPath);
        }
    }

    private void startDownloadApk(String downloadUrl, String downloadPath) {
        Intent intent = new Intent(mContext, FileDownloadService.class);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_URL, downloadUrl);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_PATH, downloadPath);
        mContext.startService(intent);
    }


    private void registerDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast == null) {
            mDownloadNewApkBroadcast = new DownloadNewApkBroadcast();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileDownloadService.ACTION_DOWNLOADING);
        intentFilter.addAction(FileDownloadService.ACTION_START_DOWNLOAD);
        intentFilter.addAction(FileDownloadService.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(mDownloadNewApkBroadcast, intentFilter);
    }

    public class DownloadNewApkBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isEmpty(action)) {
                return;
            }
            if (mDownloadApkDialog != null && mDownloadApkDialog.isShowing() && mDownloadApkProgressBar != null) {
                if (action.equals(FileDownloadService.ACTION_DOWNLOADING)) {
                    int progress = intent.getIntExtra(FileDownloadService.EXTRA_PROGRESS, 0);
                    float percent = (float) progress / mDownloadApkProgressBar.getMax() * 100;
                    mDownloadApkProgressBar.setProgress(progress);
                    mDownloadApkProgressTextView.setText(
                            mContext.getString(R.string.main_download_apk_progress, percent));
                } else if (action.equals(FileDownloadService.ACTION_START_DOWNLOAD)) {
                    mDownloadApkProgressBar.setMax(intent.getIntExtra(FileDownloadService.EXTRA_APK_LENGTH, 0));
                } else if (action.equals(FileDownloadService.ACTION_DOWNLOAD_COMPLETE)) {
                    mDownloadApkDialog.dismiss();
                    isCreate = true;
//                    Preference.setDownloadAppFile(false);
//                    Intent intent1 = new Intent();
//                    ComponentName componentName = new ComponentName("com.liking.aerobicopener", "com.liking.aerobicopener.MainActivity");
//                    intent1.setComponent(componentName);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent1);
//                    ApkController.install(intent.getStringExtra(FileDownloadService.EXTRA_INSTALL_APK_PATH), mContext);

//                     unregisterDownloadNewApkBroadcast();
                    LikingPreference.setUpdateApp(0);//将更新设置为不用更新
                    //调用系统安装
                    String dirPath = DiskStorageManager.getInstance().getApkFileStoragePath();
                    String apkName = LikingPreference.getNewAPkName();
                    String filePath = dirPath + apkName + ".apk"; //文件需有可读权限
                    Intent intent1 = new Intent();
                    intent1.setAction(android.content.Intent.ACTION_VIEW);
                    intent1.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    BaseApplication.exitApp();
                } else if (action.equals(FileDownloadService.ACTION_DOWNLOAD_FAIL)) {
                    mDownloadApkDialog.dismiss();
                    unregisterDownloadNewApkBroadcast();
                }
            }
        }
    }

    public void unregisterDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast != null) {
            mContext.unregisterReceiver(mDownloadNewApkBroadcast);
            mDownloadNewApkBroadcast = null;
        }
    }

}
