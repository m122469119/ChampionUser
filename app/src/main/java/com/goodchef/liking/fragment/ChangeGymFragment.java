package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.DrawerMessage;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.mvp.presenter.CheckGymPresenter;
import com.goodchef.liking.mvp.view.CheckGymView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/18 下午3:30
 */
public class ChangeGymFragment extends BaseFragment implements CheckGymView, View.OnClickListener {
    private LikingStateView mStateView;
    private PullToRefreshRecyclerView mRecyclerView;
    private TextView mMyTextView;
    private View mNoCardHeadView;

    private ChangeGymAdapter mChangeGymAdapter;
    private CheckGymPresenter mCheckGymPresenter;

    private String selectCityId;//选择的城市id
    private boolean isLoaction;//是否定位
    private String gymId;//场馆id
    private int tabIndex;//从哪个位置切换过来的标志位，首页或者是买卡

    private double longitude;
    private double latitude;

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
        setViewOnClickListener();
        return view;
    }

    private void initView(View view) {
        mStateView = (LikingStateView) view.findViewById(R.id.change_gym_state_view);
        mRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.change_gym_RecyclerView);
        mMyTextView = (TextView) view.findViewById(R.id.my_gym);

        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    mStateView.setState(StateView.State.FAILED);
                } else {
                    sendGymRequest(Integer.parseInt(selectCityId), longitude, latitude);
                }
            }
        });
        setInitNoCardHeadView();
    }

    private void setViewOnClickListener() {
        mMyTextView.setOnClickListener(this);
    }

    private void setInitNoCardHeadView() {
        mNoCardHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_change_gym_head, mRecyclerView, false);
    }

    private void setNetWorkView() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.FAILED);
        } else {
            initData();
        }
    }

    private void initData() {
        selectCityId = getArguments().getString(LikingHomeActivity.KEY_SELECT_CITY_ID);
        isLoaction = getArguments().getBoolean(LikingHomeActivity.KEY_START_LOCATION, false);
        gymId = getArguments().getString(LikingLessonFragment.KEY_GYM_ID);
        tabIndex = getArguments().getInt(LikingHomeActivity.KEY_TAB_INDEX, 0);
        mChangeGymAdapter = new ChangeGymAdapter(getActivity());
        LocationData locationData = Preference.getLocationData();
        sendGymRequest(Integer.parseInt(selectCityId), locationData.getLongitude(), locationData.getLatitude());
    }


    private void sendGymRequest(int cityId, double longitude, double latitude) {
        mCheckGymPresenter = new CheckGymPresenter(getActivity(), this);
        mCheckGymPresenter.getGymList(cityId, longitude, latitude);
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
            } else {
                mStateView.setState(StateView.State.NO_DATA);
            }
            if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId()) && !StringUtils.isEmpty(mMyGym.getGymName())) {
                if (mNoCardHeadView != null) {
                    mChangeGymAdapter.setHeaderView(null);
                    mChangeGymAdapter.notifyDataSetChanged();
                }
                mMyTextView.setVisibility(View.VISIBLE);
                mMyTextView.setText("购卡场馆：" + mMyGym.getGymName());
            } else {
                if (mNoCardHeadView != null) {
                    mChangeGymAdapter.setHeaderView(mNoCardHeadView);
                    mChangeGymAdapter.notifyDataSetChanged();
                }
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
                        jumpLikingHomeActivity(String.valueOf(checkGym.getGymId()));
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    /**
     * 跳转到首页
     *
     * @param gymId
     */
    private void jumpLikingHomeActivity(String gymId) {
        UMengCountUtil.UmengCount(getActivity(), UmengEventId.GYMCOURSESACTIVITY);
        postEvent(new ChangGymMessage(gymId, tabIndex));
        Intent intent = new Intent(getActivity(), LikingHomeActivity.class);
        intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, tabIndex);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(DrawerMessage message) {

    }

    public void onEvent(RefreshChangeCityMessage message) {
        selectCityId = message.getCityId();
        longitude = message.getLongitude();
        latitude = message.getLatitude();
        sendGymRequest(Integer.parseInt(selectCityId), longitude, latitude);
    }


    @Override
    public void onClick(View v) {
        if (v == mMyTextView) {
            if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId())) {
                jumpLikingHomeActivity(mMyGym.getGymId());
            }
        }
    }
}
