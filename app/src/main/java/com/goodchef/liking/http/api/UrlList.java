package com.goodchef.liking.http.api;

import com.aaron.android.framework.utils.EnvironmentUtils;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:13
 */
public class UrlList {
    public static String HOST_VERSION = "/v1";

    public static String getVersionHostUrl() {
        return getHostUrl() + HOST_VERSION;
    }

    public static String getHostUrl() {
        return EnvironmentUtils.Config.getHttpRequestUrlHost();
    }
    /**基础配置*/
    public static final String BASE_CONFIG = getHostUrl() +  "/config/config";
    /**同步时间戳*/
    public static final String SYNC_SERVER_TIMESTAMP = getHostUrl() + "/time/timestamp/";
    /**获取验证码*/
    public static final String GET_VERIFICATION_CODE = getHostUrl() +  "/sms/captcha";
    /**用户登录*/
    public static final String USER_LOGIN = getVersionHostUrl() + "/user/login";
    /**登出*/
    public static final String LOGIN_OUT = getVersionHostUrl()+"/user/logout";
    /**首页*/
    public static final String HOME_INDEX = getVersionHostUrl()+"/index/index";
    /**团体课详情*/
    public static final String GROUP_LESSON_DETAILS = getVersionHostUrl()+"/course/info";
    /**私教课详情*/
    public static final String PRIVATE_LESSON_DETAILS = getVersionHostUrl()+"/trainer/info";


}
