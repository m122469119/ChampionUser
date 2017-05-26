package com.goodchef.liking.data.remote.rxobserver;

import android.content.Context;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

/**
 * Created on 17/5/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class PagerLoadingObserver<T extends LikingResult> extends LikingBaseObserver<T> {

    protected PagerLoadingObserver(Context context, BaseView view) {
        super(context, view);
    }

    @Override
    public void onNext(T result) {
        if (getView() instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) getView()).requestSuccess();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (getView() instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) getView()).requestFailure();
        }
        super.onError(e);
    }

}
