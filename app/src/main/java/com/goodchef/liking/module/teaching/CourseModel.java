package com.goodchef.liking.module.teaching;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

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

        return LikingNewApi.getInstance().getTeamCourseList(UrlList.sHostVersion, Preference.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消团体课
     *
     * @param orderId
     */
    public Observable<LikingResult> sendCancelCoursesRequest(String orderId) {

        return LikingNewApi.getInstance().cancelTeamCourse(UrlList.sHostVersion, Preference.getToken(), orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 我的私教课列表
     *
     * @param page
     */
    public Observable<MyPrivateCoursesResult> getMyPrivateCourses(int page) {

        return LikingNewApi.getInstance().getPersonalCourseList(UrlList.sHostVersion, Preference.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
