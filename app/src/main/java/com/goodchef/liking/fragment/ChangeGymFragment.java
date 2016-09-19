package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.adapter.ChangeGymAdapter;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.mvp.presenter.CheckGymPresenter;
import com.goodchef.liking.mvp.view.CheckGymView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/18 下午3:30
 */
public class ChangeGymFragment extends BaseFragment implements CheckGymView {
    private LikingStateView mStateView;
    private PullToRefreshRecyclerView mRecyclerView;
    private RelativeLayout mNoCardLayout;
    private TextView mMyTextView;

    private ChangeGymAdapter mChangeGymAdapter;
    private CheckGymPresenter mCheckGymPresenter;

    private String selectCityName;//选择的城市名称
    private String selectCityId;//选择的城市id
    private boolean isLoaction;//是否定位
    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡

    private List<CheckGymListResult.CheckGymData.CheckGym> allGymList;
    private CheckGymListResult.CheckGymData.MyGymData mMyGym;

    public static ChangeGymFragment newInstance(Bundle args) {
        ChangeGymFragment fragment = new ChangeGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_gym, container, false);
        initView(view);
        setNetWorkView();
        return view;
    }

    private void initView(View view) {
        mStateView = (LikingStateView) view.findViewById(R.id.change_gym_state_view);
        mNoCardLayout = (RelativeLayout) view.findViewById(R.id.layout_no_buy_card);
        mRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.change_gym_RecyclerView);
        mMyTextView = (TextView) view.findViewById(R.id.my_gym);

        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);


        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                setNetWorkView();
            }
        });

    }

    private void setNetWorkView() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.FAILED);
        } else {
            initData();
        }
    }

    private void initData() {
        selectCityName = getArguments().getString(LikingHomeActivity.KEY_SELECT_CITY);
        selectCityId = getArguments().getString(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getArguments().getBoolean(LikingHomeActivity.KEY_START_LOCATION, false);
        gymId = getArguments().getString(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getArguments().getInt(LikingHomeActivity.KEY_TAB_INDEX, 0);
        mChangeGymAdapter = new ChangeGymAdapter(getActivity());
        sendGymRequest();
    }


    private void sendGymRequest() {
        LocationData locationData = Preference.getLocationData();
        mCheckGymPresenter = new CheckGymPresenter(getActivity(), this);
        mCheckGymPresenter.getGymList(Integer.parseInt(selectCityId), locationData.getLongitude(), locationData.getLatitude());
    }

    @Override
    public void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData) {
        if (checkGymData != null) {
            mMyGym = checkGymData.getMyGymData();
            mStateView.setState(StateView.State.SUCCESS);
            allGymList = checkGymData.getAllGymList();
            if (allGymList != null && allGymList.size() > 0) {
                mChangeGymAdapter.setData(checkGymData.getAllGymList());
                mRecyclerView.setAdapter(mChangeGymAdapter);
                setDefaultCheck();
                setOnItemClickListener();
            }
            if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId()) && !StringUtils.isEmpty(mMyGym.getGymName())) {
                mNoCardLayout.setVisibility(View.GONE);
                mMyTextView.setVisibility(View.VISIBLE);
                mMyTextView.setText("购卡场馆：" + mMyGym.getGymName());
            } else {
                mNoCardLayout.setVisibility(View.VISIBLE);
                mMyTextView.setVisibility(View.GONE);
            }

        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }

    }

    private void setDefaultCheck() {
        for (int i = 0; i < allGymList.size(); i++) {
            if (i == 0) {
                allGymList.get(0).setReCently(true);
            }
            if (String.valueOf(allGymList.get(i).getGymId()).equals(gymId)) {
                allGymList.get(i).setSelect(true);
            } else {
                allGymList.get(i).setSelect(false);
            }
        }
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }

    private void setOnItemClickListener() {
        mChangeGymAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.gym_name);
                if (textView != null) {
                    CheckGymListResult.CheckGymData.CheckGym checkGym = (CheckGymListResult.CheckGymData.CheckGym) textView.getTag();
                    if (checkGym != null) {
                        for (CheckGymListResult.CheckGymData.CheckGym gym : allGymList) {
                            if (gym.getGymId() == checkGym.getGymId()) {
                                gym.setSelect(true);
                            } else {
                                gym.setSelect(false);
                            }
                        }
                        mChangeGymAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }
}
