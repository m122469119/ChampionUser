package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.http.result.DataListExtraResult;
import com.aaron.android.codelibrary.http.result.ExtraData;
import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.utils.PopupUtils;

import java.util.List;

/**
 * Created on 16/6/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class NetworkPagerLoaderRecyclerViewFragement extends BasePagerLoaderViewFragment<PullToRefreshRecyclerView>{
    private PullToRefreshRecyclerView mRecyclerView;
    private BaseRecycleViewAdapter mRecyclerViewAdapter;
    protected PullToRefreshRecyclerView createRecyclerView() {
        return null;
    }
    @Override
    protected PullToRefreshRecyclerView createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = createRecyclerView();
        if (mRecyclerView == null) {
            getDefaultRecyclerView(inflater);
        }
        return mRecyclerView;
    }

    private void getDefaultRecyclerView(LayoutInflater inflater) {
        mRecyclerView = (PullToRefreshRecyclerView) inflater.inflate(R.layout.recycler_page_loader, null, false);
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
                    PopupUtils.showToast("没有更多数据了");
                }
            } else {
                getStateView().setState(StateView.State.SUCCESS);
                if (isRequestHomePage()) {
                    mRecyclerViewAdapter.setData(listData);
                } else {
                    mRecyclerViewAdapter.addData(listData);
                }
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


        /**
         * @return 获取ListView适配器
         */
    public RecyclerView.Adapter getListAdapter() {
        return mRecyclerViewAdapter;
    }

    /**
     * 设置ListView适配器Adapter
     *
     * @param adapter BaseAdapter
     */
    public void setListAdapter(BaseRecycleViewAdapter adapter) {
        mRecyclerViewAdapter = adapter;
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

}
