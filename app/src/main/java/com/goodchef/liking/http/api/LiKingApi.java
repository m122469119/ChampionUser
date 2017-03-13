package com.goodchef.liking.http.api;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.aaron.android.codelibrary.http.NetworkErrorResponse;
import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.DateUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.http.RequestParams;
import com.aaron.android.framework.library.http.statistics.NetworkStatistics;
import com.aaron.android.framework.library.http.volley.VolleyHttpRequestClient;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.http.result.BodyModelNavigationResult;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.DishesOrderListResult;
import com.goodchef.liking.http.result.FoodDetailsResult;
import com.goodchef.liking.http.result.FoodListResult;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.GymCoursesResult;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.http.result.GymListResult;
import com.goodchef.liking.http.result.InviteFriendResult;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.result.MyDishesOrderDetailsResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.result.MyUserOtherInfoResult;
import com.goodchef.liking.http.result.NutritionMealConfirmResult;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.result.ShareResult;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.result.SyncTimestampResult;
import com.goodchef.liking.http.result.UserAuthCodeResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.storage.Preference;

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
    public static final String KEY_PAGE_NUM = "pageNum";
    public static final String DEFAULT_PAGE_SIZE = "20";
    private static final String KEY_PAGE = "current_page";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_CITY_ID = "city_id";
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
                    uploadNetworkError(base64EncodeUrl, errorMessage, new RequestCallback<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult result) {
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
        VolleyHttpRequestClient.doPost(UrlList.USER_LOGIN, UserLoginResult.class, null, getCommonRequestParams()
                .append("phone", phone).append("captcha", captcha), callback);
    }


    /***
     * 退出登录
     *
     * @param token          token
     * @param registrationId 极光注册ID //没有可不传或传0
     * @param callback       RequestCallback
     */
    public static void userLoginOut(String token, String registrationId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.LOGIN_OUT, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token).append("registration_id", registrationId), callback);
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
    public static void getHomeData(String token, String longitude, String latitude, String cityId, String districtId, int currentPage, String gymId, RequestCallback<CoursesResult> callback) {
        RequestParams params = getCommonRequestParams().append("longitude", longitude)
                .append("latitude", latitude).append(KEY_CITY_ID, cityId)
                .append(KEY_DISTRICT_ID, districtId).append(KEY_PAGE, currentPage).append("gym_id", gymId);
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.HOME_INDEX, CoursesResult.class, params, callback);
    }


    /**
     * 获取团体课详情
     *
     * @param scheduleId 课程排序id
     * @param callback   RequestCallback
     */
    public static void getGroupLessonDetails(String scheduleId, RequestCallback<GroupCoursesResult> callback) {
        RequestParams params = getCommonRequestParams().append("schedule_id", scheduleId);
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
    public static void orderGroupCourses(String gymId, String scheduleId, String token, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_GROUP_COURSES, BaseResult.class, getCommonRequestParams().append("gym_id", gymId).append("schedule_id", scheduleId).append(KEY_TOKEN, token), callback);
    }

    /**
     * 私教课确认预约订单
     *
     * @param trainerId 教练ID
     * @param token     token
     * @param callback  RequestCallback
     */
    public static void orderPrivateCoursesConfirm(String gymId, String trainerId, String token, RequestCallback<PrivateCoursesConfirmResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.PRIVATE_ORDER_CONFIRM, PrivateCoursesConfirmResult.class, getCommonRequestParams().append("trainer_id", trainerId)
                .append(KEY_TOKEN, token).append("gym_id", gymId), callback);
    }

    /**
     * 获取优惠券
     *
     * @param courseId 私教课下单确认页需要传
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getCoupons(String courseId, String selectTimes, String goodInfo, String cardId, String type, String scheduleId, String token, int page, String gymId, RequestCallback<CouponsResult> callback) {
        RequestParams params = getCommonRequestParams().append(KEY_TOKEN, token).append("page", page);
        if (!TextUtils.isEmpty(courseId)) {
            params.append("course_id", courseId);
        }
        if (!TextUtils.isEmpty(selectTimes)) {
            params.append("select_times", selectTimes);
        }
        if (!TextUtils.isEmpty(goodInfo)) {
            params.append("good_info", goodInfo);
        }
        if (!TextUtils.isEmpty(cardId)) {
            params.append("card_id", cardId);
        }
        if (!TextUtils.isEmpty(type)) {
            params.append("type", type);
        }
        if (!TextUtils.isEmpty(scheduleId)) {
            params.append("schedule_id", scheduleId);
        }
        if (!TextUtils.isEmpty(gymId)) {
            params.append("gym_id", gymId);
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
    public static void submitPrivateCourses(String token, String courseId, String couponCode, String payType, int selectTimes, String gymId, RequestCallback<SubmitPayResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_PRIVATE_COURSES_PAY, SubmitPayResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("course_id", courseId).append("coupon_code", couponCode).append("pay_type", payType).append("select_times", selectTimes)
                .append("gym_id", gymId), callback);
    }

    /**
     * 获取健身卡列表
     *
     * @param callback RequestCallback
     */
    public static void getCardList(String token, String longitude, String latitude, String cityId, String districtId, String gymId, int type, RequestCallback<CardResult> callback) {
        RequestParams params = getCommonRequestParams().append("type", type);

        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        if (!TextUtils.isEmpty(longitude)) {
            params.append("longitude", longitude);
        }
        if (!TextUtils.isEmpty(latitude)) {
            params.append("latitude", latitude);
        }
        if (!TextUtils.isEmpty(cityId)) {
            params.append(KEY_CITY_ID, cityId);
        }
        if (!TextUtils.isEmpty(districtId)) {
            params.append(KEY_DISTRICT_ID, districtId);
        }
        if (!TextUtils.isEmpty(gymId)) {
            params.append("gym_id", gymId);
        }

        VolleyHttpRequestClient.doPost(UrlList.CARD_LIST, CardResult.class, params, callback);
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
    public static void getFoodList(String longitude, String latitude, String cityId, int page, RequestCallback<FoodListResult> callback) {
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


    /***
     * 获取我的订单详情
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void getDishesDetails(String token, String orderId, RequestCallback<MyDishesOrderDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_ORDER_DETAILS, MyDishesOrderDetailsResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }


    /**
     * 营养餐立即支付
     *
     * @param orderId  订单id
     * @param payType  支付方式
     * @param callback RequestCallback
     */
    public static void myDishesOrderPay(String orderId, String payType, RequestCallback<SubmitPayResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_ORDER_PAY, SubmitPayResult.class, getCommonRequestParams().append("order_id", orderId)
                .append("pay_type", payType), callback);
    }


    /***
     * 取消营养餐
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void cancelMyDishesOrder(String token, String orderId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_CANCEL_ORDER, BaseResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }

    /**
     * 完成订单
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void completeMyDishesOrder(String token, String orderId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.FOOD_COMPLETE_ORDER, BaseResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("order_id", orderId), callback);
    }

    /***
     * 确认购卡
     *
     * @param token      token
     * @param type       类型 1购卡页 2续卡 3升级卡
     * @param categoryId 类别ID
     * @param callback   RequestCallback
     */
    public static void confirmCard(String token, int type, int categoryId, String gymId, RequestCallback<ConfirmBuyCardResult> callback) {
        RequestParams params = getCommonRequestParams().append("type", type).append("category_id", categoryId)
                .append("gym_id", gymId);
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.CARD_CONFIRM, ConfirmBuyCardResult.class, params, callback);
    }

    /***
     * 提交买卡订单
     *
     * @param token      token
     * @param cardId     cardId
     * @param type       类型 1购卡页 2续卡 3升级卡
     * @param couponCode 优惠券code
     * @param payType    支付方式
     * @param callback   RequestCallback
     */
    public static void submitBuyCardData(String token, int cardId, int type, String couponCode, String payType, String gymId, RequestCallback<SubmitPayResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.CARD_SUBMIT_CARD, SubmitPayResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("card_id", cardId).append("type", type).append("coupon_code", couponCode).append("pay_type", payType)
                .append("gym_id", gymId), callback);
    }


    /**
     * 兑换优惠券
     *
     * @param token        token
     * @param exchangeCode 优惠券码
     * @param callback     RequestCallback
     */
    public static void exchangeCoupon(String token, String exchangeCode, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.COUPON_EXCHANGE_COUPON, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("exchange_code", exchangeCode), callback);
    }


    /***
     * 获取我的会员卡列表
     *
     * @param token    token
     * @param page     页数
     * @param callback RequestCallback
     */
    public static void getCardOrderList(String token, int page, RequestCallback<OrderCardListResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_CARD_ORDER_LIST, OrderCardListResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("page", page), callback);
    }


    /***
     * 获取我的会员卡详情
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void getCardDetails(String token, String orderId, RequestCallback<MyOrderCardDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_CARD_ORDER_DETAILS, MyOrderCardDetailsResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("order_id", orderId), callback);
    }


    /**
     * 获取我的会员卡信息
     *
     * @param token    token
     * @param callback RequestCallback
     */
    public static void getMyCard(String token, RequestCallback<MyCardResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_MY_CARD, MyCardResult.class, getCommonRequestParams().append(KEY_TOKEN, token), callback);
    }


    /***
     * 获取场馆团体课列表
     *
     * @param gymId    场馆id
     * @param date     时间
     * @param callback RequestCallback
     */
    public static void getGymCoursesList(String gymId, String date, RequestCallback<GymCoursesResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_GYM_COURESE, GymCoursesResult.class, getCommonRequestParams().append("gym_id", gymId)
                .append("format", date), callback);
    }


    /***
     * 获取的个人信息
     *
     * @param token    token
     * @param callback RequestCallback
     */
    public static void getUserInfo(String token, RequestCallback<UserInfoResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_USER_INFO, UserInfoResult.class, getCommonRequestParams().append(KEY_TOKEN, token), callback);
    }

    public static void updateUserInfo(String token, String name, String avatar, Integer gender, String birthday, String weight, String height, RequestCallback<BaseResult> callback) {
        RequestParams params = getCommonRequestParams().append(KEY_TOKEN, token);
        if (!StringUtils.isEmpty(name)) {
            params.append("name", name);
        }
        if (!StringUtils.isEmpty(avatar)) {
            params.append("avatar", avatar);
        }
        if (gender != null) {
            params.append("gender", gender);
        }
        if (!StringUtils.isEmpty(birthday)) {
            params.append("birthday", birthday);
        }
        if (!StringUtils.isEmpty(weight)) {
            params.append("weight", weight);
        }
        if (!StringUtils.isEmpty(height)) {
            params.append("height", height);
        }
        VolleyHttpRequestClient.doPost(UrlList.UPDATE_USER, BaseResult.class, params, callback);
    }

    /***
     * 上传头像
     *
     * @param img      头像base64 字符串
     * @param callback RequestCallback
     */
    public static void uploadUserImage(String img, RequestCallback<UserImageResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.UPLOAD_USER_IMAGE, UserImageResult.class, getCommonRequestParams().append("img", img), callback);
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
    public static void uploadUserDevice(String token, String device_id, String device_token, String registration_id, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_DEVICE, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("device_id", device_id).append("device_token", device_token).append("registration_id", registration_id).append("os_version", Build.VERSION.RELEASE)
                .append("phone_type", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL), callback);
    }

    /***
     * 获取我的邀请码
     *
     * @param token    token
     * @param callback RequestCallback
     */
    public static void getInviteCode(String token, RequestCallback<InviteFriendResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_INVITE_CODE, InviteFriendResult.class, getCommonRequestParams().append(KEY_TOKEN, token), callback);
    }

    /**
     * 提交邀请码
     *
     * @param token    token
     * @param code     邀请码
     * @param callback RequestCallback
     */
    public static void exchangeInviteCode(String token, String code, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.EXCHANGE_INVITE_CODE, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("code", code), callback);
    }


    /***
     * 联系加盟或者成为私教
     *
     * @param name     姓名
     * @param phone    手机号码
     * @param city     所在城市
     * @param type     类型 0加盟  1成为教练
     * @param callback RequestCallback
     */
    public static void joinApply(String name, String phone, String city, int type, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.JOIN_APPLY, BaseResult.class, getCommonRequestParams().append("name", name)
                .append("phone", phone).append("city", city).append("type", type), callback);
    }


    /**
     * 获取场馆详情
     *
     * @param gymId    场馆id
     * @param callback RequestCallback
     */
    public static void getGymDetails(String gymId, RequestCallback<GymDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GYM_INFO, GymDetailsResult.class, getCommonRequestParams().append("gym_id", gymId), callback);
    }


    /***
     * 获取我的运动数据
     *
     * @param token    token
     * @param callback RequestCallback
     */
    public static void getUserExerciseData(String token, RequestCallback<UserExerciseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_USER_EXERCISE_DATA, UserExerciseResult.class, getCommonRequestParams().append(KEY_TOKEN, token), callback);
    }


    /**
     * 查看场馆
     *
     * @param cityId   城市id
     * @param callback RequestCallback
     */
    public static void getCheckGymList(String token, int cityId, String longitude, String latitude, RequestCallback<CheckGymListResult> callback) {
        RequestParams params = getCommonRequestParams().append("city_id", cityId);
        if (!StringUtils.isEmpty(longitude)) {
            params.append("longitude", longitude);
        }
        if (!StringUtils.isEmpty(latitude)) {
            params.append("latitude", latitude);
        }
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.GET_GYM_LIST, CheckGymListResult.class, params, callback);
    }


    /**
     * 获取开门密码
     *
     * @param token    token
     * @param inout    获取类别 1进门 2出门
     * @param callback RequestCallback
     */
    public static void getUserAuthCode(String token, int inout, RequestCallback<UserAuthCodeResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.GET_USER_AUTHCODE, UserAuthCodeResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("inout", inout), callback);
    }

    /**
     * 上报错误接口
     *
     * @param url          请求链接
     * @param errorMessage 错误信息
     */
    public static void uploadNetworkError(String url, String errorMessage, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.UPLOAD_ERROR, BaseResult.class, getCommonRequestParams().append("url", url)
                .append("error_msg", errorMessage), callback);
    }


    /**
     * 获取我的付费团体课详情
     *
     * @param token    token
     * @param orderId  订单id
     * @param callback RequestCallback
     */
    public static void getMyChargeGroupCoursesDetails(String token, String orderId, RequestCallback<MyChargeGroupCoursesDetailsResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_GET_COURSE_DETAIL, MyChargeGroupCoursesDetailsResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("order_id", orderId), callback);
    }


    /***
     * 计算私教课金额
     *
     * @param token       token
     * @param courseId    课程id
     * @param selectTimes 选择的课次
     * @param callback    RequestCallback
     */
    public static void orderCalculate(String token, String courseId, String selectTimes, RequestCallback<OrderCalculateResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_CALCULATE, OrderCalculateResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("course_id", courseId).append("select_times", selectTimes), callback);
    }

    /***
     * 付费团体课确认订单
     *
     * @param scheduleId 排期id
     * @param callback   RequestCallback
     */
    public static void chargeGroupCoursesConfirm(String token, String gymId, String scheduleId, RequestCallback<ChargeGroupConfirmResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_CHANGE_GROUP_CONFIRM, ChargeGroupConfirmResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("schedule_id", scheduleId).append("gym_id", gymId), callback);
    }


    /***
     * 付费团体课提交订单获取支付数据
     *
     * @param token      token
     * @param scheduleId 排期id
     * @param couponCode 优惠券吗
     * @param payType    支付方式
     * @param callback   RequestCallback
     */
    public static void chargeGroupCoursesImmediately(String token, String gymId, String scheduleId, String couponCode, String payType, RequestCallback<SubmitPayResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.ORDER_SUBMIT_TEAM_COURSE, SubmitPayResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, token).append("schedule_id", scheduleId)
                .append("gym_id", gymId).append("coupon_code", couponCode).append("pay_type", payType), callback);
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

    /***
     * 获取自助团体课时间表
     *
     * @param token    token
     * @param gymId    场馆地
     * @param callback RequestCallback
     */
    public static void getSelfCorsesTimeList(String token, String gymId, RequestCallback<SelfHelpGroupCoursesResult> callback) {
        RequestParams params = getCommonRequestParams();
        if (!TextUtils.isEmpty(token)) {
            params.append(KEY_TOKEN, token);
        }
        VolleyHttpRequestClient.doPost(UrlList.COURSE_GYM_SCHEDULE_INFO, SelfHelpGroupCoursesResult.class,
                params.append("gym_id", gymId), callback);
    }

    /***
     * 获取选择自助团体课的列表
     *
     * @param page     页码
     * @param callback RequestCallback
     */
    public static void getSelfCoursesList(int page, RequestCallback<SelfGroupCoursesListResult> callback) {
        //.append(KEY_TOKEN, Preference.getToken())
        VolleyHttpRequestClient.doPost(UrlList.COURSE_CAN_SCHEDULE_COURSE_LIST, SelfGroupCoursesListResult.class, getCommonRequestParams()
                .append("page", page), callback);
    }

    /***
     * 预约团体课
     *
     * @param gymId       场馆id
     * @param roomId      操房id
     * @param coursesId   课程id
     * @param coursesDate 日期
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param price       价格
     * @param peopleNum   人数
     * @param callback    RequestCallback
     */
    public static void orderCourses(String token, String gymId, String roomId, String coursesId, String coursesDate, String startTime, String endTime, String price, String peopleNum, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.COURSE_ADD_SCHEDULE, BaseResult.class, getCommonRequestParams().append(KEY_TOKEN, token)
                .append("gym_id", gymId).append("room_id", roomId).append("course_id", coursesId).append("course_date", coursesDate)
                .append("start_time", startTime).append("end_time", endTime).append("price", price)
                .append("people_num", peopleNum), callback);
    }

    /**
     * 获取体测数据
     *
     * @param bodyId   体测数据id,可选,不传为最新数据
     * @param callback RequestCallback
     */
    public static void getBodyTestData(String bodyId, RequestCallback<BodyTestResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_GET_USER_BODY, BodyTestResult.class, getCommonRequestParams()
                .append("body_id", bodyId).append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /***
     * 获取体测历史
     *
     * @param page     页码
     * @param callback RequestCallback
     */
    public static void getBodyHistory(int page, RequestCallback<BodyHistoryResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_GET_BODY_LIST, BodyHistoryResult.class, getCommonRequestParams().append(KEY_TOKEN, Preference.getToken()).append("page", page)
                , callback);
    }

    /**
     * 获取体测历史页面顶部导航
     *
     * @param modules  模块名称
     * @param callback RequestCallback
     */
    public static void getBodyHistoryTitleList(String modules, RequestCallback<BodyModelNavigationResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_BODY_MODULES_HISTORY, BodyModelNavigationResult.class, getCommonRequestParams()
                .append("modules", modules).append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /**
     * 获取体测历史下面数据
     *
     * @param column   导航栏目
     * @param callback RequestCallback
     */
    public static void getBodyHistoryList(String column, RequestCallback<BodyAnalyzeHistoryResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_BODY_COLUMN_HISTORY, BodyAnalyzeHistoryResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, Preference.getToken()).append("column", column), callback);
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
    public static void bindDevices(String braceletName, String braceletVersion, String deviceId, String platform, String deviceName, String osVersion, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_BIND_DEVICE, BaseResult.class, getCommonRequestParams()
                .append(KEY_TOKEN, Preference.getToken()).append("bracelet_name", braceletName)
                .append("bracelet_version", braceletVersion).append("device_id", deviceId)
                .append("platform", platform).append("device_name", deviceName).append("os_version", osVersion), callback);
    }

    /**
     * 解绑手环
     *
     * @param devicesId 设备id
     * @param callback  RequestCallback
     */
    public static void unBindDevices(String devicesId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_UNBIND, BaseResult.class, getCommonRequestParams()
                .append("device_id", devicesId).append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /**
     * 上报运动数据
     *
     * @param sportData 运动数据
     * @param deviceId  设备id
     * @param callback  RequestCallback
     */
    public static void sendSportData(String sportData, String deviceId, RequestCallback<BaseResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_SAVE_SPORT_DATA, BaseResult.class, getCommonRequestParams()
                .append("sport_data", sportData).append("device_id", deviceId).append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /**
     * 获取运动数据
     *
     * @param callback RequestCallback
     */
    public static void getSportData(RequestCallback<SportDataResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_SPORT_LIST, SportDataResult.class, getCommonRequestParams().append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /**
     * 我的界面获取我的其他个人信息
     *
     * @param callback RequestCallback
     */
    public static void getMyUserInfoData(RequestCallback<MyUserOtherInfoResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_GET_USER_INFO, MyUserOtherInfoResult.class, getCommonRequestParams().append(KEY_TOKEN, Preference.getToken()), callback);
    }

    /**
     * 检测更新APP
     *
     * @param callback RequestCallback
     */
    public static void getUpdateApp(RequestCallback<CheckUpdateAppResult> callback) {
        VolleyHttpRequestClient.doPost(UrlList.USER_CHECK_UPDATES, CheckUpdateAppResult.class, getCommonRequestParams(), callback);
    }
}