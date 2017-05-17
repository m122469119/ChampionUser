package com.goodchef.liking.module.course;

import android.text.TextUtils;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.ChargeGroupConfirmResult;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.MyChargeGroupCoursesDetailsResult;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.http.result.MyPrivateCoursesDetailsResult;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.http.result.OrderCalculateResult;
import com.goodchef.liking.http.result.PrivateCoursesConfirmResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.http.result.SubmitPayResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.remote.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/05/09
 * desc: 课程相关(团体课、私教课)
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
                .getTeamCourseList(UrlList.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<MyGroupCoursesResult>applyHttpSchedulers());
    }

    /**
     * 取消团体课
     *
     * @param orderId
     */
    public Observable<LikingResult> sendCancelCoursesRequest(String orderId) {

        return LikingNewApi.getInstance()
                .cancelTeamCourse(UrlList.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /**
     * 我的私教课列表
     *
     * @param page
     */
    public Observable<MyPrivateCoursesResult> getMyPrivateCourses(int page) {

        return LikingNewApi.getInstance()
                .getPersonalCourseList(UrlList.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<MyPrivateCoursesResult>applyHttpSchedulers());
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
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance()
                .getCourseInfo(UrlList.sHostVersion, map)
                .compose(RxUtils.<GroupCoursesResult>applyHttpSchedulers());
    }

    /**
     * 团体课预约
     *
     * @param scheduleId 团体课排期id
     */

    public Observable<LikingResult> orderGroupCourses(String gymId, String scheduleId) {

        return LikingNewApi.getInstance()
                .teamCourseReserve(UrlList.sHostVersion, LikingPreference.getToken(), gymId, scheduleId)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /**
     * 获取我的付费团体课详情
     *
     * @param orderId
     */
    public Observable<MyChargeGroupCoursesDetailsResult> getChargeGroupCoursesDetails(String orderId) {

        return LikingNewApi.getInstance()
                .chargeGroupCoursesDetails(UrlList.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<MyChargeGroupCoursesDetailsResult>applyHttpSchedulers());
    }

    /***
     * 获取我的私教课详情
     *
     * @param orderId 订单id
     */
    public Observable<MyPrivateCoursesDetailsResult> getMyPrivateCoursesDetails(String orderId) {

        return LikingNewApi.getInstance()
                .getPersonalCourseDetails(UrlList.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<MyPrivateCoursesDetailsResult>applyHttpSchedulers());
    }

    /**
     * 完成我的私教课
     *
     * @param orderId 订单id
     */
    public Observable<LikingResult> completeMyPrivateCourses(String orderId) {

        return LikingNewApi.getInstance()
                .completeTrainerCourse(UrlList.sHostVersion, LikingPreference.getToken(), orderId)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /***
     * 获取自助团体课时间表
     *
     * @param gymId 场馆ID
     */
    public Observable<SelfHelpGroupCoursesResult> getSelfHelpScheduleInfo(String gymId) {
        Map<String, String> map = new HashMap<>();
        map.put("gym_id", gymId);
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance()
                .getSelfHelpScheduleInfo(UrlList.sHostVersion, map)
                .compose(RxUtils.<SelfHelpGroupCoursesResult>applyHttpSchedulers());
    }

    /***
     * 自助团体课 - 预约
     *
     * @param gymId       场馆id
     * @param roomId      操房id
     * @param coursesId   课程id
     * @param coursesDate 日期
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param price       价格
     * @param peopleNum   人数
     */
    public Observable<LikingResult> joinSelfHelpCourses(String gymId, String roomId, String coursesId, String coursesDate, String startTime, String endTime, String price, String peopleNum) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("gym_id", gymId);
        map.put("room_id", roomId);
        map.put("course_id", coursesId);
        map.put("course_date", coursesDate);
        map.put("start_time", startTime);
        map.put("end_time", endTime);
        map.put("price", price);
        map.put("people_num", peopleNum);
        return LikingNewApi.getInstance()
                .joinSelfHelpCourses(UrlList.sHostVersion, map)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /***
     * 获取选择自助团体课的列表
     *
     * @param page     页码
     */
    public Observable<SelfGroupCoursesListResult> getSelfCoursesList(int page) {
        return LikingNewApi.getInstance()
                .getScheduleCourseList(UrlList.sHostVersion, page)
                .compose(RxUtils.<SelfGroupCoursesListResult>applyHttpSchedulers());
    }

    /**
     * 团体课支付确认
     * @param gymId
     * @param scheduleId
     * @return
     */
    public Observable<ChargeGroupConfirmResult> chargeGroupCoursesConfirm(String gymId, String scheduleId) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("schedule_id", scheduleId);
        map.put("gym_id", gymId);
        return LikingNewApi.getInstance()
                .chargeGroupCoursesConfirm(UrlList.sHostVersion, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /***
     * 付费团体课提交订单获取支付数据
     *
     * @param scheduleId 排期id
     * @param couponCode 优惠券吗
     * @param payType    支付方式
     */
    public Observable<SubmitPayResult> chargeGroupCoursesImmediately(String gymId, String scheduleId, String couponCode, String payType) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("schedule_id", scheduleId);
        map.put("gym_id", gymId);
        map.put("coupon_code", couponCode);
        map.put("pay_type", payType);
        return LikingNewApi.getInstance()
                .chargeGroupCoursesImmediately(UrlList.sHostVersion, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 私教课确认预约订单
     *
     * @param trainerId 教练ID
     */
    public Observable<PrivateCoursesConfirmResult> orderPrivateCoursesConfirm(String gymId, String trainerId) {

        return LikingNewApi.getInstance()
                .orderPrivateCoursesConfirm(UrlList.sHostVersion, LikingPreference.getToken(), gymId, trainerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *  提交预约私教课
     * @param courseId
     * @param couponCode
     * @param payType
     * @param selectTimes
     * @param gymId
     */
    public Observable<SubmitPayResult> submitPrivateCourses(String courseId, String couponCode, String payType, int selectTimes, String gymId) {
        Map<String, String> map = new HashMap<>();
        map.put("token", LikingPreference.getToken());
        map.put("gym_id", gymId);
        map.put("course_id", courseId);
        map.put("coupon_code", couponCode);
        map.put("pay_type", payType);
        map.put("select_times", String.valueOf(selectTimes));

        return LikingNewApi.getInstance()
                .submitPrivateCourses(UrlList.sHostVersion, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 计算私教课金额
     * @param courseId
     * @param selectTimes
     * @return
     */
    public Observable<OrderCalculateResult> orderCalculate(String courseId, String selectTimes) {

        return LikingNewApi.getInstance()
                .orderCalculate(UrlList.sHostVersion, LikingPreference.getToken(), courseId, selectTimes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /***
     * 私教课详情
     *
     * @param trainerId 私教id
     */
    public Observable<PrivateCoursesResult> getPrivateCoursesDetails(String trainerId) {
        Map<String, String> map = new HashMap<>();
        map.put("trainer_id", trainerId);
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance()
                .getPrivateCoursesDetails(UrlList.sHostVersion, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
