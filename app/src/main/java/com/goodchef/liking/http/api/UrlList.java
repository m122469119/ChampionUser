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
    /**首页banner*/
    public static final String HOME_BANNER = getVersionHostUrl()+"/index/banner";
    /**预约团体课*/
    public static final String ORDER_GROUP_COURSES = getVersionHostUrl()+"/order/team-course-reserve";
    /**私教课确认预约*/
    public static final String PRIVATE_ORDER_CONFIRM = getVersionHostUrl()+"/order/confirm";
    /**获取优惠券*/
    public static final String GET_COUPON = getVersionHostUrl()+"/coupon/fetch-coupon";
    /**预约私教课支付*/
    public static final String ORDER_PRIVATE_COURSES_PAY = getVersionHostUrl()+"/order/trainer-reserve";
    /**健身卡列表*/
    public static final String CARD_LIST = getVersionHostUrl()+"/card/list";
    /**团体课列表*/
    public static final String MY_ORDER_COURSES_LIST = getVersionHostUrl()+"/order/team-course-list";
    /**我的私教课列表*/
    public static final String MY_ORDER_PRIVATE_LIST = getVersionHostUrl()+"/order/personal-course-list";
    /**我的私教课详情*/
    public static final String MY_ORDER_PRIVATE_DETAILS = getVersionHostUrl()+"/order/course-detail";
    /**私教课确认完成*/
    public static final String COMPLETE_MY_PRIVATE_COURSES = getVersionHostUrl()+"/order/complete-trainer-course";
    /**取消团体课*/
    public static final String CANCEL_GROUP_COURSES = getVersionHostUrl()+"/order/cancel-team-course";
    /**营养餐列表*/
    public static final String FOOD_LIST = getVersionHostUrl()+"/food/list";
    /**营养餐详情*/
    public static final String FOOD_DETAILS = getVersionHostUrl()+"/food/detail";


}
