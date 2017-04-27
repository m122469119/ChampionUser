package com.aaron.android.framework.base.widget.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.aaron.android.codelibrary.http.result.DataListExtraResult;
import com.aaron.android.codelibrary.http.result.ExtraData;
import com.aaron.android.framework.R;
import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshListView;
import com.aaron.common.utils.ListUtils;

import java.util.List;

/**
 * Created on 15/7/27.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class NetworkPullToRefreshPagerLoaderListViewFragment extends BasePullToRefreshPagerLoaderViewFragment<PullToRefreshListView> {

    protected PullToRefreshListView mListView;
    private HBaseAdapter mListAdapter;
    private AdapterView.OnItemClickListener mItemClickListener;

    /**
     * 如果需要使用自定义的ListView，可以重写此方法
     *
     * @return ListView
     */
    protected PullToRefreshListView createListView() {
        return null;
    }

    @Override
    protected PullToRefreshListView createContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = createListView();
        if (mListView == null) {
            getDefaultListView(inflater);
        }
        return mListView;
    }

    private void getDefaultListView(LayoutInflater inflater) {
        mListView = (PullToRefreshListView) inflater.inflate(R.layout.listview_page_loader, null, false);
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
                    mListAdapter.setData(result.getDataList());
                    ExtraData extraData = result.getExtraData();
                    if (extraData != null) {
                        int pageTotal = extraData.getPageTotal();
                        if (pageTotal >= 1) {
                            setTotalPage(pageTotal);
                        }
                    }
                } else {
                    mListAdapter.addData(result.getDataList());
                }
                mListAdapter.notifyDataSetChanged();
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
        if (mListAdapter != null) {
            if (ListUtils.isEmpty(listData)) {
                if (isRequestHomePage()) {
                    clearListViewContent();
                    getStateView().setState(StateView.State.NO_DATA);
                } else {
                    showToast("没有更多数据了");
                }
            } else {
                getStateView().setState(StateView.State.SUCCESS);
                if (isRequestHomePage()) {
                    mListAdapter.setData(listData);
                } else {
                    mListAdapter.addData(listData);
                }
                mListAdapter.notifyDataSetChanged();
            }
        }
    }


    protected void clearListViewContent() {
        if (mListAdapter != null) {
            mListAdapter.setData(null);
            mListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * @return 获取ListView适配器
     */
    public HBaseAdapter getListAdapter() {
        return mListAdapter;
    }

    /**
     * 设置ListView适配器Adapter
     *
     * @param adapter BaseAdapter
     */
    public void setListAdapter(HBaseAdapter adapter) {
        mListAdapter = adapter;
        mListView.setAdapter(mListAdapter);
    }

    public void setListItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        mListView.setOnItemClickListener(mItemClickListener);
    }

    @Override
    public void onDestroy() {
        if (mListView != null && mListView.getRefreshableView().getOnItemClickListener() != null) {
            setListItemClickListener(null);
            mItemClickListener = null;
        }
        super.onDestroy();
    }

}
