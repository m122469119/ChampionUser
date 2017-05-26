package com.goodchef.liking.module.body;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyHistoryResult;
import com.goodchef.liking.data.remote.retrofit.result.BodyModelNavigationResult;

import io.reactivex.Observable;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class BodyModel extends BaseModel {


    /**
     *获取体测历史
     * @param page
     */
    public Observable<BodyHistoryResult> getHistoryData(int page) {

        return LikingNewApi.getInstance().getHistoryData(LikingNewApi.sHostVersion, page)
                .compose(RxUtils.<BodyHistoryResult>applyHttpSchedulers());
    }

    /**
     * 获取体测历史页面顶部导航
     *
     * @param modules  模块名称
     */
    public Observable<BodyModelNavigationResult> getBodyHistoryTitleList(String modules) {

        return LikingNewApi.getInstance().getBodyHistoryTitleList(LikingNewApi.sHostVersion, LikingPreference.getToken(),modules)
                .compose(RxUtils.<BodyModelNavigationResult>applyHttpSchedulers());
    }

    /**
     * 获取体测历史下面数据
     *
     * @param column   导航栏目
     */
    public Observable<BodyAnalyzeHistoryResult> getBodyHistoryList(String column) {

        return LikingNewApi.getInstance().getBodyHistoryList(LikingNewApi.sHostVersion, LikingPreference.getToken(),column)
                .compose(RxUtils.<BodyAnalyzeHistoryResult>applyHttpSchedulers());
    }
}
