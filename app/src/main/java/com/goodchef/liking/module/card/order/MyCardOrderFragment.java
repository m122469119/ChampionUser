package com.goodchef.liking.module.card.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.data.remote.retrofit.result.data.OrderCardData;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午2:54
 */
public class MyCardOrderFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment<CardOrderContract.Presenter> implements CardOrderContract.View {

    public static final String KEY_ORDER_ID = "key_order_id";
    private MyCardOrderAdapter mMyCardOrderAdapter;


    public static MyCardOrderFragment newInstance() {
        Bundle args = new Bundle();
        MyCardOrderFragment fragment = new MyCardOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void requestData(int page) {
        sendGetListRequest(page);
    }

    @Override
    protected void initViews() {
        setNoDataView();
        mMyCardOrderAdapter = new MyCardOrderAdapter(getActivity());
        setRecyclerAdapter(mMyCardOrderAdapter);
        onItemClick();
    }

    private void setNoDataView() {
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_order);
        noDataText.setText(R.string.no_card_data);
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


    private void onItemClick() {
        mMyCardOrderAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(android.view.View view, int position) {
                OrderCardData data = mMyCardOrderAdapter.getDataList().get(position);
                if (data != null) {
                    Intent intent = new Intent(getActivity(), MyCardDetailsActivity.class);
                    intent.putExtra(KEY_ORDER_ID, data.getOrderId());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(android.view.View view, int position) {
                return false;
            }
        });
    }


    private void sendGetListRequest(int page) {
        mPresenter.getCardOrderList(page);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(LoginOutFialureMessage message) {
        getActivity().finish();
    }

    @Override
    public void updateCardOrderListView(List<OrderCardData> listData) {
        if (listData != null) {
            updateListView(listData);
        }
    }

    public void onEvent(LoginFinishMessage message){
        loadHomePage();
    }

    @Override
    public void setPresenter() {
        mPresenter = new CardOrderContract.Presenter();
    }

    public class MyCardOrderAdapter extends BaseRecycleViewAdapter<MyCardOrderAdapter.MyCardOrderViewHolder, OrderCardData> {

        private Context mContext;

        protected MyCardOrderAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected MyCardOrderViewHolder createViewHolder(ViewGroup parent) {
            android.view.View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_card, parent, false);
            return new MyCardOrderViewHolder(view);
        }

        class MyCardOrderViewHolder extends BaseRecycleViewHolder<OrderCardData> {
            TextView mOrderNumberTextView;
            TextView mOrderStateTextView;
            TextView mOrderBuyTypeTextView;
            TextView mOrderPeriodOfValidityTextView;
            TextView mOrderMoneyTextView;
            HImageView mCardHImageView;

            public MyCardOrderViewHolder(android.view.View itemView) {
                super(itemView);
                mOrderNumberTextView = (TextView) itemView.findViewById(R.id.card_order_number);
                mOrderStateTextView = (TextView) itemView.findViewById(R.id.card_order_state);
                mOrderBuyTypeTextView = (TextView) itemView.findViewById(R.id.card_order_buyType);
                mOrderPeriodOfValidityTextView = (TextView) itemView.findViewById(R.id.card_order_period_of_validity);
                mOrderMoneyTextView = (TextView) itemView.findViewById(R.id.card_order_money);
                mCardHImageView = (HImageView) itemView.findViewById(R.id.card_image);
            }

            @Override
            public void bindViews(OrderCardData object) {
                mOrderNumberTextView.setText(getString(R.string.order_number) + object.getOrderId());
                int orderSate = object.getOrderStatus();
                if (orderSate == 1) {
                    mOrderStateTextView.setText(getString(R.string.dishes_order_state_payed));
                }
                String buyType = object.getBuyType();
                if ("1".equals(buyType)) {
                    mOrderBuyTypeTextView.setText(getString(R.string.buy_card));
                } else if ("2".equals(buyType)) {
                    mOrderBuyTypeTextView.setText(getString(R.string.continue_card));
                } else if ("3".equals(buyType)) {
                    mOrderBuyTypeTextView.setText(getString(R.string.upgrade_card));
                }
                mOrderPeriodOfValidityTextView.setText(object.getStartTime() + " ~ " + object.getEndTime());
                mOrderMoneyTextView.setText(getString(R.string.money_symbol) + object.getOrderAmount());
                String imageUrl = object.getCardImg();
                if (!StringUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.loadImage(mCardHImageView, imageUrl, MyCardOrderFragment.this.getActivity());
                }
            }
        }
    }
}
