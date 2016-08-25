package com.aaron.android.framework.base.widget.refresh;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;

/**
 * Created on 16/8/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class PagerRequestCallback<T extends BaseResult> extends RequestCallback<T> {
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
