package com.goodchef.liking.http.api;

import android.text.TextUtils;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.http.RequestParams;
import com.aaron.android.framework.library.http.volley.VolleyHttpRequestClient;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.DishesOrderListResult;
import com.goodchef.liking.http.result.FoodDetailsResult;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.GymListResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.result.SyncTimestampResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.storage.Preference;

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
    public static final String KEY_PAGE_NUM = "pageNum";
    public static final String DEFAULT_PAGE_SIZE = "20";
    private static final String KEY_PAGE = "current_page";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_CITY_ID = "city_id";
    public static long sTimestampOffset = 0;
    public static long sRequestTimestamp = 0;
    public static long sRequestSyncTimestamp = 0;

    public static RequestParams getCommonRequestParams() {
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
     * 同步服务器时间戳
     *
     * @param callback RequestCallback
     */
    public static void syncServerTimestamp(RequestCallback<SyncTimestampResult> callback) {
        sRequestSyncTimestamp = DateUtils.currentDataSeconds();
        VolleyHttpRequestClient.doPost(UrlList.SYNC_SERVER_TIMESTAMP, SyncTimestampResult.class, null, getCommonRequestParams()
                , callback);
    }

    /**
     * 获取验证码
     *
     * @param phone    手机号码
     * @param callback RequestCallback
     */
    public static void getVerificationCode(String phone, RequestCallback<VerificationCodeResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_VERIFICATION_CODE, VerificationCodeResult.class, null,
                getCommonRequestParams().append("phone", phone), callback);
    }


    /**
     * 用户登录
     *
     * @param phone    手机号码
     * @param captcha  验证码
     * @param callback RequestCallback
     */
    public static void userLogin(String phone, String captcha, RequestCallback<UserLoginResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_LOGIN, UserLoginResult.class, null, getCommonRequestParams().append("phone", phone).append("captcha", captcha), callback);
    }


    /***
     * 退出登录
     *
     * @param token          token
     * @param registrationId 极光注册ID //没有可不传或传0
     * @param callback       RequestCallback
     */
    public static void userLoginOut(String token, String registrationId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.LOGIN_OUT, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token).append("registrationId", registrationId), callback);
    }


    /***
     * 首页课程列表
     *
     * @param longitude   经度
     * @param latitude    纬度
     * @param cityId      城市id
     * @param districtId  街道id
     * @param currentPage 页数
     * @param callback    RequestCallback
     */
    public static void getHomeData(double longitude, double latitude, String cityId, String districtId, int currentPage, RequestCallback<CoursesResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.HOME_INDEX, CoursesResult.class, getCommonRequestParams().append("longitude", longitude)
                .append("latitude", latitude).append(KEY_CITY_ID, cityId).append(KEY_DISTRICT_ID, districtId).append(KEY_PAGE, currentPage), callback);
    }


    /**
     * 获取团体课详情
     *
     * @param scheduleId 课程排序id
     * @param callback   RequestCallback
     */
    public static void getGroupLessonDetails(String scheduleId, RequestCallback<GroupCoursesResult> callback) {
        RequestParams params = getCommonRequestParams().append(KEY_TOKEN, Preference.getToken()).append("schedule_id", scheduleId);
        String token = Preference.getToken();
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.GROUP_LESSON_DETAILS, GroupCoursesResult.class, params, callback);
    }


    /***
     * 私教课详情
     *
     * @param trainerId 私教id
     * @param callback  RequestCallback
     */
    public static void getPrivateCoursesDetails(String trainerId, RequestCallback<PrivateCoursesResult> callback) {
        RequestParams params = getCommonRequestParams().append("trainer_id", trainerId);
        String token = Preference.getToken();
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.PRIVATE_LESSON_DETAILS, PrivateCoursesResult.class, params, callback);
    }

    /**
     * 获取首页banner
     *
     * @param callback RequestCallback
     */
    public static void getBanner(RequestCallback<BannerResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.HOME_BANNER, BannerResult.class, getCommonRequestParams(), callback);
    }

    /**
     * 团体课预约
     *
     * @param scheduleId 团体课排期id
     * @param token      token
     * @param callback   RequestCallback
     */
    public static void orderGroupCourses(String scheduleId, String token, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_GROUP_COURSES, BaseResult.class, getCommonRequestParams().append("schedule_id", scheduleId).append(KEY_TOKEN, token), callback);
    }

    /**
     * 私教课确认预约订单
     *
     * @param trainerId 教练ID
     * @param token     token
     * @param callback  RequestCallback
     */
    public static void orderPrivateCoursesConfirm(String trainerId, String token, RequestCallback<PrivateCoursesConfirmResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.PRIVATE_ORDER_CONFIRM, PrivateCoursesConfirmResult.class, getCommonRequestParams().append("trainer_id", trainerId)
                .append(KEY_TOKEN, token), callback);
    }

    /**
     * 获取优惠券
     *
     * @param courseId 私教课下单确认页需要传
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getCoupons(String courseId, String goodInfo, String token, int page, RequestCallback<CouponsResult> callback) {
        RequestParams params = getCommonRequestParams().append(KEY_TOKEN, token).append("page", page);
        if (!TextUtils.isEmpty(courseId)) {
            params.append("course_id", courseId);
        }
        if (!TextUtils.isEmpty(goodInfo)) {
            params.append("good_info", goodInfo);
        }
        VolleyHttpRequestClient.doPost(UrlList.GET_COUPON, CouponsResult.class, params, callback);
    }


    /**
     * 提交预约私教课
     *
     * @param token      token
     * @param courseId   课程id
     * @param couponCode 优惠券
     * @param payType    支付方式
     * @param callback   RequestCallback
     */
    public static void submitPrivateCourses(String token, String courseId, String couponCode, String payType, RequestCallback<SubmitPayResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_PRIVATE_COURSES_PAY, SubmitPayResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("course_id", courseId).append("coupon_code", couponCode).append("pay_type", payType), callback);
    }

    /**
     * 获取健身卡列表
     *
     * @param callback RequestCallback
     */
    public static void getCardList(RequestCallback<CardResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.CARD_LIST, CardResult.class, getCommonRequestParams(), callback);
    }

    /**
     * 获取我的团体课列表
     *
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getMyGroupList(String token, int page, RequestCallback<MyGroupCoursesResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.MY_ORDER_COURSES_LIST, MyGroupCoursesResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("page", page), callback);
    }


    /**
     * 获取我的私教课列表
     *
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getMyPrivateList(String token, int page, RequestCallback<MyPrivateCoursesResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.MY_ORDER_PRIVATE_LIST, MyPrivateCoursesResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("page", page), callback);
    }


    /***
     * 获取我的私教课详情
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void getMyPrivateCoursesDetails(String token, String orderId, RequestCallback<MyPrivateCoursesDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.MY_ORDER_PRIVATE_DETAILS, MyPrivateCoursesDetailsResult.class,
                getCommonRequestParams().append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }

    /**
     * 完成我的私教课
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void completerMyPrivateCourses(String token, String orderId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.COMPLETE_MY_PRIVATE_COURSES, BaseResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }


    /**
     * 取消团体课
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void cancelGroupCourses(String token, String orderId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.CANCEL_GROUP_COURSES, BaseResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }


    /**
     * 获取营养餐列表接口
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param cityId    城市id
     * @param callback  RequestCallback
     */
    public static void getFoodList(double longitude, double latitude, String cityId, int page, RequestCallback<FoodListResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_LIST, FoodListResult.class, getCommonRequestParams()
                .append("longitude", longitude).append("latitude", latitude).append("city_id", cityId).append("page", page), callback);
    }


    /**
     * 获取营养餐详情
     *
     * @param useCityId 城市id
     * @param goodsId   食物id
     * @param callback  RequestCallback
     */
    public static void getFoodDetails(String useCityId, String goodsId, RequestCallback<FoodDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_DETAILS, FoodDetailsResult.class, getCommonRequestParams()
                .append("user_city_id", useCityId).append("goods_id", goodsId), callback);
    }


    /**
     * 购买营养餐确认订单
     *
     * @param token      token
     * @param userCityId 城市id
     * @param goodInfo   购买的信息
     * @param callback   RequestCallback
     */
    public static void confirmFood(String token, String userCityId, String goodInfo, RequestCallback<NutritionMealConfirmResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_CONFIRM, NutritionMealConfirmResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("user_city_id", userCityId).append("good_info", goodInfo), callback);
    }


    /**
     * 切换门店
     *
     * @param userCityId 城市id
     * @param good_info  选择的营养餐
     * @param callback   RequestCallback
     */
    public static void getGymList(String userCityId, String good_info, RequestCallback<GymListResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_GET_GYM_LIST, GymListResult.class, getCommonRequestParams()
                .append("user_city_id", userCityId).append("good_info", good_info), callback);
    }


    /**
     * 提交营养餐支付订单
     *
     * @param token       token
     * @param gym_id      场馆id
     * @param take_time   取餐时间
     * @param coupon_code 优惠券code
     * @param good_info   购买菜品
     * @param pay_type    支付方式
     * @param callback    RequestCallback
     */
    public static void submitDishesOrder(String token, String gym_id, String take_time, String coupon_code, String good_info, String pay_type, RequestCallback<SubmitPayResult> callback) {
        RequestParams params = getCommonRequestParams().append(KEY_TOKEN, token).append("gym_id", gym_id)
                .append("take_time", take_time).append("good_info", good_info).append("pay_type", pay_type);
        if (!StringUtils.isEmpty(coupon_code)) {
            params.append("coupon_code", coupon_code);
        }
        VolleyHttpRequestClient.doPost(UrlList.FOOD_ODER_SUBMIT, SubmitPayResult.class, params, callback);
    }

    /**
     * 获取营养餐订单列表
     *
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getDishesOrderList(String token, int page, RequestCallback<DishesOrderListResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_ORDER_LIST, DishesOrderListResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("page", page), callback);
    }
}
