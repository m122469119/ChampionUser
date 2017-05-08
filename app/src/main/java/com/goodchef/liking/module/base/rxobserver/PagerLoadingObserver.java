package com.goodchef.liking.module.base.rxobserver;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.library.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.http.result.LikingResult;

/**
 * Created on 17/5/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class PagerLoadingObserver<T extends LikingResult> extends BaseRequestObserver<T> {

    BaseView mView;

    public PagerLoadingObserver(BaseView view) {
        mView = view;
    }
    @Override
    public void onNext(T result) {
        super.onNext(result);
        if (mView instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) mView).requestSuccess();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (mView instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) mView).requestFailure();
        }
    }
}
