package com.aaron.android.framework.base.widget.refresh;


import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.http.code.result.Result;

/**
 * Created on 16/8/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class PagerRequestCallback<T extends Result> extends RequestCallback<T> {
    private BasePagerLoaderFragment mPagerLoaderFragment;

    public PagerRequestCallback(BasePagerLoaderFragment pagerLoaderFragment) {
        mPagerLoaderFragment = pagerLoaderFragment;
    }

    @Override
    public void onSuccess(T result) {
        if (mPagerLoaderFragment != null && mPagerLoaderFragment.getActivity() != null) {
            mPagerLoaderFragment.requestSuccess();
        }
    }

    @Override
    public void onFailure(RequestError error) {
        if (mPagerLoaderFragment != null && mPagerLoaderFragment.getActivity() != null) {
            mPagerLoaderFragment.requestFailure();
        }
    }
}
