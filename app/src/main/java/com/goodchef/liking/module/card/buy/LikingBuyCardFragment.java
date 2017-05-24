package com.goodchef.liking.module.card.buy;

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
import com.aaron.android.framework.base.widget.pullrefresh.PullToRefreshBase;
import com.goodchef.liking.R;
import com.goodchef.liking.module.card.buy.confirm.BuyCardConfirmActivity;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnCLickBuyCardFragmentMessage;
import com.goodchef.liking.eventmessages.RefreshBuyCardMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.CardResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.result.data.GymData;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingBuyCardFragment extends BaseFragment implements BuyCardContract.CardListView {
    public static final String KEY_CARD_CATEGORY = "key_card_category";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_BUY_TYPE = "key_buy_type";

    private BuyCardAdapter mBuyCardAdapter;
    private BuyCardContract.CardListPresenter mCardListPresenter;
    private View mHeadView;
    private static final int TYPE_BUY = 1;

    @BindView(R.id.card_state_view) LikingStateView mStateView;
    @BindView(R.id.buy_card_recyclerView) PullToRefreshRecyclerView mRecyclerView;
    private View mainView = null;
    private TextView mCityOpenTextView;//当前城市是否开通

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EnvironmentUtils.Network.isNetWorkAvailable() && mainView != null && mBuyCardAdapter != null
                && mBuyCardAdapter.getDataList().size() > 0) { //无网络情况并且有数据显示上一次的缓存
            if (mStateView != null) {
                if (mRecyclerView.getRefreshableView().getAdapter() == null) {
                    mRecyclerView.getRefreshableView().setAdapter(mBuyCardAdapter);
                }
                mStateView.setState(LikingStateView.State.SUCCESS);
            }
        } else {
            mainView = inflater.inflate(R.layout.fragment_layout_buy_card, container, false);
            ButterKnife.bind(this, mainView);
            initViews();
        }
        return mainView;
    }

    protected void initViews() {
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                LiKingVerifyUtils.initApi(getActivity());
                sendBuyCardListRequest();
            }
        });
        mRecyclerView.setRefreshViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);
        initRecycleHeadView();
        sendBuyCardListRequest();
    }

    private void setItemClickListener() {
        mBuyCardAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CardResult.CardData.Card card = mBuyCardAdapter.getDataList().get(position);
                if (card != null) {
                    if (!StringUtils.isEmpty(mCardListPresenter.getGymId())) {
                        UMengCountUtil.UmengCount(getActivity(), UmengEventId.BUYCARDCONFIRMACTIVITY);
                        Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
                        intent.putExtra(KEY_CARD_CATEGORY, card.getCategoryName());
                        intent.putExtra(KEY_CATEGORY_ID, card.getCategoryId());
                        intent.putExtra(KEY_BUY_TYPE, 1);
                        intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mCardListPresenter.getGymId());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        mStateView.setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendBuyCardListRequest();
        }
    };

    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_card_item, mRecyclerView, false);
        mCityOpenTextView = (TextView) mHeadView.findViewById(R.id.buy_card_head_text);
        mCityOpenTextView.setVisibility(View.VISIBLE);
    }

    private void sendBuyCardListRequest() {
        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
            if (mStateView != null) {
                mStateView.setState(StateView.State.FAILED);
            }
        } else {
            if (mStateView != null) {
                mStateView.setState(StateView.State.LOADING);
            }
            if(mCardListPresenter == null) {
                mCardListPresenter = new BuyCardContract.CardListPresenter(getActivity(), this);
            }
            mCardListPresenter.getCardList(TYPE_BUY);
        }
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        if (cardData != null) {
            if (mStateView == null) {
                return;
            }
            mStateView.setState(StateView.State.SUCCESS);
            GymData mGymData = cardData.getGymData();
            mCardListPresenter.setGymData(mGymData);
            if (mGymData != null) {
                CoursesResult.Courses.Gym gym = new CoursesResult.Courses.Gym();
                gym.setGymId(mGymData.getGymId());
                gym.setDistance(mGymData.getDistance());
                gym.setName(mGymData.getName());
                gym.setCityId(mGymData.getCityId());
                postEvent(new getGymDataMessage(gym));
            }
            List<CardResult.CardData.Card> list = cardData.getCardList();
            if (list != null && list.size() > 0) {
                LocationData locationData = LikingPreference.getLocationData();
                if (locationData != null) {
                    mBuyCardAdapter = new BuyCardAdapter(getActivity());
                    mBuyCardAdapter.setData(list);
                    mRecyclerView.setAdapter(mBuyCardAdapter);
                    setItemClickListener();
                    String cityName = locationData.getCityName();

                    boolean isLocation = locationData.isPositionSuccess();
                    if (!isLocation) {
                        SetHeadView();
                        mCityOpenTextView.setText(R.string.location_fails_confirm);
                    } else if (!StringUtils.isEmpty(cityName)) {
                        setHeadNoLocationView(cityName);
                    } else {
                        removeHeadView();
                    }
                }
            } else if (list == null || list.size() == 0) {
                setNoDataView();
            }
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        sendBuyCardListRequest();
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            sendBuyCardListRequest();
        }
    }

    public void onEvent(OnCLickBuyCardFragmentMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(BuyCardListMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(ChangGymMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(LoginOutMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(LoginFinishMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(CoursesErrorMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(LoginOutFialureMessage message) {
        sendBuyCardListRequest();
    }

    public void onEvent(RefreshBuyCardMessage message) {
        sendBuyCardListRequest();
    }

    private void setHeadNoLocationView(String cityName) {
        boolean isContains = false;
        BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                List<CityData> cityDataList = baseConfigData.getCityList();
                if (cityDataList != null && cityDataList.size() > 0) {
                    for (CityData data : cityDataList) {
                        if (cityName.contains(data.getCityName()) || cityName.equals(data.getCityName())) {
                            isContains = true;
                            break;
                        }
                    }
                    if (isContains) {
                        removeHeadView();
                    } else {
                        SetHeadView();
                        mCityOpenTextView.setText(R.string.current_city_no_dredge);
                    }
                }
            }
        }
    }

    private void SetHeadView() {
        if (mBuyCardAdapter != null) {
            if (mHeadView != null) {
                mBuyCardAdapter.addHeaderView(mHeadView);
            }
        }
    }

    private void removeHeadView() {
        if (mBuyCardAdapter != null) {
            if (mHeadView != null) {
                mBuyCardAdapter.removeHeaderView(mHeadView);
            }
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }
}