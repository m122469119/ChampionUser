package com.goodchef.liking.http.api;

import com.aaron.android.framework.utils.EnvironmentUtils;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:13
 */
public class UrlList {
    public static String HOST_VERSION = "/v2";

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
    /**我的会员卡详情*/
    public static final String GET_CARD_ORDER_DETAILS = getVersionHostUrl()+"/order/get-card-order-detail";
    /**获取我的会员卡*/
    public static final String GET_MY_CARD = getVersionHostUrl()+"/card/get-my-card";
    /**获取团体课常光列表*/
    public static final String GET_GYM_COURESE = getVersionHostUrl()+"/gym/course";
    /**获取我的个人信息*/
    public static final String GET_USER_INFO = getVersionHostUrl()+"/user/info";
    /**更新我的个人信息*/
    public static final String UPDATE_USER = getVersionHostUrl()+"/user/update";
    /**上传图片*/  //测试环境上传图片域名testimg.likingfit.com，正式环境尚未给出  正式：http://img.likingfit.com/upload/image
    public static final String UPLOAD_USER_IMAGE = EnvironmentUtils.Config.isDebugMode() ? "http://testimg.likingfit.com/upload/image" : "http://img.likingfit.com/upload/image";
    /**上报手机信息*/
    public static final String USER_DEVICE = getVersionHostUrl()+"/user/device";
    /**获取我的邀请码*/
    public static final String GET_INVITE_CODE = getVersionHostUrl()+"/user/get-my-invitate-code";
    /**提交邀请码*/
    public static final String EXCHANGE_INVITE_CODE = getVersionHostUrl()+"/user/exchange-code";
    /**联系加盟或者成为教练*/
    public static final String JOIN_APPLY = getVersionHostUrl()+"/user/join-apply";
    /**场馆详情*/
    public static final String GYM_INFO = getVersionHostUrl()+"/gym/info";
    /**获取的运动数据*/
    public static final String GET_USER_EXERCISE_DATA = getVersionHostUrl()+"/user/get-exercise-data";
    /**查看场馆*/
    public static final String GET_GYM_LIST = getVersionHostUrl()+"/gym/get-all-gym";
    /**获取开门密码*/
    public static final String GET_USER_AUTHCODE = getVersionHostUrl()+"/user/authcode";
    /**上报接口错误信息*/
    public static final String UPLOAD_ERROR = getVersionHostUrl() + "/index/err-log";
    /**获取我的付费团体课详情*/
    public static final String ORDER_GET_COURSE_DETAIL = getVersionHostUrl() + "/order/get-course-detail";
    /**计算私教课金额*/
    public static final String ORDER_CALCULATE = getVersionHostUrl() + "/order/calculate";
    /**付费团体课确认订单*/
    public static final String ORDER_CHANGE_GROUP_CONFIRM = getVersionHostUrl() + "/order/team-course-confirm";
    /**收费团体课购买*/
    public static final String ORDER_SUBMIT_TEAM_COURSE = getVersionHostUrl() + "/order/submit-team-course";
    /**私教课分享*/
    public static final String TRAINER_SHARE = getVersionHostUrl() + "/user/trainer-share";
    /**团体课分享*/
    public static final String USER_TEAM_SHARE = getVersionHostUrl() + "/user/team-share";
    /**运动数据分享*/
    public static final String USER_EXERCISE_SHARE = getVersionHostUrl() + "/user/exercise-share";
    /**获取用户自助排课页面的时间表*/
    public static final String COURSE_GYM_SCHEDULE_INFO = getVersionHostUrl() + "/course/gym-schedule-info";
    /**选择排课列表*/
    public static final String COURSE_CAN_SCHEDULE_COURSE_LIST = getVersionHostUrl() + "/course/can-schedule-course-list";
    /**预约团体课*/
    public static final String COURSE_ADD_SCHEDULE = getVersionHostUrl() + "/course/add-schedule";
    /**获取体测数据*/
    public static final String USER_GET_USER_BODY = getVersionHostUrl() + "/user/get-user-body";
    /**获取体测历史*/
    public static final String USER_GET_BODY_LIST = getVersionHostUrl() + "/user/get-body-list";
    /** 获取历史页面顶部导航*/
    public static final String USER_BODY_MODULES_HISTORY = getVersionHostUrl() + "/user/body-modules-history";
    /**获取体测数据单个字段历史值*/
    public static final String USER_BODY_COLUMN_HISTORY = getVersionHostUrl() + "/user/body-column-history";
    /**绑定手环*/
    public static final String USER_BIND_DEVICE = getVersionHostUrl() + "/user/bind-device";
    /**解绑手环*/
    public static final String USER_UNBIND = getVersionHostUrl() + "/user/unbind";


}
