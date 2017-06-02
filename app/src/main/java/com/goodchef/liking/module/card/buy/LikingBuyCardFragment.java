package com.goodchef.liking.module.card.buy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.pullrefresh.PullToRefreshBase;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.data.GymData;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.RefreshBuyCardMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.module.card.buy.confirm.BuyCardConfirmActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.NumberConstantUtil;
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
public class LikingBuyCardFragment extends BaseMVPFragment<BuyCardContract.Presenter> implements BuyCardContract.View {
    public static final String KEY_CARD_CATEGORY = "key_card_category";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_BUY_TYPE = "key_buy_type";

    private BuyCardAdapter mBuyCardAdapter;
    private android.view.View mHeadView;
    private static final int TYPE_BUY = 1;

    @BindView(R.id.card_state_view)
    LikingStateView mStateView;
    @BindView(R.id.buy_card_recyclerView)
    PullToRefreshRecyclerView mRecyclerView;
    private android.view.View mainView = null;
    private TextView mCityOpenTextView;//当前城市是否开通

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            public void onItemClick(android.view.View view, int position) {
                CardResult.CardData.Card card = mBuyCardAdapter.getDataList().get(position);
                if (card != null) {
                    String status = card.getUseStatus() + "";
                    if (!StringUtils.isEmpty(status)) {
                        if (status.equals(NumberConstantUtil.STR_ZERO)) {//0表示不可进入购卡确认页
                            showCanNotIntoConfirmActivity(card.getUseDesc());
                        } else if (status.equals(NumberConstantUtil.STR_ONE)) {
                            jumpCardConfirmActivity(card);
                        }
                    }
                }
            }

            @Override
            public boolean onItemLongClick(android.view.View view, int position) {
                return false;
            }
        });
    }


    /**
     * 展示为什么不能进入购卡确认 页面的对话框
     *
     * @param message
     */
    private void showCanNotIntoConfirmActivity(String message) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(getActivity());
        android.view.View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_one_content, null, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        titleTextView.setVisibility(android.view.View.GONE);
        contentTextView.setText(message);
        builder.setCustomView(view);
        builder.setNegativeButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 跳转到购卡详情页面
     *
     * @param card
     */
    private void jumpCardConfirmActivity(CardResult.CardData.Card card) {
        if (!StringUtils.isEmpty(mPresenter.getGymId())) {
            UMengCountUtil.UmengCount(getActivity(), UmengEventId.BUYCARDCONFIRMACTIVITY);
            Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
            intent.putExtra(KEY_CARD_CATEGORY, card.getCategoryName());
            intent.putExtra(KEY_CATEGORY_ID, card.getCategoryId());
            intent.putExtra(KEY_BUY_TYPE, NumberConstantUtil.ONE);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mPresenter.getGymId());
            startActivity(intent);
        }
    }

    private void setNoDataView() {
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
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
    private android.view.View.OnClickListener refreshOnClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            sendBuyCardListRequest();
        }
    };

    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_card_item, mRecyclerView, false);
        mCityOpenTextView = (TextView) mHeadView.findViewById(R.id.buy_card_head_text);
        mCityOpenTextView.setVisibility(android.view.View.VISIBLE);
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
            mPresenter.getCardList(TYPE_BUY);
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
            mPresenter.setGymData(mGymData);
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

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new BuyCardContract.Presenter();
    }
}
