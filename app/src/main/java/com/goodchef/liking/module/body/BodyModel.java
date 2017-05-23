package com.goodchef.liking.module.body;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.remote.RxUtils;

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

}
