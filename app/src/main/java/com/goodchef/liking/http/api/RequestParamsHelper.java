package com.goodchef.liking.http.api;

import com.aaron.android.codelibrary.utils.DecriptUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;


import java.util.Arrays;

/**
 * Created on 16/1/11.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class RequestParamsHelper {
    private static final String TAG = "RequestParamsHelper";
    public static String REQUEST_APP_KEY = EnvironmentUtils.Config.isDebugMode() ? "testCcmsIam500QiangA" : "gIhErwWVDaM4jefERmoR"; //app签名key

    //正式环境的支付公钥为(yGcNQznPTvusj7Y6rMI5)
    public static final String SET_PASSWORD_KEY = EnvironmentUtils.Config.isDebugMode() ? "vZr8m0erpGqLbLThv4ov" : "yGcNQznPTvusj7Y6rMI5";//测试环境支付公钥

    public static String getSignature(String timeStamp, String requestId) {
        StringBuffer stringBuffer = new StringBuffer();
        LogUtils.d(TAG, "支付加密预埋: " + SET_PASSWORD_KEY + " app签名key: " + REQUEST_APP_KEY);
        Object[] commonKeys = {timeStamp, requestId, REQUEST_APP_KEY};
        Arrays.sort(commonKeys);
        for (int i = 0; i < commonKeys.length; i++) {
            stringBuffer.append(commonKeys[i]);
            LogUtils.i(TAG, "key: " + commonKeys[i]);
        }
        String signature = stringBuffer.toString();
        LogUtils.i(TAG, "signature: " + signature);
        return DecriptUtils.SHA1(signature);
    }

    /**
     * @param min
     * @param max
     * @return 密码的字符串
     */
    public static String genRandomNum(long min, long max) {
        int num = (int) (min + Math.random() * (max - min));
        return String.valueOf(num);
    }
}
