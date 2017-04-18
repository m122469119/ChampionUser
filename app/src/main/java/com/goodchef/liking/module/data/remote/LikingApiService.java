package com.goodchef.liking.module.data.remote;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.CheckUpdateAppResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @POST(Urls.USER_LOGIN)
    Observable<UserLoginResult> userLogin(@Path(PATH_VERSION) String version,
                                          @Query("phone") String phone,
                                          @Query("captcha") String captcha);

    @POST(Urls.GET_VERIFICATION_CODE)
    Observable<VerificationCodeResult> getVerificationCode(@Query(KEY_PHONE) String phone);

    @POST(Urls.CHECK_UPDATE)
    Observable<CheckUpdateAppResult> getCheckUpdateAppResult(@Path(PATH_VERSION) String version);

    @POST(Urls.USER_LOGOUT)
    Observable<BaseResult> userLogout(@Path(PATH_VERSION) String version,
                                      @Query(KEY_TOKEN) String token,
                                      @Query("registration_id") String registrationId);

    @POST(Urls.JOIN_APPLY)
    Observable<BaseResult> joinApply(@Path(PATH_VERSION) String version,
                                     @Query("name") String name,
                                     @Query("phone") String phone,
                                     @Query("city") String city,
                                     @Query("type") int type);

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
