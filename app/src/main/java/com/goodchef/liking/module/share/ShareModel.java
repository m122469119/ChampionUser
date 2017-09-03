package com.goodchef.liking.module.share;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;

import io.reactivex.Observable;

/**
 * Created on 2017/05/24
 * desc: 分享
 *
 * @author: chenlei
 * @version:1.0
 */

public class ShareModel extends BaseModel {

    /***
     * 私教课分享
     *
     * @param trainerId 教练id
     */
    public Observable<ShareResult> getPrivateCoursesShare(String trainerId) {
        return LikingNewApi.getInstance().getPrivateCoursesShare(LikingNewApi.sHostVersion, trainerId)
                .compose(RxUtils.<ShareResult>applyHttpSchedulers());
    }

    /**
     * 团体课分享
     *
     * @param scheduleId 排期id
     */
    public Observable<ShareResult> getGroupCoursesShare(String scheduleId) {
        return LikingNewApi.getInstance().getGroupCoursesShare(LikingNewApi.sHostVersion, scheduleId)
                .compose(RxUtils.<ShareResult>applyHttpSchedulers());
    }

    /**
     * 我的运动数据分享
     */
    public Observable<ShareResult> getUserShare() {
        return LikingNewApi.getInstance().getUserShare(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<ShareResult>applyHttpSchedulers());
    }


    public Observable<ShareResult> getSportshare() {
        return LikingNewApi.getInstance()
                .getSportShare(LikingNewApi.sHostVersion, LikingPreference.getToken())
                .compose(RxUtils.<ShareResult>applyHttpSchedulers());
    }


    public Observable<ShareResult> getSmartSpotShare(String url, String exercise_name) {
        return LikingNewApi.getInstance().getSmartSpotShare(LikingNewApi.sHostVersion,
                LikingPreference.getToken(), url, exercise_name)
                .compose(RxUtils.<ShareResult>applyHttpSchedulers());
    }


}
