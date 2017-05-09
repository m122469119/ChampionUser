package com.goodchef.liking.module.data.remote;

import com.alipay.tscenter.biz.rpc.vkeydfp.result.BaseResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.CouponsPersonResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyCardResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
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
    Observable<CouponsResult> getCoupons(@Path(PATH_VERSION) String version, @QueryMap Map<String,String> map);

    @FormUrlEncoded
    @POST(Urls.MY_ORDER_COURSES_LIST)
    Observable<MyGroupCoursesResult> getTeamCourseList(@Path(PATH_VERSION) String version,
                                                       @Field(KEY_TOKEN) String token,
                                                       @Field("page") int page);

    @FormUrlEncoded
    @POST(Urls.CANCEL_GROUP_COURSES)
    Observable<LikingResult> cancelTeamCourse(@Path(PATH_VERSION) String version,
                                              @Field(KEY_TOKEN) String token,
                                              @Field("order_id")String orderId);

    @FormUrlEncoded
    @POST(Urls.MY_ORDER_PRIVATE_LIST)
    Observable<MyPrivateCoursesResult> getPersonalCourseList(@Path(PATH_VERSION) String version,
                                                             @Field(KEY_TOKEN) String token,
                                                             @Field("page")int page);

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
