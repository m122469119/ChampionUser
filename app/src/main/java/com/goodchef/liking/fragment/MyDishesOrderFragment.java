package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.DateUtils;
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
import com.goodchef.liking.eventmessages.CancelMyDishesOrderMessage;
import com.goodchef.liking.eventmessages.CompleteMyDishesOrderMessage;
import com.goodchef.liking.eventmessages.MyDishesOrderAlipayMessage;
import com.goodchef.liking.eventmessages.MyDishesDetailsWechatMessage;
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

import java.util.Date;
import java.util.List;

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
    private boolean mIsUpdateListTimeStarting = false;

    @Override
    protected void requestData(int page) {
        sendListRequest(page);
    }

    @Override
    protected void initViews() {
        setPullType(PullMode.PULL_BOTH);
        setNoDataView();
        mMyDishesOrderPresenter = new MyDishesOrderPresenter(getActivity(), this);
        initRecycleViewData();
        initPayModule();
    }

    private void setNoDataView(){
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_order);
        noDataText.setText(R.string.no_dishes_order);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
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
        mMyDishesOrderAdapter.setClickListener(mClickListener);
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
                    List<DishesOrderListResult.DishesOrderData.DishesOrder> orderList = result.getData().getOrderList();
                    setOrderListData(orderList);
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    private void setOrderListData(List<DishesOrderListResult.DishesOrderData.DishesOrder> orderList){
        if (orderList != null && orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                long serviceTime = DateUtils.currentDataSeconds() + LiKingApi.sTimestampOffset;//服务器当前时间
                Log.e("serviceTime=", serviceTime + "");
                String orderTime = orderList.get(i).getOrderTime();//下单时间String类型,
                Log.e("orderTime=", orderTime + "");
                Date oderDate = DateUtils.parseString("yyyy-MM-dd HH:mm:ss", orderTime);//转换为时间格式
                Log.e("oderDate=", oderDate + "");
                long orderDateLong = oderDate.getTime() / 1000;//将订单时间转换为s
                Log.e("orderDateLong=", orderDateLong + "");
                //  long limitTime = (long) orderList.get(i).getLimitSecond();//限时时间
                long limitTime = (long) 300;//暂时定死
                long orderOverdueTime = orderDateLong + limitTime;//订单过期时间 = 下单时间+限时时间
                Log.e("orderOverdueTime=", orderOverdueTime + "");
                long orderSurplusTime = orderOverdueTime - serviceTime;// 订单剩余时间 = 服务器订单过期时间-服务器当前时间
                Log.e("orderSurplusTime=", orderSurplusTime + "");
                long orderSurplusData = Math.abs(orderSurplusTime);
                if (orderSurplusData > 0) {
                    orderList.get(i).setOrderSurplusTime(orderSurplusData);
                } else if (orderSurplusData <= 0) {
                    orderList.get(i).setOrderSurplusTime(0);
                }
            }
        }
        updateListView(orderList);
        if (!mIsUpdateListTimeStarting) {
            mIsUpdateListTimeStarting = true;
            startUpdateListTime();
        }
    }


    Handler handler = new Handler();
    private void startUpdateListTime() {
        if (mUpdateListTimeRunnable != null) {
            handler.postDelayed(mUpdateListTimeRunnable, 1000);
        }
    }

    private Runnable mUpdateListTimeRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < mMyDishesOrderAdapter.getDataList().size(); i++) {
                DishesOrderListResult.DishesOrderData.DishesOrder order = mMyDishesOrderAdapter.getDataList().get(i);
                //计算每一个item的倒计时时间
                long time = Math.abs(order.getOrderSurplusTime());
                if (time > 0) {//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                    order.setOrderSurplusTime(time - 1);
                } else {
                    order.setOrderSurplusTime(0);
                    order.setOrderStatus(2);
                }

            }
            mMyDishesOrderAdapter.notifyDataSetChanged();
            startUpdateListTime();
        }
    };

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(mUpdateListTimeRunnable);
            mUpdateListTimeRunnable = null;
            mIsUpdateListTimeStarting = false;
        }
        super.onDestroy();
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

    public void onEvent(MyDishesDetailsWechatMessage wechatMessage) {
       loadHomePage();
    }

    public void onEvent(MyDishesOrderAlipayMessage orderAlipyMessage) {
        loadHomePage();
    }

    public void onEvent(CancelMyDishesOrderMessage cancelMyDishesOrderMessage) {
        loadHomePage();
    }

    public void onEvent(CompleteMyDishesOrderMessage completeMyDishesOrderMessage) {
        loadHomePage();
    }

}
