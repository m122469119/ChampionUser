package com.goodchef.liking.module.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.SportDataAdapter;
import com.goodchef.liking.adapter.SportHistogramAdapter;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.data.remote.retrofit.result.SportStatsResult;
import com.goodchef.liking.module.smartspot.SmartSpotDetailActivity;

import java.util.List;

/**
 * 我的训练数据记录 - day
 */
public class SportDataDayFragment extends BaseMVPFragment<SportDataContract.Presenter> implements SportDataContract.View {

    @BindView(R.id.recyclerview_sport)
    RecyclerView mRecyclerView;
    SportDataAdapter mAdapter;

    @BindView(R.id.state_view)
    StateView mStateView;

    RecyclerView mSportRecyclerView;
    private SportHistogramAdapter mHistogramAdapter = null;

    private View mFooterView;
    private boolean isLoadMore = false;

    public static SportDataDayFragment newInstance() {
        SportDataDayFragment fragment = new SportDataDayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.activity_sport_contains, container, false);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        mAdapter = new SportDataAdapter(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View headView = inflater.inflate(R.layout.layout_sport_data_header, null);
        mSportRecyclerView = (RecyclerView) headView.findViewById(R.id.recyclerView_sport);
        initHeadView();
        mFooterView = inflater.inflate(R.layout.item_sport_data_empty, null);
        mAdapter.setHeaderView(headView);
        mAdapter.setFooterView(mFooterView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SportListResult.DataBean.ListBean>() {

            @Override
            public void onItemClick(int position, SportListResult.DataBean.ListBean data) {
                if (data.getType() == SportListResult.DataBean.ListBean.TYPE_SMARTSPOT) {
                    SmartSpotDetailActivity.launch(getContext(), String.valueOf(data.getRecordId()));
                }
            }
        });
    }

    private void initHeadView() {
        bindSportRecyclerLayout();
        requestData();
    }

    public void requestData() {
        mPresenter.getSportStats();
    }

    private void bindSportRecyclerLayout() {
        mHistogramAdapter = new SportHistogramAdapter(getContext());
        mSportRecyclerView.setAdapter(mHistogramAdapter);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mSportRecyclerView.setLayoutManager(manager);
        mSportRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.i(TAG, "-----------onScrollStateChanged-----------");
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollHorizontally(-1)) {
                    mPresenter.getSportStats();
                    isLoadMore = true;
                    PopupUtils.showToast(getContext(), "加载更多...");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.e(TAG, "onScrolled: DX:" + dx + ";DY:" + dy);
                LogUtils.e(TAG, "CHECK_SCROLL_LEFT:" + recyclerView.canScrollHorizontally(-1));
                LogUtils.e(TAG, "CHECK_SCROLL_RIGHT:" + recyclerView.canScrollHorizontally(1));
            }
        });
        mHistogramAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                int currPoi = mHistogramAdapter.getSelectCurrPosition();
                if (currPoi < mHistogramAdapter.getDatas().size()) {
                    mHistogramAdapter.getDatas().get(currPoi).setChecked(false);
                }
                mHistogramAdapter.getDatas().get(position).setChecked(true);
                mHistogramAdapter.setSelectCurrPosition(position);
                mPresenter.getSportList(position);
            }
        });
    }

    private void setSportRecyclerView() {
        mHistogramAdapter.setDatas(mPresenter.getSportDatas(), isLoadMore);
        mHistogramAdapter.notifyDataSetChanged();
        if (mHistogramAdapter.getSelectCurrPosition() != -1 && !isLoadMore) {
            mPresenter.getSportList(mHistogramAdapter.getSelectCurrPosition());
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new SportDataContract.Presenter(SportDataContract.Presenter.TYPE_TIME_DAY);
    }

    @Override
    public void updateSportStatsView(SportStatsResult value) {
        if (value != null) {
            mStateView.setState(StateView.State.SUCCESS);
            setSportRecyclerView();
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateSportListView(SportListResult value) {
        List<SportListResult.DataBean.ListBean> data = value.getData().getList();
        mAdapter.setDatas(data);
        mAdapter.notifyDataSetChanged();

        ViewGroup.LayoutParams params = mFooterView.getLayoutParams();
        if (null == data || data.size() == 0) {
            mFooterView.setVisibility(View.VISIBLE);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            mFooterView.setVisibility(View.GONE);
            params.height = 0;
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}
