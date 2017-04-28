package com.goodchef.liking.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.aaron.common.utils.FileUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.goodchef.liking.http.result.data.PatchData;
import com.goodchef.liking.module.data.local.Preference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lennon on 16/3/12.
 */
public class PatchDowner extends AsyncTask<PatchData, Integer, PatchData> {
    private static final String TAG = "PatchDowner";
    private Context context;
    private String patchDirPath;
    String patchName = "";

    public PatchDowner(Context context) {
        this.context = context;
        patchDirPath = DiskStorageManager.getInstance().getFilePath() + "patch/";
        File patchDir = FileUtils.createFolder(patchDirPath);
        if (patchDir == null) {
            LogUtils.e(TAG, "patchDir create fail");
        } else {
            LogUtils.e(TAG, "patchDir create success");
        }
    }

    @Override
    protected PatchData doInBackground(PatchData... params) {
        if (params[0] != null) {
            String urlS = params[0].getPatchUrl();
            if (!TextUtils.isEmpty(urlS)) {
                patchName = urlS.substring(urlS.lastIndexOf("/"));
                File patchFile = new File(patchDirPath, patchName);
                if (patchFile.exists()) {
                    LogUtils.i(TAG, "补丁存在");
                    params[0].setPatchFile(patchFile.getAbsolutePath());
                    LogUtils.d(TAG, "patchFile: " + patchFile.getAbsolutePath());
                    return params[0];
                } else {
                    LogUtils.i("Dust", "补丁不存在，开始下载");
                    try {
                        URL url = new URL(urlS);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        int responseCode = conn.getResponseCode();
                        if (responseCode == 200) {
                            FileOutputStream fos = new FileOutputStream(patchFile);
                            InputStream inStream = conn.getInputStream();
                            byte[] buff = new byte[1024];
                            int len = 0;
                            while ((len = inStream.read(buff)) != -1) {
                                fos.write(buff, 0, len);
                            }
                            inStream.close();
                            fos.close();
                            params[0].setPatchFile(patchFile.getAbsolutePath());
                            LogUtils.d(TAG, "patchFile: " + patchFile.getAbsolutePath());
                            return params[0];
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(PatchData s) {
        if (s != null) {
            LogUtils.i("Dust", s.toString());
            Preference.savePatchData(s);
        }
    }
}
