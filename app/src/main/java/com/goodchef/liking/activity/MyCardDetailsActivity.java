package com.goodchef.liking.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.MyCardOrderFragment;
import com.goodchef.liking.http.result.MyOrderCardDetailsResult;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.goodchef.liking.mvp.MyCardDetailsContract;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:我的会员卡详情
 * Author shaozucheng
 * Time:16/7/1 下午2:23
 */
public class MyCardDetailsActivity extends AppBarActivity implements MyCardDetailsContract.MyCardDetailsView {
    private static final int BUY_TYPE_BUY = 1;//买卡
    private static final int BUY_TYPE_CONTINUE = 2;//续卡
    private static final int BUY_TYPE_UPGRADE = 3;//升级卡
    private static final int PAY_TYPE_WECHAT = 0;//微信支付
    private static final int PAY_TYPE_ALIPLY = 1;//支付宝支付
    private static final int PAY_TYPE_FREE = 3;//免金额


    @BindView(R.id.card_order_number) TextView mOrderNumberTextView;
    @BindView(R.id.card_buy_time) TextView mBuyTimeTextView;
    @BindView(R.id.card_buy_state) TextView mBuyStateTextView;
    @BindView(R.id.card_buy_way) TextView mBuyWayTextView;
    @BindView(R.id.card_period_of_validity) TextView mPeriodOfValidityTextView;
    @BindView(R.id.card_buy_type) TextView mBuyTypeTextView;
    @BindView(R.id.card_price) TextView mCardPriceTextView;
    @BindView(R.id.card_limint_recyclerView) RecyclerView mTimeLimitRecyclerView;
    @BindView(R.id.layout_favourable) LinearLayout mFavourableLayout;
    @BindView(R.id.favourable_number) TextView mFavourableNumberTextView;
    @BindView(R.id.favourable_line) ImageView mImageViewLine;
    @BindView(R.id.gym_name) TextView mGymNameTextView;
    @BindView(R.id.gym_address) TextView mGymAddressTextView;
    @BindView(R.id.my_card_details_state_view) LikingStateView mStateView;

    private String orderId;//订单id

    private MyCardDetailsContract.MyCardDetailsPresenter mMyCardDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_details);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_my_card_details));
        initView();
        iniData();
    }

    private void initView() {

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendCardDetailsRequest();
            }
        });
    }

    private void iniData() {
        orderId = getIntent().getStringExtra(MyCardOrderFragment.KEY_ORDER_ID);
        mMyCardDetailsPresenter = new MyCardDetailsContract.MyCardDetailsPresenter(this, this);
        sendCardDetailsRequest();
    }

    private void sendCardDetailsRequest() {
        mMyCardDetailsPresenter.getCardDetails(orderId);
    }

    @Override
    public void updateMyCardDetailsView(MyOrderCardDetailsResult.OrderCardDetailsData data) {
        if (data != null) {
            mStateView.setState(StateView.State.SUCCESS);
            mOrderNumberTextView.setText(getString(R.string.order_number) + data.getOrderId());
            mBuyTimeTextView.setText(getString(R.string.buy_time) + data.getOrderTime());
            int orderSate = data.getOrderStatus();
            if (orderSate == 1) {
                mBuyStateTextView.setText(R.string.dishes_order_state_payed);
            }
            int buyType = data.getBuyType();
            if (buyType == BUY_TYPE_BUY) {
                mBuyWayTextView.setText(R.string.buy_card);
            } else if (buyType == BUY_TYPE_CONTINUE) {
                mBuyWayTextView.setText(R.string.continue_card);
            } else if (buyType == BUY_TYPE_UPGRADE) {
                mBuyWayTextView.setText(R.string.upgrade_card);
            }
            mPeriodOfValidityTextView.setText(data.getStartTime() + " ~ " + data.getEndTime());
            int payType = data.getPayType();
            if (payType == PAY_TYPE_WECHAT) {
                mBuyTypeTextView.setText(R.string.pay_wechat_type);
            } else if (payType == PAY_TYPE_ALIPLY) {
                mBuyTypeTextView.setText(R.string.pay_alipay_type);
            } else if (payType == PAY_TYPE_FREE) {
                mBuyTypeTextView.setText(R.string.pay_free_type);
            }
            mCardPriceTextView.setText(getString(R.string.money_symbol) + data.getOrderAmount());
            mGymNameTextView.setText(data.getGym_name());
            mGymAddressTextView.setText(data.getGym_address());

            List<TimeLimitData> limitDataList = data.getTimeLimit();
            if (limitDataList != null && limitDataList.size() > 0) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mTimeLimitRecyclerView.setLayoutManager(mLayoutManager);
                MyCardTimeLimitAdapter adapter = new MyCardTimeLimitAdapter(this);
                adapter.setData(limitDataList);
                mTimeLimitRecyclerView.setAdapter(adapter);
            }

            String couponAmount = data.getCouponAmount();
            if (!StringUtils.isEmpty(couponAmount)) {
                if (couponAmount.equals("0")) {
                    mFavourableLayout.setVisibility(View.GONE);
                    mImageViewLine.setVisibility(View.GONE);
                } else {
                    mFavourableLayout.setVisibility(View.VISIBLE);
                    mImageViewLine.setVisibility(View.VISIBLE);
                    mFavourableNumberTextView.setText(getString(R.string.money_symbol) + couponAmount);
                }
            } else {
                mFavourableLayout.setVisibility(View.GONE);
                mImageViewLine.setVisibility(View.GONE);
            }

        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void showToast(String message) {
        PopupUtils.showToast(message);
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }

    public class MyCardTimeLimitAdapter extends BaseRecycleViewAdapter<MyCardTimeLimitAdapter.CardTimeLimitViewHolder, TimeLimitData> {
        private Context mContext;

        public MyCardTimeLimitAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected CardTimeLimitViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_card_time_limit, parent, false);
            return new CardTimeLimitViewHolder(view);
        }

        public class CardTimeLimitViewHolder extends BaseRecycleViewHolder<TimeLimitData> {
            TextView mLimitTitleTextView;
            TextView mCardPeriodTextView;

            public CardTimeLimitViewHolder(View itemView) {
                super(itemView);
                mLimitTitleTextView = (TextView) itemView.findViewById(R.id.my_card_limit_title);
                mCardPeriodTextView = (TextView) itemView.findViewById(R.id.my_card_period);
            }

            @Override
            public void bindViews(TimeLimitData object) {
                mLimitTitleTextView.setText(object.getTitle());
                mCardPeriodTextView.setText(object.getDesc());
            }
        }
    }
}
