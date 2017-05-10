package com.goodchef.liking.module.teaching;

import android.text.TextUtils;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/05/09
 * desc: 课程相关
 *
 * @author: chenlei
 * @version:1.0
 */

public class CourseModel {

    /**
     * 团体课列表
     *
     * @param page
     */
    public Observable<MyGroupCoursesResult> getMyGroupList(int page) {

        return LikingNewApi.getInstance()
                .getTeamCourseList(UrlList.sHostVersion, Preference.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消团体课
     *
     * @param orderId
     */
    public Observable<LikingResult> sendCancelCoursesRequest(String orderId) {

        return LikingNewApi.getInstance()
                .cancelTeamCourse(UrlList.sHostVersion, Preference.getToken(), orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 我的私教课列表
     *
     * @param page
     */
    public Observable<MyPrivateCoursesResult> getMyPrivateCourses(int page) {

        return LikingNewApi.getInstance()
                .getPersonalCourseList(UrlList.sHostVersion, Preference.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取团体课详情
     *
     * @param scheduleId 课程排序id
     * @return
     */
    public Observable<GroupCoursesResult> getGroupCoursesDetails(String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("schedule_id", scheduleId);
        String token = Preference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance()
                .getCourseInfo(UrlList.sHostVersion, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 团体课预约
     *
     * @param scheduleId 团体课排期id
     */

    public Observable<LikingResult> orderGroupCourses(String gymId, String scheduleId) {

        return LikingNewApi.getInstance()
                .teamCourseReserve(UrlList.sHostVersion, Preference.getToken(), gymId, scheduleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取我的付费团体课详情
     *
     * @param orderId
     */
    public Observable<MyChargeGroupCoursesDetailsResult> getChargeGroupCoursesDetails(String orderId) {

        return LikingNewApi.getInstance()
                .chargeGroupCoursesDetails(UrlList.sHostVersion, Preference.getToken(), orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
