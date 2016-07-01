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
    /**购买营养餐确认订单页*/
    public static final String FOOD_CONFIRM = getVersionHostUrl()+"/food/confirm";
    /**获取切换门店的列表*/
    public static final String FOOD_GET_GYM_LIST = getVersionHostUrl()+"/food/get-gym-list";
    /**营养餐确认订单*/
    public static final String FOOD_ODER_SUBMIT = getVersionHostUrl()+"/food/order-submit";
    /**营养餐订单列表*/
    public static final String FOOD_ORDER_LIST = getVersionHostUrl()+"/food/food-order-list";
    /**营养餐订单详情*/
    public static final String FOOD_ORDER_DETAILS = getVersionHostUrl()+"/food/food-order-detail";
    /**营养餐订单立即支付*/
    public static final String FOOD_ORDER_PAY = getVersionHostUrl()+"/food/pay-order";
    /**取消营养餐*/
    public static final String FOOD_CANCEL_ORDER = getVersionHostUrl()+"/food/cancel-order";
    /**完成订单*/
    public static final String FOOD_COMPLETE_ORDER = getVersionHostUrl()+"/food/complete-order";
    /**购卡确认*/
    public static final String CARD_CONFIRM = getVersionHostUrl()+"/card/confirm";
    /**买卡提交确认*/
    public static final String CARD_SUBMIT_CARD = getVersionHostUrl()+"/order/submit-card";
    /**兑换优惠券*/
    public static final String COUPON_EXCHANGE_COUPON = getVersionHostUrl()+"/coupon/exchange-coupon";
    /**获取我的会员卡订单列表*/
    public static final String GET_CARD_ORDER_LIST = getVersionHostUrl()+"/order/get-card-order-list";

}
