package com.goodchef.liking.data.remote.retrofit;

import com.goodchef.liking.data.remote.retrofit.result.BannerResult;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyHistoryResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyModelNavigationResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyTestResult;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.retrofit.result.ChargeGroupConfirmResult;
import com.goodchef.liking.data.remote.retrofit.result.CheckGymListResult;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.data.remote.retrofit.result.CityListResult;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsCities;
import com.goodchef.liking.data.remote.retrofit.result.CouponsDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsPersonResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.DelBodyRecordResult;
import com.goodchef.liking.data.remote.retrofit.result.GroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.GymDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.MessageResult;
import com.goodchef.liking.data.remote.retrofit.result.MyCardResult;
import com.goodchef.liking.data.remote.retrofit.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.MyGroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.MyOrderCardDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.MyPrivateCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.OrderCalculateResult;
import com.goodchef.liking.data.remote.retrofit.result.OrderCardListResult;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.SelfGroupCoursesListResult;
import com.goodchef.liking.data.remote.retrofit.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.SportDataResult;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportWeekResult;
import com.goodchef.liking.data.remote.retrofit.result.SubmitPayResult;
import com.goodchef.liking.data.remote.retrofit.result.SyncTimestampResult;
import com.goodchef.liking.data.remote.retrofit.result.UnreadMessageResult;
import com.goodchef.liking.data.remote.retrofit.result.UserAuthCodeResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.retrofit.result.UserImageResult;
import com.goodchef.liking.data.remote.retrofit.result.UserInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.UserLoginResult;
import com.goodchef.liking.data.remote.retrofit.result.VerificationCodeResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterOrderResult;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;
import com.goodchef.liking.data.remote.retrofit.result.QRCodeResult;
import com.goodchef.liking.module.smartspot.SmartspotDetailResult;

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
    String BODY_ID = "body_id";

    String GYM_ID = "gym_id";
    String WATER_ID = "water_id";
    String PAY_TYPE = "pay_type";


    @FormUrlEncoded
    @POST(Urls.USER_LOGIN)
    Observable<UserLoginResult> userLogin(@Path(PATH_VERSION) String version,
                                          @Field("phone") String phone,
                                          @Field("captcha") String captcha);

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
    @FormUrlEncoded
    Observable<CardResult> getCardList(@Path(PATH_VERSION) String version,
                                       @FieldMap Map<String, String> map);

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

    @FormUrlEncoded
    @POST(Urls.CARD_SUBMIT_CARD)
    Observable<SubmitPayResult> submitBuyCardData(@Path(PATH_VERSION) String version,
                                                  @FieldMap Map<String, String> map);

    @POST(Urls.CITY_LIST)
    Observable<CityListResult> getCityList(@Path(PATH_VERSION) String version);

    @POST(Urls.GYM_INFO)
    Observable<GymDetailsResult> getGymDetails(@Path(PATH_VERSION) String version,
                                               @Query("gym_id") String gymId);

    @POST(Urls.USER_GET_BODY_LIST)
    Observable<BodyHistoryResult> getHistoryData(@Path(PATH_VERSION) String version,
                                                 @Query(KEY_TOKEN) String token,
                                                 @Query("page") int page);

    @POST(Urls.USER_BODY_MODULES_HISTORY)
    Observable<BodyModelNavigationResult> getBodyHistoryTitleList(@Path(PATH_VERSION) String version,
                                                                  @Query(KEY_TOKEN) String token,
                                                                  @Query("modules") String modules);

    @POST(Urls.USER_BODY_COLUMN_HISTORY)
    Observable<BodyAnalyzeHistoryResult> getBodyHistoryList(@Path(PATH_VERSION) String version,
                                                            @Query(KEY_TOKEN) String token,
                                                            @Query("column") String column);

    @FormUrlEncoded
    @POST(Urls.USER_GET_USER_INFO)
    Observable<MyUserOtherInfoResult> getMyUserInfoData(@Path(PATH_VERSION) String version,
                                                        @Field(KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(Urls.USER_GET_USER_BODY)
    Observable<BodyTestResult> getBodyTestData(@Path(PATH_VERSION) String version,
                                               @Field(KEY_TOKEN) String token,
                                               @Field(BODY_ID) String bodyid);

    @FormUrlEncoded
    @POST(Urls.USER_SAVE_SPORT_DATA)
    Observable<LikingResult> sendSportData(@Path(PATH_VERSION) String version,
                                           @Field(KEY_TOKEN) String token,
                                           @Field("sport_data") String sportData,
                                           @Field("device_id") String deviceId);

    @FormUrlEncoded
    @POST(Urls.USER_SPORT_LIST)
    Observable<SportDataResult> getSportData(@Path(PATH_VERSION) String version,
                                             @Field(KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(Urls.USER_DEVICE)
    Observable<LikingResult> uploadUserDevice(@Path(PATH_VERSION) String version,
                                              @FieldMap Map<String, String> map);

    @POST(Urls.SYNC_SERVER_TIMESTAMP)
    Observable<SyncTimestampResult> syncServerTimestamp();

    @POST(Urls.BASE_CONFIG)
    Observable<BaseConfigResult> baseConfig();

    @FormUrlEncoded
    @POST(Urls.TRAINER_SHARE)
    Observable<ShareResult> getPrivateCoursesShare(@Path(PATH_VERSION) String version,
                                                   @Field("trainer_id") String trainerId);

    @FormUrlEncoded
    @POST(Urls.USER_TEAM_SHARE)
    Observable<ShareResult> getGroupCoursesShare(@Path(PATH_VERSION) String version,
                                                 @Field("schedule_id") String scheduleId);

    @FormUrlEncoded
    @POST(Urls.USER_EXERCISE_SHARE)
    Observable<ShareResult> getUserShare(@Path(PATH_VERSION) String version,
                                         @Field(KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(Urls.USER_BIND_DEVICE)
    Observable<LikingResult> bindDevices(@Path(PATH_VERSION) String version,
                                         @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Urls.USER_UNBIND)
    Observable<LikingResult> unBindDevices(@Path(PATH_VERSION) String version,
                                           @Field(KEY_TOKEN) String token,
                                           @Field("device_id") String devicesId);


    @FormUrlEncoded
    @POST(Urls.UPLOAD_ERROR)
    Observable<LikingResult> uploadNetworkError(@Path(PATH_VERSION) String version,
                                                @Field("url") String url,
                                                @Field("error_msg") String errorMsg);

    @FormUrlEncoded
    @POST(Urls.GET_WATER_ALL)
    Observable<WaterRateResult> getWaterRateResult(@Path(PATH_VERSION) String sHostVersion,
                                                   @Field(KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(Urls.WATER_ORDER_SUBMIT)
    Observable<WaterOrderResult> buyWaterRate(@Path(PATH_VERSION) String sHostVersion,
                                              @Field(KEY_TOKEN) String token,
                                              @Field(GYM_ID) String gymId,
                                              @Field(WATER_ID) String waterId,
                                              @Field(PAY_TYPE) String payType);

    @FormUrlEncoded
    @POST(Urls.DEL_BODY_RECORD)
    Observable<DelBodyRecordResult> delBodyRecord(@Path(PATH_VERSION) String sHostVersion,
                                                  @Field(KEY_TOKEN) String token,
                                                  @Field("body_id") int id);

    @FormUrlEncoded
    @POST(Urls.WATER_DETAILS)
    Observable<WaterDetailsResult> getWaterDetailsResult(@Path(PATH_VERSION) String sHostVersion,
                                                         @Field(KEY_TOKEN) String token,
                                                         @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST(Urls.USER_GET_MY_MSG)
    Observable<MessageResult> getMessageList(@Path(PATH_VERSION) String sHostVersion,
                                             @Field(KEY_TOKEN) String token,
                                             @Field("page") int page);

    @FormUrlEncoded
    @POST(Urls.USER_MSG_READ_SET)
    Observable<LikingResult> setMessageRead(@Path(PATH_VERSION) String sHostVersion,
                                            @Field(KEY_TOKEN) String token,
                                            @Field("msg_id") String msgId);

    @FormUrlEncoded
    @POST(Urls.USER_CHECK_UNREAD_MSG)
    Observable<UnreadMessageResult> getHasReadMessage(@Path(PATH_VERSION) String sHostVersion, @Field(KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST(Urls.USER_CHECK_UNREAD_MSG)
    Observable<UnreadMessageResult> getHasReadMessage2(@Path(PATH_VERSION) String sHostVersion);

    @FormUrlEncoded
    @POST(Urls.GET_SMARTSPOT_DETAIL)
    Observable<SmartspotDetailResult> getSmartspotDetail(@Path(PATH_VERSION) String sHostVersion,
                                                         @Field(KEY_TOKEN) String token,
                                                         @Field("record_id") String recordID);

    @FormUrlEncoded
    @POST(Urls.AUTH_QRCODE)
    Observable<QRCodeResult> authQRCode(@Path(PATH_VERSION) String sHostVersion,
                                        @Field(KEY_TOKEN) String token,
                                        @Field("code") String code);

    @FormUrlEncoded
    @POST(Urls.SPORT_LIST)
    Observable<SportListResult> getSportListResult(@Path(PATH_VERSION) String sHostVersion,
                                                   @Field(KEY_TOKEN) String token,
                                                   @Field("page") String page,
                                                   @Field("date") String date);

    @FormUrlEncoded
    @POST(Urls.SPORT_STATS)
    Observable<SportWeekResult> getSportStatsData(@Path(PATH_VERSION) String sHostVersion,
                                                  @Field(KEY_TOKEN) String token);

    class Urls {
        private static final String sVersion = "/{version}/";

        /**
         * 基础配置
         */
        static final String BASE_CONFIG = "config/config";
        /**
         * 同步时间戳
         */
        public static final String SYNC_SERVER_TIMESTAMP = "time/timestamp/";

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
        public static final String CITY_LIST = sVersion + "gym/open-city";

        /**
         * 我的页面获取我的个人信息其他数据
         */
        public static final String USER_GET_USER_INFO = sVersion + "user/get-user-info";

        /**
         * 场馆详情
         */
        public static final String GYM_INFO = sVersion + "gym/info";

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

        /**
         * 获取体测数据
         */
        public static final String USER_GET_USER_BODY = sVersion + "user/get-user-body";

        /**
         * 上报运动数据
         */
        public static final String USER_SAVE_SPORT_DATA = sVersion + "user/save-sport-data";

        /**
         * 获取运动数据
         */
        public static final String USER_SPORT_LIST = sVersion + "user/sport-list";

        /**
         * 上报手机信息
         */
        public static final String USER_DEVICE = sVersion + "user/device";

        /**
         * 私教课分享
         */
        public static final String TRAINER_SHARE = sVersion + "user/trainer-share";

        /**
         * 团体课分享
         */
        public static final String USER_TEAM_SHARE = sVersion + "user/team-share";

        /**
         * 运动数据分享
         */
        public static final String USER_EXERCISE_SHARE = sVersion + "user/exercise-share";

        /**
         * 绑定手环
         */
        public static final String USER_BIND_DEVICE = sVersion + "user/bind-device";

        /**
         * 解绑手环
         */
        public static final String USER_UNBIND = sVersion + "user/unbind";

        /**
         * 上报接口错误信息
         */
        public static final String UPLOAD_ERROR = sVersion + "index/err-log";

        /**
         * 获取水费
         */
        public static final String GET_WATER_ALL = sVersion + "water/get-water-all";

        /**
         * 水费支付订单
         */
        public static final String WATER_ORDER_SUBMIT = sVersion + "water/order-submit";

        /**
         * 删除体侧记录
         */
        public static final String DEL_BODY_RECORD = sVersion + "user/del-body-record";

        /**
         * 水费详情
         */
        public static final String WATER_DETAILS = sVersion + "water/order-water-detail";

        /**
         * 获取消息列表
         */
        public static final String USER_GET_MY_MSG = sVersion + "user/get-my-msg";

        /**
         * 设置消息是否已读
         */
        public static final String USER_MSG_READ_SET = sVersion + "user/msg-read-set";

        /**
         * 获取消息数
         */
        public static final String USER_CHECK_UNREAD_MSG = sVersion + "user/check-unread-msg";

        /**
         * 获取smartspot详情
         */
        public static final String GET_SMARTSPOT_DETAIL = sVersion + "sport/info";

        /**
         * 二维码确认
         */
        public static final String AUTH_QRCODE = sVersion + "code/auth";
        /**
         * 运动列表
         */
        public static final String SPORT_LIST = sVersion + "sport/list";

        /**
         * 运动状态
         */
        public static final String SPORT_STATS = sVersion + "sport/stats";
     }
}
