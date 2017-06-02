package com.goodchef.liking.module.body.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BodyTestHistoryAdapter;
import com.goodchef.liking.data.remote.retrofit.result.BodyHistoryResult;
import com.goodchef.liking.module.bodytest.BodyTestDataActivity;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:12
 * version 1.0.0
 */

public class BodyTestHistoryFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment<BodyTestHistoryContract.Presenter> implements BodyTestHistoryContract.View {

    private BodyTestHistoryAdapter mBodyTestHistoryAdapter;

    public static BodyTestHistoryFragment newInstance() {
        Bundle args = new Bundle();
        BodyTestHistoryFragment bodyTestHistoryFragment = new BodyTestHistoryFragment();
        bodyTestHistoryFragment.setArguments(args);
        return bodyTestHistoryFragment;
    }

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    private void sendRequest(int page) {
        mPresenter.getHistoryData(page);
    }

    @Override
    protected void initViews() {
        setNoDataView();
        getRecyclerView().setBackgroundColor(ResourceUtils.getColor(R.color.app_content_background));
        setPullMode(PullMode.PULL_BOTH);
        if (mBodyTestHistoryAdapter == null) {
            mBodyTestHistoryAdapter = new BodyTestHistoryAdapter(getActivity());
        }
        setRecyclerAdapter(mBodyTestHistoryAdapter);
        mBodyTestHistoryAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(android.view.View view, int position) {
                BodyHistoryResult.BodyHistoryData.ListData object = mBodyTestHistoryAdapter.getDataList().get(position);
                if (object != null) {
                    String bodyId = object.getBodyId();
                    Intent intent = new Intent(getActivity(), BodyTestDataActivity.class);
                    intent.putExtra(BodyTestDataActivity.BODY_ID, bodyId);
                    intent.putExtra(BodyTestDataActivity.SOURCE, "history");
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(android.view.View view, int position) {
                return false;
            }
        });
    }

    private void setNoDataView() {
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_data);
        noDataText.setText(R.string.no_body_history_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private android.view.View.OnClickListener refreshOnClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            loadHomePage();
        }
    };

    @Override
    public void updateBodyHistoryView(BodyHistoryResult.BodyHistoryData data) {
        getStateView().setState(StateView.State.SUCCESS);
        if (data != null) {
            updateListView(data.getList());
        }
    }

    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setPresenter() {
        mPresenter = new BodyTestHistoryContract.Presenter();
    }
}
