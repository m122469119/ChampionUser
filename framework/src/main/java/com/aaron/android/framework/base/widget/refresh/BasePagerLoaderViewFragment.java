package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;

/**
 * Created on 15/7/27.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class BasePagerLoaderViewFragment<T extends PullToRefreshBase> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private FrameLayout mRootView;
    private T mRefreshView;
    private StateView mStateView;
    private TextView failRefreshText;
    private PullMode mPullMode = PullMode.PULL_DOWN;
    private boolean mIsLoading = false;
    private Pager mPager = new Pager();
    private boolean mIsRefreshViewPull = false;

    protected abstract T createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void requestData(int page);

    protected abstract void initViews();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mPager.setTotal(Integer.MAX_VALUE);
            mRootView = (FrameLayout) inflater.inflate(R.layout.layout_pager_loader, null, false);
            initStatView();
            setRefreshView(createContentView(inflater, container, savedInstanceState));
            initViews();
            setPullType(mPullMode);
            mStateView.setState(StateView.State.LOADING);
            loadHomePage();
        }
        return mRootView;
    }

    private void initStatView() {
        mStateView = (StateView) mRootView.findViewById(R.id.state_view);
        failRefreshText = (TextView) mRootView.findViewById(R.id.text_view_fail_refresh);
        failRefreshText.setOnClickListener(mRefreshClickListener);
    }

    private final View.OnClickListener mRefreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mStateView.setState(StateView.State.LOADING);
            loadHomePage();
        }
    };

    public StateView getStateView() {
        return mStateView;
    }

    public TextView getFailRefreshText() {
        return failRefreshText;
    }

    protected void setOverlayView(View view) {
        if (view != null) {
            ViewGroup.LayoutParams overlayLayoutParams =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRootView.addView(view, overlayLayoutParams);
        }
    }

    private void setRefreshView(T view) {
        if (view == null) {
            throw new IllegalArgumentException("refresh view content must not be null!");
        }
        if (mRefreshView != null) {
            mStateView.removeView(mRefreshView);
        }
        mRefreshView = view;
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsRefreshViewPull = true;
                String label = DateUtils.formatDateTime(BaseApplication.getInstance().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                loadHomePage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsRefreshViewPull = true;
                loadNextPage();
            }
        });
        mStateView.setSuccessView(mRefreshView);
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    /**
     * @return 获取当前请求页
     */
    protected int getCurrentPage() {
        return mPager.getCurrent();
    }

    /**
     * @return 当前请求的是否是第一页数据
     */
    protected boolean isRequestHomePage() {
        return getCurrentPage() == Pager.HOME_PAGE;
    }


    /**
     * 请求page页数据
     *
     * @param page page
     */
    protected void loadPageData(int page) {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            LogUtils.i(TAG, "Page Loader error, network is not available!");
            if (page == mPager.getStart()) {
                mStateView.setState(StateView.State.FAILED);
            }
            requestFinished();
            return;
        }
        if (isLoading()) {
            return;
        }
        setLoading(true);
        if (isRequestHomePage() && !mIsRefreshViewPull) {
            mStateView.setState(StateView.State.LOADING);
        }
        mIsRefreshViewPull = false;
        mPager.setCurrent(page);
        requestData(page);
    }

    public enum PullMode {
        PULL_DOWN,
        PULL_UP,
        PULL_BOTH,
        PULL_NONE
    }

    /**
     * 设置上拉下拉属性
     *
     * @param pullMode 上拉下拉类型
     */
    public void setPullType(PullMode pullMode) {
        mPullMode = pullMode;
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

    /**
     * 如果分页数据加载失败，在请求错误回调中重置当前page值
     */
    private void requestFailure() {
        requestFinished();
        if (!isRequestHomePage()) {
            mPager.setCurrent(mPager.getCurrent() - 1);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    private void requestSuccess() {
        if (isRequestHomePage()) {
            mStateView.setState(StateView.State.SUCCESS);
        }
        requestFinished();
    }


    private void requestFinished() {
        mRefreshView.onRefreshComplete();
        setLoading(false);
    }

    /**
     * 请求第一页数据
     */
    protected void loadHomePage() {
        loadPageData(Pager.HOME_PAGE);
    }

    /**
     * 设置接口返回总页数
     *
     * @param totalPage totalPage
     */
    protected void setTotalPage(int totalPage) {
        mPager.setTotal(totalPage);
    }

    /**
     * 请求下一页数据
     */
    protected void loadNextPage() {
        if (mPager.hasNext()) {
            loadPageData(mPager.next());
        }
    }

    @Override
    public void onRefresh() {
        if (isLoading()) {
            return;
        }
        loadHomePage();
    }

    public static class PagerRequestCallback<T extends BaseResult> extends RequestCallback<T> {
        private BasePagerLoaderViewFragment mFragment;

        public PagerRequestCallback(BasePagerLoaderViewFragment fragment) {
            mFragment = fragment;
        }

        @Override
        public void onSuccess(T result) {
            if (mFragment != null && mFragment.getActivity() != null) {
                mFragment.requestSuccess();
            }
        }

        @Override
        public void onFailure(RequestError error) {
            if (mFragment != null && mFragment.getActivity() != null) {
                mFragment.requestFailure();
            }
        }
    }

}