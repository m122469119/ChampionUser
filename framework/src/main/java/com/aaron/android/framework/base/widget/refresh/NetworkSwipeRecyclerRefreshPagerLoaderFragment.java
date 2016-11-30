package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.http.result.DataListExtraResult;
import com.aaron.android.codelibrary.http.result.ExtraData;
import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;

import java.util.List;

/**
 * Created on 16/8/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class NetworkSwipeRecyclerRefreshPagerLoaderFragment extends BaseSwipeRefreshPagerLoaderFragment {
    private RecyclerView mRecyclerView;
    private BaseRecycleViewAdapter mRecyclerViewAdapter;
    private View mFooterView;

    @Override
    protected View createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = createRecyclerView();
        if (mRecyclerView == null) {
            getDefaultRecyclerView();
        }
        mFooterView = inflater.inflate(R.layout.layout_network_footer, mRecyclerView, false);
        return mRecyclerView;
    }

    protected RecyclerView createRecyclerView() {
        return null;
    }

    private void getDefaultRecyclerView() {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isSlideToBottom(recyclerView) && allowPullUp()) {
                    loadNextPage();
                }
            }
        });
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        return !ViewCompat.canScrollVertically(recyclerView, 1);
    }

    /**
     * 数据回调后，通知ListView更新
     *
     * @param result DataListExtraResult
     */
    public void updateListView(DataListExtraResult result) {
        if (result != null) {
            if (result.getDataList() != null) {
                if (isRequestHomePage()) {
                    mRecyclerViewAdapter.setData(result.getDataList());
                    ExtraData extraData = result.getExtraData();
                    if (extraData != null) {
                        int pageTotal = extraData.getPageTotal();
                        if (pageTotal >= 1) {
                            setTotalPage(pageTotal);
                        }
                    }
                } else {
                    mRecyclerViewAdapter.addData(result.getDataList());
                }
                mRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                clearListViewContent();
                if (isRequestHomePage()) {
                    getStateView().setState(StateView.State.NO_DATA);
                }
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
        mRecyclerView.setClipToPadding(false);
    }

    /**
     * 数据回调后，通知ListView更新
     *
     * @param listData DataListExtraResult
     */
    public void updateListView(List listData) {
        if (mRecyclerViewAdapter != null) {
            if (ListUtils.isEmpty(listData)) {
                if (isRequestHomePage()) {
                    clearListViewContent();
                    getStateView().setState(StateView.State.NO_DATA);
                } else {
                    setTotalPage(getCurrentPage());
                    mRecyclerViewAdapter.addFooterView(mFooterView);
                }
            } else {
                getStateView().setState(StateView.State.SUCCESS);
                if (isRequestHomePage()) {
                    mRecyclerViewAdapter.setData(listData);
                } else {
                    mRecyclerViewAdapter.addData(listData);
                }
                setTotalPage(getCurrentPage() + 1);
                mRecyclerViewAdapter.removeFooterView(mFooterView);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void clearListViewContent() {
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setData(null);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    protected void setNoNextPageFooterView(View view) {
        mFooterView = view;
    }


    /**
     * @return 获取ListView适配器
     */
    public RecyclerView.Adapter getRecyclerAdapter() {
        return mRecyclerViewAdapter;
    }

    /**
     * 设置ListView适配器Adapter
     *
     * @param adapter BaseAdapter
     */
    public void setRecyclerAdapter(BaseRecycleViewAdapter adapter) {
        mRecyclerViewAdapter = adapter;
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

}
