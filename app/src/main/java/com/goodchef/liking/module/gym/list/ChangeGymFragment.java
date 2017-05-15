package com.goodchef.liking.module.gym.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.framework.base.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.adapter.ChangeGymAdapter;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.DrawerMessage;
import com.goodchef.liking.eventmessages.RefreshChangeCityMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:切换场馆
 * Author shaozucheng
 * Time:16/9/18 下午3:30
 */
public class ChangeGymFragment extends BaseFragment implements GymListContract.CheckGymView, View.OnClickListener {
    @BindView(R.id.my_gym)
    TextView mMyTextView;
    @BindView(R.id.change_gym_RecyclerView)
    PullToRefreshRecyclerView mRecyclerView;
    @BindView(R.id.change_gym_state_view)
    LikingStateView mStateView;

    private View mNoCardHeadView;
    private ChangeGymAdapter mChangeGymAdapter;
    private GymListContract.CheckGymPresenter mCheckGymPresenter;

    public static ChangeGymFragment newInstance(Bundle args) {
        ChangeGymFragment fragment = new ChangeGymFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_gym, container, false);
        ButterKnife.bind(this, view);
        initView();
        getInitData();
        setNetWorkView();
        setViewOnClickListener();
        return view;
    }

    private void initView() {
        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                    mStateView.setState(StateView.State.FAILED);
                } else {
                    if (!StringUtils.isEmpty(mCheckGymPresenter.getSelectCityId())) {
                        sendGymRequest();
                    }
                }
            }
        });
        setInitNoCardHeadView();
    }

    private void getInitData() {
        mChangeGymAdapter = new ChangeGymAdapter(getActivity());

        if(mCheckGymPresenter == null) {
            mCheckGymPresenter = new GymListContract.CheckGymPresenter(getActivity(), this);
        }
        Bundle bundle = getArguments();
        if(bundle != null) {
            mCheckGymPresenter.setSelectCityId(bundle.getString(LikingHomeActivity.KEY_SELECT_CITY_ID));
            mCheckGymPresenter.setGymId(bundle.getString(LikingLessonFragment.KEY_GYM_ID));
            mCheckGymPresenter.setTabIndex(bundle.getInt(LikingHomeActivity.KEY_TAB_INDEX, 0));
            mCheckGymPresenter.setIslocation(bundle.getBoolean(LikingHomeActivity.KEY_WHETHER_LOCATION));
        }
    }

    private void setNetWorkView() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.FAILED);
        }
        initData();
    }

    private void setViewOnClickListener() {
        mMyTextView.setOnClickListener(this);
    }

    private void setInitNoCardHeadView() {
        mNoCardHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_change_gym_head, mRecyclerView, false);
    }

    private void initData() {
        sendGymRequest();
    }


    private void sendGymRequest() {
        mStateView.setState(StateView.State.LOADING);
        mCheckGymPresenter.getGymList();
    }

    @Override
    public void updateCheckGymView(CheckGymListResult.CheckGymData checkGymData) {
        if (checkGymData != null) {
            CheckGymListResult.CheckGymData.MyGymData mMyGym = checkGymData.getMyGymData();
            mCheckGymPresenter.setMyGym(mMyGym);
            mStateView.setState(StateView.State.SUCCESS);
            List<CheckGymListResult.CheckGymData.CheckGym> allGymList = checkGymData.getAllGymList();
            mCheckGymPresenter.setAllGymList(allGymList);
            if (allGymList != null && allGymList.size() > 0) {
                mChangeGymAdapter.setData(checkGymData.getAllGymList());
                mRecyclerView.setAdapter(mChangeGymAdapter);
                mCheckGymPresenter.setDefaultCheck(allGymList);
                setOnItemClickListener();
            } else {
                setNoDataView();
            }
            if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId()) && !StringUtils.isEmpty(mMyGym.getGymName())) {
                if (mNoCardHeadView != null) {
                    mChangeGymAdapter.removeHeaderView(mNoCardHeadView);
                }
                mMyTextView.setVisibility(View.VISIBLE);
                mMyTextView.setText(ResourceUtils.getString(R.string.buy_card_stadium) + mMyGym.getGymName());
            } else {
                if (mNoCardHeadView != null) {
                    if (LikingPreference.isLogin()) {
                        mChangeGymAdapter.removeHeaderView(mNoCardHeadView);
                        mChangeGymAdapter.addHeaderView(mNoCardHeadView);
                    } else {
                        mChangeGymAdapter.removeHeaderView(mNoCardHeadView);
                    }
                }
                mMyTextView.setVisibility(View.GONE);
            }

        } else {
            setNoDataView();
        }

    }

    /***
     * 设置没有数据
     */
    private void setNoDataView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_chenge_no_data, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.no_buy_card_imageView);
        TextView myGymTextView = (TextView) view.findViewById(R.id.no_data_my_gym);
        CheckGymListResult.CheckGymData.MyGymData mMyGym = mCheckGymPresenter.getMyGym();
        if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId()) && !StringUtils.isEmpty(mMyGym.getGymName())) {
            imageView.setVisibility(View.GONE);
            myGymTextView.setVisibility(View.VISIBLE);
            myGymTextView.setText(getString(R.string.buy_card_stadium) + mMyGym.getGymName());
        } else {
            if (LikingPreference.isLogin()) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            myGymTextView.setVisibility(View.GONE);
        }
        myGymTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpHomeActivity();
            }
        });
        mStateView.setState(StateView.State.NO_DATA);
        mStateView.setNodataView(view);
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
                        for (CheckGymListResult.CheckGymData.CheckGym gym : mCheckGymPresenter.getAllGymList()) {
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
        LikingHomeActivity.gymId = gymId;
        LikingHomeActivity.isChangeGym = true;
        postEvent(new ChangGymMessage(mCheckGymPresenter.getTabIndex()));
        Intent intent = new Intent(getActivity(), LikingHomeActivity.class);
        intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, mCheckGymPresenter.getTabIndex());
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
        mCheckGymPresenter.setGymId(message.getCityId());
        mCheckGymPresenter.setLongitude(message.getLongitude());
        mCheckGymPresenter.setLatitude(message.getLatitude());
        sendGymRequest();
    }


    @OnClick(R.id.my_gym)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_gym:
                jumpHomeActivity();
                break;
            default:
                break;
        }
    }


    private void jumpHomeActivity() {
        CheckGymListResult.CheckGymData.MyGymData mMyGym = mCheckGymPresenter.getMyGym();
        if (mMyGym != null && !StringUtils.isEmpty(mMyGym.getGymId())) {
            UMengCountUtil.UmengCount(getActivity(), UmengEventId.MY_GYM_BTN);
            jumpLikingHomeActivity(mMyGym.getGymId());
        }
    }
}
