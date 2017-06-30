package com.goodchef.liking.data.remote.rxobserver;

import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

/**
 * Created on 2017/6/30
 * Created by sanfen
 *
 * @version 1.0.0
 */

public abstract class StateViewLoadingObserver<T extends LikingResult> extends LikingBaseObserver<T> {


    public StateViewLoadingObserver(BaseStateView view) {
        super(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseStateView)getView()).changeStateView(StateView.State.LOADING);
    }


    @Override
    public void onNext(T value) {
        ((BaseStateView)getView()).changeStateView(StateView.State.SUCCESS);
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        ((BaseStateView)getView()).changeStateView(StateView.State.FAILED);
    }
}
