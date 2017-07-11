package com.goodchef.liking.module.card.buy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.BuyCardAdapter;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.eventmessages.BuyCardListMessage;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.RefreshBuyCardMessage;
import com.goodchef.liking.module.card.buy.confirm.BuyCardConfirmActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.UMengCountUtil;
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
    public static final String CARD_ID = "key_buy_card_id";

    private BuyCardAdapter mBuyCardAdapter;
    private static int mBuyType = 1;
    private static String mGymId = "-1";


    @BindView(R.id.card_state_view)
    LikingStateView mStateView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.buy_card_recyclerView)
    RecyclerView mRecyclerView;
    private TextView mCityOpenTextView;//当前城市是否开通

    private HImageView mHeadActivityBg;
    private TextView mHeadActivityTitle, mHeadActivityTime;
    private View mTitleAndTimeView, mOnlyAllView, mOnlyStaggerView, mAllAndStaggerView, mHeadActivityView;


    private TextView mAllAndStaggerTitle, mAllAndStaggerTime;
    private RadioGroup mAllAndStaggerRg;
    private RadioButton mAllAndStaggerRbAll, mAllAndStaggerRbStagger;

    public static LikingBuyCardFragment newInstance(String gymId, int buyType) {
        Bundle args = new Bundle();
        LikingBuyCardFragment fragment = new LikingBuyCardFragment();
        args.putInt("buy_type", buyType);
        args.putString("gym_id", gymId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_buy_card, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            mBuyType = getArguments().getInt("buy_type", 1);
            mGymId = getArguments().getString("gym_id", "-1");
        }
        initViews();
        return view;
    }

    protected void initViews() {
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendBuyCardListRequest();
            }
        });
        initRecycleHeadView();
        sendBuyCardListRequest();
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
    private void jumpCardConfirmActivity(CardResult.CardData.Category.CardBean card) {
        if (!StringUtils.isEmpty(mPresenter.getGymId())) {
            UMengCountUtil.UmengCount(getActivity(), UmengEventId.BUYCARDCONFIRMACTIVITY);
            Intent intent = new Intent(getActivity(), BuyCardConfirmActivity.class);
            intent.putExtra(KEY_CARD_CATEGORY, card.getCategory_name());
            intent.putExtra(KEY_CATEGORY_ID, card.getCategory_id());
            intent.putExtra(CARD_ID, card.getCard_id());
            intent.putExtra(KEY_BUY_TYPE, NumberConstantUtil.ONE);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mPresenter.getGymId());
            startActivity(intent);
        }
    }

    @Override
    public void setNoDataView() {
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                sendBuyCardListRequest();
            }
        });
        mStateView.setNodataView(noDataView);
    }

    private void initRecycleHeadView() {
        mBuyCardAdapter = new BuyCardAdapter(getContext());
        View mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.header_buy_card, null);
        mHeadActivityBg = (HImageView) mHeadView.findViewById(R.id.header_buy_card_bg);
        mHeadActivityTitle = (TextView) mHeadView.findViewById(R.id.header_buy_card_title);
        mHeadActivityTime = (TextView) mHeadView.findViewById(R.id.header_buy_card_time);
        mOnlyAllView = mHeadView.findViewById(R.id.header_buy_card_view_1);
        mAllAndStaggerView = mHeadView.findViewById(R.id.header_buy_card_view_2);
        mOnlyStaggerView = mHeadView.findViewById(R.id.header_buy_card_view_3);
        mTitleAndTimeView = mHeadView.findViewById(R.id.header_buy_card_view_0);

        mHeadActivityView = mHeadView.findViewById(R.id.head_activity);
        mAllAndStaggerTitle = (TextView) mHeadView.findViewById(R.id.header_buy_card_view_title);
        mAllAndStaggerTime = (TextView) mHeadView.findViewById(R.id.header_buy_card_view_time);

        mAllAndStaggerRg = (RadioGroup) mHeadView.findViewById(R.id.header_buy_card_view_rg);
        mAllAndStaggerRbAll = (RadioButton) mHeadView.findViewById(R.id.header_buy_card_view_rb_all);
        mAllAndStaggerRbStagger = (RadioButton) mHeadView.findViewById(R.id.header_buy_card_view_rb_stagger);

        mAllAndStaggerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.header_buy_card_view_rb_all:
                        mPresenter.setAllAndStaggerChecked(CardResult.CardData.Category.CardBean.ALL_CARD);
                        break;
                    case R.id.header_buy_card_view_rb_stagger:
                        mPresenter.setAllAndStaggerChecked(CardResult.CardData.Category.CardBean.STAGGER_CARD);
                        break;
                }
            }
        });
        mAllAndStaggerRbAll.setChecked(true);
        mPresenter.setAllAndStaggerChecked(CardResult.CardData.Category.CardBean.ALL_CARD);
        mBuyCardAdapter.setHeaderView(mHeadView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mBuyCardAdapter);

        mRefreshLayout.setColorSchemeResources(R.color.liking_green_btn_back);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendBuyCardListRequest();
            }
        });


        mBuyCardAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<CardResult.CardData.Category.CardBean>() {
            @Override
            public void onItemClick(int position, CardResult.CardData.Category.CardBean card) {
                if (card.getUser_card_status() == 0) {
                    showCanNotIntoConfirmActivity(card.getUser_card_msg());
                } else {
                    String status = card.getUse_status() + "";
                    if (!StringUtils.isEmpty(status)) {
                        if (status.equals(NumberConstantUtil.STR_ZERO)) {//0表示不可进入购卡确认页
                            showCanNotIntoConfirmActivity(card.getUse_desc());
                        } else if (status.equals(NumberConstantUtil.STR_ONE)) {
                            jumpCardConfirmActivity(card);
                        }
                    }
                }
            }
        });
    }

    private void sendBuyCardListRequest() {
        mPresenter.getCardList(mGymId, mBuyType);
    }

    @Override
    public void updateCardListView(CardResult.CardData cardData) {
        mRefreshLayout.setRefreshing(false);
        mAllAndStaggerRbAll.setChecked(true);

        if (!mPresenter.isActivity()) {
            mHeadActivityView.setVisibility(View.GONE);
        } else {
            mHeadActivityView.setVisibility(View.VISIBLE);
            mHeadActivityTitle.setText(cardData.getGymActivityBean().getName());
            mHeadActivityTime.setText(cardData.getGymActivityBean().getStart_time()
                    + " - "
                    + cardData.getGymActivityBean().getEnd_time());
        }

        if (cardData.getType() == CardResult.CardData.ALL_CARD) {
            mOnlyAllView.setVisibility(View.VISIBLE);
            mAllAndStaggerView.setVisibility(View.GONE);
            mOnlyStaggerView.setVisibility(View.GONE);
            mTitleAndTimeView.setVisibility(View.GONE);
        } else if (cardData.getType() == CardResult.CardData.STAGGER_CARD) {
            mTitleAndTimeView.setVisibility(View.VISIBLE);
            mOnlyStaggerView.setVisibility(View.VISIBLE);
            mAllAndStaggerView.setVisibility(View.GONE);
            mOnlyAllView.setVisibility(View.GONE);
        } else if (cardData.getType() == CardResult.CardData.STAGGER_AND_ALL_CARD) {
            mTitleAndTimeView.setVisibility(View.VISIBLE);
            mOnlyStaggerView.setVisibility(View.GONE);
            mAllAndStaggerView.setVisibility(View.VISIBLE);
            mOnlyAllView.setVisibility(View.GONE);
        } else {
            setNoDataView();
        }
    }

    @Override
    public void setAllAndStaggerTitleText(String text) {
        mAllAndStaggerTitle.setText(text);
    }

    @Override
    public void setAllAndStaggerTimeText(String text) {
        mAllAndStaggerTime.setText(text);
    }


    @Override
    public void setAdapter(List<CardResult.CardData.Category.CardBean> cardList) {
        mBuyCardAdapter.setDatas(cardList);
        mBuyCardAdapter.notifyDataSetChanged();

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
