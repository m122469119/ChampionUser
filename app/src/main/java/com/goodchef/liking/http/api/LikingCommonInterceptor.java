package com.goodchef.liking.http.api;

import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.data.remote.LikingBaseRequestHelper;

import java.io.IOException;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 16/11/29.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingCommonInterceptor implements Interceptor {
    public static final String KEY_APP_VERSION = "app_version";
    private static final String PLATFORM_ANDROID = "android";
    private static final String KEY_PLATFORM = "platform";
    private static final String TAG = "CommonInterceptor";

    public LikingCommonInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        LikingBaseRequestHelper.sRequestTimestamp = DateUtils.currentDataSeconds() + LikingBaseRequestHelper.sTimestampOffset;
        LogUtils.i(TAG, "request timestamp: " + DateUtils.formatDate(LikingBaseRequestHelper.sRequestTimestamp * 1000L, 3, "-"));
        LogUtils.i(TAG, "current system timestamp: " + DateUtils.formatDate(DateUtils.currentDataSeconds() * 1000L, 3, "-"));
        LogUtils.i(TAG, "timestamp offset: " + DateUtils.formatDuring(LikingBaseRequestHelper.sTimestampOffset * 1000L));
        Request oldRequest = chain.request();
        String requestId = RequestParamsHelper.genRandomNum(100000000, 999999999);
        HttpUrl.Builder httpUrlBuilder = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("signature", RequestParamsHelper.getSignature(String.valueOf(LikingBaseRequestHelper.sRequestTimestamp), requestId))
                .addQueryParameter("timestamp", String.valueOf(LikingBaseRequestHelper.sRequestTimestamp))
                .addQueryParameter("request_id", requestId)
                .addQueryParameter(KEY_APP_VERSION, EnvironmentUtils.Config.getAppVersionName())
                .addQueryParameter(KEY_PLATFORM, PLATFORM_ANDROID);
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(httpUrlBuilder.build())
                .build();
        Set<String> queryKeys =  newRequest.url().queryParameterNames();
        String queryValue;
        for (String params : queryKeys) {
            queryValue = newRequest.url().queryParameter(params);
            LogUtils.d(TAG, "queryKey: " + params + " queryValue: " + queryValue);
        }
        LogUtils.d(TAG, "request url: " + newRequest.url().url().toString());
        return chain.proceed(newRequest);
    }
}
