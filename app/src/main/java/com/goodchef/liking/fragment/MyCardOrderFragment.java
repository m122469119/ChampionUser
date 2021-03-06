package com.goodchef.liking.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.MyCardDetailsActivity;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.OrderCardListResult;
import com.goodchef.liking.http.result.data.OrderCardData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午2:54
 */
public class MyCardOrderFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment {

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
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
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
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadHomePage();
        }
    };


    private void onItemClick() {
        mMyCardOrderAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderCardData data = mMyCardOrderAdapter.getDataList().get(position);
                if (data != null) {
                    Intent intent = new Intent(getActivity(), MyCardDetailsActivity.class);
                    intent.putExtra(KEY_ORDER_ID, data.getOrderId());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    private void sendGetListRequest(int page) {
        LiKingApi.getCardOrderList(Preference.getToken(), page, new PagerRequestCallback<OrderCardListResult>(this) {
            @Override
            public void onSuccess(OrderCardListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    List<OrderCardData> listData = result.getData().getOrderCardList();
                    if (listData != null) {
                        updateListView(listData);
                    }
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(LoginOutFialureMessage message) {
        getActivity().finish();
    }


    public class MyCardOrderAdapter extends BaseRecycleViewAdapter<MyCardOrderAdapter.MyCardOrderViewHolder, OrderCardData> {

        private Context mContext;

        protected MyCardOrderAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected MyCardOrderViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_card, parent, false);
            return new MyCardOrderViewHolder(view);
        }

        class MyCardOrderViewHolder extends BaseRecycleViewHolder<OrderCardData> {
            TextView mOrderNumberTextView;
            TextView mOrderStateTextView;
            TextView mOrderBuyTypeTextView;
            TextView mOrderPeriodOfValidityTextView;
            TextView mOrderMoneyTextView;
            HImageView mCardHImageView;

            public MyCardOrderViewHolder(View itemView) {
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
                    HImageLoaderSingleton.getInstance().loadImage(mCardHImageView, imageUrl);
                }
            }
        }
    }
}
