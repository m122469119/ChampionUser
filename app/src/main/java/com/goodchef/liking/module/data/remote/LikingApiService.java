package com.goodchef.liking.module.data.remote;

import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.http.result.BodyModelNavigationResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.CityListResult;
import com.goodchef.liking.http.result.ConfirmBuyCardResult;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.http.result.CouponsDetailsResult;
import com.goodchef.liking.http.result.CouponsPersonResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.result.UserAuthCodeResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created on 17/3/13.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public interface LikingApiService {
    String PATH_VERSION = "version";
    String KEY_PHONE = "phone";
    String KEY_TOKEN = "token";
    String KEY_DISTRICT_ID = "district_id";
    String KEY_CITY_ID = "city_id";

    @POST(Urls.USER_LOGIN)
    Observable<UserLoginResult> userLogin(@Path(PATH_VERSION) String version,
                                          @Query("phone") String phone,
                                          @Query("captcha") String captcha);

    @POST(Urls.GET_VERIFICATION_CODE)
    Observable<VerificationCodeResult> getVerificationCode(@Query(KEY_PHONE) String phone);

    @POST(Urls.CHECK_UPDATE)
    Observable<CheckUpdateAppResult> getCheckUpdateAppResult(@Path(PATH_VERSION) String version);

    @POST(Urls.USER_LOGOUT)
    Observable<LikingResult> userLogout(@Path(PATH_VERSION) String version,
                                        @Query(KEY_TOKEN) String token,
                                        @Query("registration_id") String registrationId);

    @POST(Urls.JOIN_APPLY)
    Observable<LikingResult> joinApply(@Path(PATH_VERSION) String version,
                                       @Query("name") String name,
                                       @Query("phone") String phone,
                                       @Query("city") String city,
                                       @Query("type") int type);

    @POST(Urls.GET_MY_CARD)
    Observable<MyCardResult> getMyCard(@Path(PATH_VERSION) String version,
                                       @Query(KEY_TOKEN) String token);

    @POST(Urls.GET_CARD_ORDER_DETAILS)
    Observable<MyOrderCardDetailsResult> getMyOrderCardDetails(@Path(PATH_VERSION) String version,
                                                               @Query(KEY_TOKEN) String token,
                                                               @Query("order_id") String orderId);

    @POST(Urls.CARD_LIST)
    Observable<CardResult> getCardList(@Path(PATH_VERSION) String version,
                                       @Query(KEY_TOKEN) String token,
                                       @Query("longitude") String longitude,
                                       @Query("latitude") String latitude,
                                       @Query(KEY_CITY_ID) String cityId,
                                       @Query(KEY_DISTRICT_ID) String districtId,
                                       @Query("gym_id") String gymId,
                                       @Query("type") int type);

    @POST(Urls.GET_CARD_ORDER_LIST)
    Observable<OrderCardListResult> getCardOrderList(@Path(PATH_VERSION) String version,
                                                     @Query(KEY_TOKEN) String token,
                                                     @Query("page") int page);


    @POST(Urls.GET_USER_EXERCISE_DATA)
    Observable<UserExerciseResult> getUserExerciseData(@Path(PATH_VERSION) String version,
                                                       @Query(KEY_TOKEN) String token);

    @POST(Urls.COUPON_EXCHANGE_COUPON)
    Observable<LikingResult> exchangeCoupon(@Path(PATH_VERSION) String version,
                                            @Query(KEY_TOKEN) String token,
                                            @Query("exchange_code") String code);

    @POST(Urls.GET_MY_COUPON)
    Observable<CouponsPersonResult> getMyCoupons(@Path(PATH_VERSION) String version,
                                                 @Query(KEY_TOKEN) String token,
                                                 @Query("page") int page);

    @POST(Urls.GET_COUPON)
    Observable<CouponsResult> getCoupons(@Path(PATH_VERSION) String version, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.MY_ORDER_COURSES_LIST)
    Observable<MyGroupCoursesResult> getTeamCourseList(@Path(PATH_VERSION) String version,
                                                       @Field(KEY_TOKEN) String token,
                                                       @Field("page") int page);

    @FormUrlEncoded
    @POST(Urls.CANCEL_GROUP_COURSES)
    Observable<LikingResult> cancelTeamCourse(@Path(PATH_VERSION) String version,
                                              @Field(KEY_TOKEN) String token,
                                              @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST(Urls.MY_ORDER_PRIVATE_LIST)
    Observable<MyPrivateCoursesResult> getPersonalCourseList(@Path(PATH_VERSION) String version,
                                                             @Field(KEY_TOKEN) String token,
                                                             @Field("page") int page);

    @FormUrlEncoded
    @POST(Urls.GROUP_LESSON_DETAILS)
    Observable<GroupCoursesResult> getCourseInfo(@Path(PATH_VERSION) String version,
                                                 @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.ORDER_GROUP_COURSES)
    Observable<LikingResult> teamCourseReserve(@Path(PATH_VERSION) String version,
                                               @Field(KEY_TOKEN) String token,
                                               @Field("gym_id") String gymId,
                                               @Field("schedule_id") String scheduleId);

    @FormUrlEncoded
    @POST(Urls.ORDER_GET_COURSE_DETAIL)
    Observable<MyChargeGroupCoursesDetailsResult>
    chargeGroupCoursesDetails(@Path(PATH_VERSION) String version,
                              @Field(KEY_TOKEN) String token,
                              @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST(Urls.MY_ORDER_PRIVATE_DETAILS)
    Observable<MyPrivateCoursesDetailsResult>
    getPersonalCourseDetails(@Path(PATH_VERSION) String version,
                             @Field(KEY_TOKEN) String token,
                             @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST(Urls.COMPLETE_MY_PRIVATE_COURSES)
    Observable<LikingResult> completeTrainerCourse(@Path(PATH_VERSION) String version,
                                                   @Field(KEY_TOKEN) String token,
                                                   @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST(Urls.COURSE_GYM_SCHEDULE_INFO)
    Observable<SelfHelpGroupCoursesResult>
    getSelfHelpScheduleInfo(@Path(PATH_VERSION) String version,
                            @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.COURSE_ADD_SCHEDULE)
    Observable<LikingResult> joinSelfHelpCourses(@Path(PATH_VERSION) String version,
                                                 @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.COURSE_CAN_SCHEDULE_COURSE_LIST)
    Observable<SelfGroupCoursesListResult> getScheduleCourseList(@Path(PATH_VERSION) String version,
                                                                 @Field("page") int page);

    @FormUrlEncoded
    @POST(Urls.TEST_UPLOAD_USER_IMAGE)
    Observable<UserImageResult> uploadDebugUserImage(@Field("img") String img);

    @FormUrlEncoded
    @POST(Urls.UPLOAD_USER_IMAGE)
    Observable<UserImageResult> uploadUserImage(@Field("img") String img);

    @FormUrlEncoded
    @POST(Urls.UPDATE_USER)
    Observable<LikingResult> updateUserInfo(@Path(PATH_VERSION) String version,
                                            @FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Urls.GET_USER_INFO)
    Observable<UserInfoResult> getUserInfo(@Path(PATH_VERSION) String version,
                                           @Field(KEY_TOKEN) String token);


    @POST(Urls.GET_GYM_LIST)
    Observable<CheckGymListResult> getCheckGymList(@Path(PATH_VERSION) String version,
                                                   @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.GET_USER_AUTHCODE)
    Observable<UserAuthCodeResult> getOpenPwd(@Path(PATH_VERSION) String version,
                                              @Field(KEY_TOKEN) String token,
                                              @Field("inout") int inout);

    @POST(Urls.ORDER_CHANGE_GROUP_CONFIRM)
    Observable<ChargeGroupConfirmResult> chargeGroupCoursesConfirm(@Path(PATH_VERSION) String version,
                                                                   @QueryMap Map<String, String> map);

    @POST(Urls.ORDER_SUBMIT_TEAM_COURSE)
    Observable<SubmitPayResult> chargeGroupCoursesImmediately(@Path(PATH_VERSION) String version,
                                                              @QueryMap Map<String, String> map);

    @POST(Urls.PRIVATE_ORDER_CONFIRM)
    Observable<PrivateCoursesConfirmResult> orderPrivateCoursesConfirm(@Path(PATH_VERSION) String version,
                                                                       @Query(KEY_TOKEN) String token,
                                                                       @Query("gym_id") String gymId,
                                                                       @Query("trainer_id") String trainerId);

    @POST(Urls.ORDER_PRIVATE_COURSES_PAY)
    Observable<SubmitPayResult> submitPrivateCourses(@Path(PATH_VERSION) String version,
                                                     @QueryMap Map<String, String> map);

    @POST(Urls.ORDER_CALCULATE)
    Observable<OrderCalculateResult> orderCalculate(@Path(PATH_VERSION) String version,
                                                    @Query(KEY_TOKEN) String token,
                                                    @Query("course_id") String courseId,
                                                    @Query("select_times") String selectTimes);

    @POST(Urls.PRIVATE_LESSON_DETAILS)
    Observable<PrivateCoursesResult> getPrivateCoursesDetails(@Path(PATH_VERSION) String version,
                                                              @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.GET_COUPON_DETAIL)
    Observable<CouponsDetailsResult> getCouponDetails(@Path(PATH_VERSION) String version,
                                                      @Field(KEY_TOKEN) String token,
                                                      @Field("coupon_code") String couponCode,
                                                      @Field("longitude") String longitude,
                                                      @Field("latitude") String latitude);

    @FormUrlEncoded
    @POST(Urls.GET_COUPON_GYM)
    Observable<CouponsCities> getCouponsGym(@Path(PATH_VERSION) String version,
                                            @Field("page") int page,
                                            @Field("coupon_code") String couponCode,
                                            @Field("longitude") String longitude,
                                            @Field("latitude") String latitude);

    @POST(Urls.USER_CHECK_UPDATES)
    Observable<CheckUpdateAppResult> getUpdateApp(@Path(PATH_VERSION) String version);


    @POST(Urls.HOME_BANNER)
    Observable<BannerResult> getBanner(@Path(PATH_VERSION) String version);


    @POST(Urls.HOME_INDEX)
    Observable<CoursesResult> getHomeData(@Path(PATH_VERSION) String version,
                                          @QueryMap Map<String, Object> map);

    @POST(Urls.CARD_CONFIRM)
    Observable<ConfirmBuyCardResult> cardConfirm(@Path(PATH_VERSION) String version,
                                                 @QueryMap Map<String, String> map);

    @POST(Urls.CARD_SUBMIT_CARD)
    Observable<SubmitPayResult> submitBuyCardData(@Path(PATH_VERSION) String version,
                                                  @QueryMap Map<String, String> map);

    @POST(Urls.CITY_LIST)
    Observable<CityListResult> getCityList(@Path(PATH_VERSION) String version);

    @POST(Urls.GYM_INFO)
    Observable<GymDetailsResult> getGymDetails(@Path(PATH_VERSION) String version,
                                               @Query("gym_id") String gymId);

    @POST(Urls.USER_GET_BODY_LIST)
    Observable<BodyHistoryResult> getHistoryData(@Path(PATH_VERSION) String version,
                                                 @Query("page") int page);

    @POST(Urls.USER_BODY_MODULES_HISTORY)
    Observable<BodyModelNavigationResult> getBodyHistoryTitleList(@Path(PATH_VERSION) String version,
                                                                  @Query(KEY_TOKEN) String token,
                                                                  @Query("modules") String modules);

    @POST(Urls.USER_BODY_COLUMN_HISTORY)
    Observable<BodyAnalyzeHistoryResult> getBodyHistoryList(@Path(PATH_VERSION) String version,
                                                            @Query(KEY_TOKEN) String token,
                                                            @Query("column") String column);

    class Urls {
        private static final String sVersion = "{version}/";
        /**
         * 获取验证码
         */
        static final String GET_VERIFICATION_CODE = "sms/captcha";
        /**
         * 检查更新
         */
        static final String CHECK_UPDATE = sVersion + "check-update/check-update";
        /**
         * 用户登录
         */
        static final String USER_LOGIN = sVersion + "user/login";
        /**
         * 用户登出
         */
        static final String USER_LOGOUT = sVersion + "user/logout";

        /**
         * 联系加盟或成为教练
         */
        static final String JOIN_APPLY = sVersion + "user/join-apply";

        /**
         * 获取我的会员卡
         */
        static final String GET_MY_CARD = sVersion + "card/get-my-card";

        /**
         * 我的会员卡详情
         */
        public static final String GET_CARD_ORDER_DETAILS = sVersion + "order/get-card-order-detail";

        /**
         * 健身卡列表
         */
        public static final String CARD_LIST = sVersion + "card/list";

        /**
         * 获取我的会员卡订单列表
         */
        public static final String GET_CARD_ORDER_LIST = sVersion + "order/get-card-order-list";

        /**
         * 获取的运动数据
         */
        public static final String GET_USER_EXERCISE_DATA = sVersion + "user/get-sports-data";

        /**
         * 兑换优惠券
         */
        public static final String COUPON_EXCHANGE_COUPON = sVersion + "coupon/exchange-coupon";

        /**
         * 个人界面获取优惠券
         */
        public static final String GET_MY_COUPON = sVersion + "coupon/get-my-coupon";

        /**
         * 获取优惠券
         */
        public static final String GET_COUPON = sVersion + "coupon/fetch-coupon";

        /**
         * 团体课列表
         */
        public static final String MY_ORDER_COURSES_LIST = sVersion + "order/team-course-list";

        /**
         * 取消团体课
         */
        public static final String CANCEL_GROUP_COURSES = sVersion + "order/cancel-team-course";

        /**
         * 我的私教课列表
         */
        public static final String MY_ORDER_PRIVATE_LIST = sVersion + "order/personal-course-list";

        /**
         * 团体课详情
         */
        public static final String GROUP_LESSON_DETAILS = sVersion + "course/info";

        /**
         * 预约团体课
         */
        public static final String ORDER_GROUP_COURSES = sVersion + "order/team-course-reserve";

        /**
         * 获取我的付费团体课详情
         */
        public static final String ORDER_GET_COURSE_DETAIL = sVersion + "order/get-course-detail";

        /**
         * 我的私教课详情
         */
        public static final String MY_ORDER_PRIVATE_DETAILS = sVersion + "order/course-detail";

        /**
         * 私教课确认完成
         */
        public static final String COMPLETE_MY_PRIVATE_COURSES = sVersion + "order/complete-trainer-course";

        /**
         * 获取用户自助排课页面的时间表
         */
        public static final String COURSE_GYM_SCHEDULE_INFO = sVersion + "course/gym-schedule-info";

        /**
         * 预约团体课
         */
        static final String COURSE_ADD_SCHEDULE = sVersion + "course/add-schedule";

        /**
         * 选择排课列表
         */
        static final String COURSE_CAN_SCHEDULE_COURSE_LIST = sVersion + "course/can-schedule-course-list";

        /**
         * 上传图片
         */
        static final String TEST_UPLOAD_USER_IMAGE = "http://testimg.likingfit.com/upload/image";
        static final String UPLOAD_USER_IMAGE = "http://img.likingfit.com/upload/image";

        /**
         * 更新我的个人信息
         */
        static final String UPDATE_USER = sVersion + "user/update";

        /**
         * 获取我的个人信息
         */
        public static final String GET_USER_INFO = sVersion + "user/info";

        /**
         * 查看场馆
         */
        public static final String GET_GYM_LIST = sVersion + "gym/get-all-gym";

        /**
         * 获取开门密码
         */
        public static final String GET_USER_AUTHCODE = sVersion + "user/authcode";

        /**
         * 付费团体课确认订单
         */
        public static final String ORDER_CHANGE_GROUP_CONFIRM = sVersion + "order/team-course-confirm";

        /**
         * 收费团体课购买
         */
        public static final String ORDER_SUBMIT_TEAM_COURSE = sVersion + "order/submit-team-course";

        /**
         * 私教课确认预约
         */
        public static final String PRIVATE_ORDER_CONFIRM = sVersion + "order/confirm";

        /**
         * 预约私教课支付
         */
        public static final String ORDER_PRIVATE_COURSES_PAY = sVersion + "order/trainer-reserve";

        /**
         * 计算私教课金额
         */
        public static final String ORDER_CALCULATE = sVersion + "order/calculate";

        /**
         * 私教课详情
         */
        public static final String PRIVATE_LESSON_DETAILS = sVersion + "trainer/info";

        /**
         * 获取优惠券详情
         */
        public static final String GET_COUPON_DETAIL = sVersion + "coupon/get-coupon-detail";

        /**
         * 获取场馆列表
         */
        public static final String GET_COUPON_GYM = sVersion + "coupon/get-coupon-gym";

        /**
         * 检测更新APP
         */
        public static final String USER_CHECK_UPDATES = sVersion + "check-update/check-update";

        /**
         * 首页banner
         */
        public static final String HOME_BANNER = sVersion + "index/banner";

        /**
         * 首页
         */
        public static final String HOME_INDEX = sVersion + "index/index";

        /**
         * 购卡确认
         */
        public static final String CARD_CONFIRM = sVersion + "card/confirm";

        /**
         * 买卡提交确认
         */
        public static final String CARD_SUBMIT_CARD = sVersion + "order/submit-card";

        /**
         * 获取城市列表
         */
        public static final String CITY_LIST = sVersion +  "gym/open-city";

        /**
         * 场馆详情
         */
        public static final String GYM_INFO = sVersion +  "gym/info";

        /**
         * 获取体测历史
         */
        public static final String USER_GET_BODY_LIST = sVersion + "user/get-body-list";

        /**
         * 获取历史页面顶部导航
         */
        public static final String USER_BODY_MODULES_HISTORY = sVersion + "user/body-modules-history";


        /**
         * 获取体测数据单个字段历史值
         */
        public static final String USER_BODY_COLUMN_HISTORY = sVersion + "user/body-column-history";

//        /**同步时间戳*/
//        public static final String SYNC_SERVER_TIMESTAMP = "time/timestamp";
//        /**获取首页课程列表*/
//        public static final String GET_COURSE_CATEGORY_LIST = sVersion + "course/course";
//        /**获取课表*/
//        public static final String COURSE_COURSE_SCHEDULE = sVersion + "course/course-schedule";
//        /**课程详情*/
//        public static final String COURSE_DETAILS = sVersion +  "course/course-detail";
//        /**检测更新*/
//        public static final String APP_UPDATE = "config/config";
//        /**获取用户信息*/
//        public static final String GET_USER_INFO = sVersion +  "user/info";
    }
}
