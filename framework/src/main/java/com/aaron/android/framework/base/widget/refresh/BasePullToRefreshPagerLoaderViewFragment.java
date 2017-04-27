package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;

/**
 * Created on 15/7/27.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class BasePullToRefreshPagerLoaderViewFragment<T extends PullToRefreshBase> extends BasePagerLoaderFragment {
    private T mRefreshView;

    protected abstract T createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    protected void setupRefreshLayout(View contentView, LayoutInflater inflater) {
        if (contentView == null) {
            throw new IllegalArgumentException("refresh view content must not be null!");
        }
        if (mRefreshView != null) {
            getStateView().removeView(mRefreshView);
        }
        mRefreshView = (T) contentView;
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRefreshViewPull(true);
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadHomePage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRefreshViewPull(true);
                loadNextPage();
            }
        });
        getStateView().setSuccessView(mRefreshView);
    }

    @Override
    protected void updateRefreshViewState(boolean refresh) {
        if (!refresh) {
            mRefreshView.onRefreshComplete();
        }
    }

    /**
     * 设置上拉下拉属性
     *
     * @param pullMode 上拉下拉类型
     */
    @Override
    public void setPullMode(PullMode pullMode) {
        super.setPullMode(pullMode);
        PullToRefreshBase.Mode mode = null;
        switch (pullMode) {
            case PULL_BOTH:
                mode = PullToRefreshBase.Mode.BOTH;
                break;
            case PULL_DOWN:
                mode = PullToRefreshBase.Mode.PULL_FROM_START;
                break;
            case PULL_UP:
                mode = PullToRefreshBase.Mode.PULL_FROM_END;
                break;
            case PULL_NONE:
                mode = PullToRefreshBase.Mode.DISABLED;
                break;
            default:
                break;
        }
        if (mRefreshView != null) {
            mRefreshView.setMode(mode);
        }
    }
}