package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.thirdparty.pay.alipay.AliPay;
import com.aaron.android.thirdparty.pay.alipay.OnAliPayListener;
import com.aaron.android.thirdparty.pay.weixin.WeixinPay;
import com.aaron.android.thirdparty.pay.weixin.WeixinPayListener;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.MyDishesOrderDetailsActivity;
import com.goodchef.liking.adapter.MyDishesOrderAdapter;
import com.goodchef.liking.eventmessages.MyDishesListWechatMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.DishesOrderListResult;
import com.goodchef.liking.http.result.data.PayResultData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyDishesOrderPresenter;
import com.goodchef.liking.mvp.view.MyDishesOrderView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.widgets.dialog.SelectPayTypeCustomDialog;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午2:52
 */
public class MyDishesOrderFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyDishesOrderView {
    public static final String INTENT_KEY_ORDER_ID = "intent_key_order_id";
    private MyDishesOrderAdapter mMyDishesOrderAdapter;
    private MyDishesOrderPresenter mMyDishesOrderPresenter;

    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信

    @Override
    protected void requestData(int page) {
        sendListRequest(page);
    }

    @Override
    protected void initViews() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.no_order);
        noDataText.setText("暂无数据");
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);

        setPullType(PullMode.PULL_BOTH);
        mMyDishesOrderPresenter = new MyDishesOrderPresenter(getActivity(), this);
        initRecycleViewData();
        initPayModule();
    }

    private void initPayModule() {
        mAliPay = new AliPay(getActivity(), mOnAliPayListener);
        mWeixinPay = new WeixinPay(getActivity(), mWeixinPayListener);
    }


    private void initRecycleViewData() {
        mMyDishesOrderAdapter = new MyDishesOrderAdapter(getActivity());
        setRecyclerAdapter(mMyDishesOrderAdapter);
        getPullToRefreshRecyclerView().setDividerDrawable(null);
        mMyDishesOrderAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.order_number);
                if (textView != null) {
                    DishesOrderListResult.DishesOrderData.DishesOrder object = (DishesOrderListResult.DishesOrderData.DishesOrder) textView.getTag();
                    if (object != null) {
                        Intent intent = new Intent(getActivity(), MyDishesOrderDetailsActivity.class);
                        intent.putExtra(INTENT_KEY_ORDER_ID, object.getOrderId());
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

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadHomePage();
        }
    };


    private void sendListRequest(int page) {
        LiKingApi.getDishesOrderList(Preference.getToken(), page, new PagerRequestCallback<DishesOrderListResult>(this) {
            @Override
            public void onSuccess(DishesOrderListResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                    mMyDishesOrderAdapter.setData(result.getData().getOrderList());
                    mMyDishesOrderAdapter.notifyDataSetChanged();
                    mMyDishesOrderAdapter.setClickListener(mClickListener);
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    /***
     * 适配器中各种事件
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_pay://去支付
                    TextView textView = (TextView) v.findViewById(R.id.go_pay);
                    if (textView != null) {
                        DishesOrderListResult.DishesOrderData.DishesOrder object = (DishesOrderListResult.DishesOrderData.DishesOrder) textView.getTag();
                        if (object != null) {
                            showPayDialog(object.getOrderId());
                        }
                    }
                    break;
                case R.id.cancel_order://取消订单
                    TextView cancelText = (TextView) v.findViewById(R.id.cancel_order);
                    if (cancelText != null) {
                        DishesOrderListResult.DishesOrderData.DishesOrder object = (DishesOrderListResult.DishesOrderData.DishesOrder) cancelText.getTag();
                        if (object != null) {
                            sendCancelOrderRequest(object.getOrderId());
                        }
                    }
                    break;
                case R.id.confirm_get_dishes_btn://确认完成
                    TextView confirmTextView = (TextView) v.findViewById(R.id.confirm_get_dishes_btn);
                    if (confirmTextView != null) {
                        DishesOrderListResult.DishesOrderData.DishesOrder object = (DishesOrderListResult.DishesOrderData.DishesOrder) confirmTextView.getTag();
                        if (object != null) {
                            sendCompleteOrderRequest(object.getOrderId());
                        }
                    }
                    break;
            }
        }
    };

    private void sendCompleteOrderRequest(String orderId) {
        mMyDishesOrderPresenter.completeDishesOrder(orderId);
    }

    private void sendCancelOrderRequest(String orderId) {
        mMyDishesOrderPresenter.cancelMyDishesOrder(orderId);
    }

    private void showPayDialog(final String orderId) {
        final SelectPayTypeCustomDialog customDialog = new SelectPayTypeCustomDialog(getActivity(), "");
        customDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.alipay_pay_layout) {//支付宝支付
                    sendGoPayRequest(orderId, "1");
                    customDialog.dismiss();
                } else if (v.getId() == R.id.wechat_pay_layout) {//微信支付
                    sendGoPayRequest(orderId, "0");
                    customDialog.dismiss();
                }
            }
        });
    }

    private void sendGoPayRequest(String payType, String orderId) {
        mMyDishesOrderPresenter.myDishesOrderPay(payType, orderId);
    }


    @Override
    public void updateMyDishesPayView(PayResultData payResultData) {
        int payType = payResultData.getPayType();
        if (payType == 3) {//3 免金额支付
            PopupUtils.showToast("支付成功");
            loadHomePage();
        } else {
            handlePay(payResultData);
        }
    }

    private void handlePay(PayResultData data) {
        int payType = data.getPayType();
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(data.getAliPayToken());
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = WXPayEntryActivity.PAY_TYPE_MY_DISHES_LIST;
                WXPayEntryActivity.orderId = data.getOrderId();
                mWeixinPay.setPrePayId(data.getWxPrepayId());
                mWeixinPay.doPay();
                break;
        }
    }


    @Override
    public void updateCancelDishesOrder() {
        loadHomePage();
    }

    @Override
    public void updateCompleteDishesOrder() {
        loadHomePage();
    }


    /**
     * 支付宝支付结果处理
     */
    private final OnAliPayListener mOnAliPayListener = new OnAliPayListener() {
        @Override
        public void onStart() {
            LogUtils.e(TAG, "alipay start");
        }

        @Override
        public void onSuccess() {
            loadHomePage();
        }

        @Override
        public void onFailure(String errorMessage) {

        }

        @Override
        public void confirm() {
        }
    };

    /**
     * 微信支付结果处理
     */
    private WeixinPayListener mWeixinPayListener = new WeixinPayListener() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onFailure(String errorMessage) {
        }
    };

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MyDishesListWechatMessage wechatMessage) {
        loadHomePage();
    }
}
