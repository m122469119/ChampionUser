package com.goodchef.liking.module.body;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.http.result.BodyModelNavigationResult;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LikingNewApi;
import com.goodchef.liking.data.remote.RxUtils;

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

        return LikingNewApi.getInstance().getHistoryData(UrlList.sHostVersion, page)
                .compose(RxUtils.<BodyHistoryResult>applyHttpSchedulers());
    }

    /**
     * 获取体测历史页面顶部导航
     *
     * @param modules  模块名称
     */
    public Observable<BodyModelNavigationResult> getBodyHistoryTitleList(String modules) {

        return LikingNewApi.getInstance().getBodyHistoryTitleList(UrlList.sHostVersion, LikingPreference.getToken(),modules)
                .compose(RxUtils.<BodyModelNavigationResult>applyHttpSchedulers());
    }

    /**
     * 获取体测历史下面数据
     *
     * @param column   导航栏目
     */
    public Observable<BodyAnalyzeHistoryResult> getBodyHistoryList(String column) {

        return LikingNewApi.getInstance().getBodyHistoryList(UrlList.sHostVersion, LikingPreference.getToken(),column)
                .compose(RxUtils.<BodyAnalyzeHistoryResult>applyHttpSchedulers());
    }
}
