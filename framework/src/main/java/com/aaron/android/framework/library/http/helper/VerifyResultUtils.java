package com.aaron.android.framework.library.http.helper;

import android.content.Context;

import com.aaron.android.codelibrary.http.result.BaseResult;

/**
 * Created on 15/7/4.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class VerifyResultUtils {
    /**
     * 检查是否成功，若不成功弹出错误信息
     * @param result BaseResult
     * @return boolean
     */
    public static boolean checkResultSuccess(Context context, BaseResult result) {
        if (context == null || result == null) {
            return false;
        }
//        if (context instanceof Activity && ((Activity)context).isFinishing()) {
//            return false;
//        }
        if (!result.isSuccess()) {
            return false;
        }
        return true;
    }

}
