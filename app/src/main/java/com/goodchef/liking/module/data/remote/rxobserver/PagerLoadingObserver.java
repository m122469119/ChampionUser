package com.goodchef.liking.module.data.remote.rxobserver;

import android.content.Context;

import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.goodchef.liking.http.result.LikingResult;

/**
 * Created on 17/5/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public abstract class PagerLoadingObserver<T extends LikingResult> extends LikingBaseObserver<T> {

    private BaseView mView;

    protected PagerLoadingObserver(Context context, BaseView view) {
        super(context, view);
        mView = view;
    }
    @Override
    public void onNext(T result) {
        if (mView instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) mView).requestSuccess();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView instanceof BasePagerLoaderFragment) {
            ((BasePagerLoaderFragment) mView).requestFailure();
        }
        super.onError(e);
    }

}
