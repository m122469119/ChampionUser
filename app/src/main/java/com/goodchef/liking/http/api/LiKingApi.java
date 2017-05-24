package com.goodchef.liking.http.api;

import android.os.Build;
import android.util.Base64;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.http.RequestParams;
import com.aaron.http.code.NetworkErrorResponse;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.http.statistics.NetworkStatistics;
import com.aaron.http.volley.VolleyHttpRequestClient;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.ShareResult;
import com.goodchef.liking.http.result.SyncTimestampResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:17
 */
public class LiKingApi {
    private static final String TAG = "LiKingApi";
    private static final String KEY_TOKEN = "token";
    public static final String KEY_APP_VERSION = "app_version";
    private static final String PLATFORM_ANDROID = "android";
    private static final String KEY_PLATFORM = "platform";
    public static long sTimestampOffset = 0;
    public static long sRequestTimestamp = 0;
    public static long sRequestSyncTimestamp = 0;

    public static RequestParams getCommonRequestParams() {
        if (VolleyHttpRequestClient.sNetworkStatistics == null) {
            VolleyHttpRequestClient.sNetworkStatistics = new NetworkStatistics() {
                @Override
                public void post(RequestError error) {
                    if (error == null) {
                        return;
                    }
                    StringBuilder urlStringBuffer = new StringBuilder();
                    String url = error.getUrl();
                    urlStringBuffer.append(url + "?");
                    Map<String, Object> params = error.getRequestParams();
                    Iterator<String> iterator = params.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        urlStringBuffer.append(key + "=" + params.get(key));
                        if (iterator.hasNext()) {
                            urlStringBuffer.append("&");
                        }
                    }
                    url = urlStringBuffer.toString();
                    LogUtils.d(TAG, "error url: " + url);
                    if (StringUtils.isEmpty(url)) {
                        return;
                    }
                    String urlEncodeUrl = null;
                    try {
                        urlEncodeUrl = URLEncoder.encode(url, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    LogUtils.d(TAG, "error encodeUrl: " + urlEncodeUrl);
                    if (StringUtils.isEmpty(urlEncodeUrl)) {
                        return;
                    }
                    String base64EncodeUrl = Base64.encodeToString(urlEncodeUrl.getBytes(), Base64.DEFAULT);
                    LogUtils.d(TAG, "base64EncodeUrl: " + base64EncodeUrl);

                    StringBuilder errorMessageString = new StringBuilder();
                    errorMessageString.append("errorType: " + error.getErrorType() + ";");
                    NetworkErrorResponse errorResponse = error.getErrorNetworkErrorResponse();
                    if (errorResponse != null) {
                        int statusCode = errorResponse.statusCode;
                        errorMessageString.append("statusCode: " + statusCode + ";");
                    }
                    errorMessageString.append("errorMessage: " + error.getMessage());
                    String errorMessage = errorMessageString.toString();
                    LogUtils.d(TAG, "errorMessage: " + errorMessage);
                    uploadNetworkError(base64EncodeUrl, errorMessage, new RequestCallback<LikingResult>() {
                        @Override
                        public void onSuccess(LikingResult likingResult) {
                            LogUtils.d(TAG, "upload error message success");
                        }

                        @Override
                        public void onFailure(RequestError error) {
                            LogUtils.d(TAG, "upload error message failure");
                        }
                    });
                }
            };
        }
        sRequestTimestamp = DateUtils.currentDataSeconds() + sTimestampOffset;
        LogUtils.i(TAG, "request timestamp: " + DateUtils.formatDate(sRequestTimestamp * 1000L, 3, "-"));
        LogUtils.i(TAG, "current system timestamp: " + DateUtils.formatDate(DateUtils.currentDataSeconds() * 1000L, 3, "-"));
        LogUtils.i(TAG, "timestamp offset: " + DateUtils.formatDuring(LiKingApi.sTimestampOffset * 1000L));
        String requestId = RequestParamsHelper.genRandomNum(100000000, 999999999);
        RequestParams requestParams = new RequestParams();
        requestParams.append("signature", RequestParamsHelper.getSignature(String.valueOf(sRequestTimestamp), requestId));
        requestParams.append("timestamp", sRequestTimestamp);
        requestParams.append("request_id", requestId);
        requestParams.append(KEY_APP_VERSION, EnvironmentUtils.Config.getAppVersionName());
        requestParams.append(KEY_PLATFORM, PLATFORM_ANDROID);
        LogUtils.i(TAG, requestParams.getParams().toString());
        return requestParams;
    }

    /**
     * 基础配置信息
     *
     * @param callback RequestCallback
     */
    public static void baseConfig(RequestCallback<BaseConfigResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.BASE_CONFIG, BaseConfigResult.class, null, getCommonRequestParams(), callback);
    }


    /**
     * 同步服务器时间戳
     *
     * @param callback RequestCallback
     */
    public static void syncServerTimestamp(RequestCallback<SyncTimestampResult> callback) {
        sRequestSyncTimestamp = DateUtils.currentDataSeconds();
        VolleyHttpRequestClient.doPost(UrlList.SYNC_SERVER_TIMESTAMP, SyncTimestampResult.class, null, getCommonRequestParams()
                , callback);
    }


    /***
     * 上传设备信息
     *
     * @param token           token
     * @param device_id       设备id
     * @param device_token    设备token
     * @param registration_id 极光推送id
     * @param callback        RequestCallback
     */
    public static void uploadUserDevice(String token, String device_id, String device_token, String registration_id, RequestCallback<LikingResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_DEVICE, LikingResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("device_id", device_id).append("device_token", device_token).append("registration_id", registration_id).append("os_version", Build.VERSION.RELEASE)
                .append("phone_type", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL), callback);
    }

    /**
     * 上报错误接口
     *
     * @param url          请求链接
     * @param errorMessage 错误信息
     */
    public static void uploadNetworkError(String url, String errorMessage, RequestCallback<LikingResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.UPLOAD_ERROR, LikingResult.class, getCommonRequestParams().append("url", url)
                .append("error_msg", errorMessage), callback);
    }

    /***
     * 私教课分享
     *
     * @param trainerId 教练id
     * @param callback  RequestCallback
     */
    public static void getPrivateCoursesShare(String trainerId, RequestCallback<ShareResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.TRAINER_SHARE, ShareResult.class, getCommonRequestParams()
                .append("trainer_id", trainerId), callback);
    }


    /**
     * 团体课分享
     *
     * @param scheduleId 排期id
     * @param callback   RequestCallback
     */
    public static void getGroupCoursesShare(String scheduleId, RequestCallback<ShareResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_TEAM_SHARE, ShareResult.class, getCommonRequestParams()
                .append("schedule_id", scheduleId), callback);
    }

    /**
     * 我的运动数据分享
     *
     * @param token    token
     * @param callback RequestCallback
     */
    public static void getUserShare(String token, RequestCallback<ShareResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_EXERCISE_SHARE, ShareResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token), callback);
    }

    /**
     * 绑定手环
     *
     * @param braceletName    用户手环名称
     * @param braceletVersion 用户手环固件版本
     * @param deviceId        用户设备id
     * @param platform        用户设备平台
     * @param deviceName      用户设备名称
     * @param osVersion       用户手环设备版本
     * @param callback        RequestCallback
     */
    public static void bindDevices(String braceletName, String braceletVersion, String deviceId, String platform, String deviceName, String osVersion, RequestCallback<LikingResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_BIND_DEVICE, LikingResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, LikingPreference.getToken()).append("bracelet_name", braceletName)
                .append("bracelet_version", braceletVersion).append("device_id", deviceId)
                .append("platform", platform).append("device_name", deviceName).append("os_version", osVersion), callback);
    }

    /**
     * 解绑手环
     *
     * @param devicesId 设备id
     * @param callback  RequestCallback
     */
    public static void unBindDevices(String devicesId, RequestCallback<LikingResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_UNBIND, LikingResult.class, getCommonRequestParams()
                .append("device_id", devicesId).append(KEY_TOKEN, LikingPreference.getToken()), callback);
    }

}