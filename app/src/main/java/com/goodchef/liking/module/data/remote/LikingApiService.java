package com.goodchef.liking.module.data.remote;

import com.aaron.android.framework.library.http.retrofit.ServiceGenerator;
import com.goodchef.liking.http.api.LikingCommonInterceptor;
import com.goodchef.liking.http.result.UserLoginResult;

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

    @POST(LikingApiService.Urls.USER_LOGIN)
    Observable<UserLoginResult> userLogin(@Path(PATH_VERSION) String version,
                                          @Query("phone") String phone,
                                          @Query("captcha") String captcha);

    class Creator {
        public static LikingApiService getInstance() {
            return LikingNewApiHolder.sLikingApiService;
        }
        static class LikingNewApiHolder {
            private static LikingApiService sLikingApiService = ServiceGenerator.createService(LikingApiService.class, new LikingCommonInterceptor());
        }
    }

    class Urls {
        private static final String sVersion = "{version}/";
        /**基础配置*/
        public static final String USER_LOGIN = sVersion + "user/login";
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
