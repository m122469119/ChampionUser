package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.DishesOrderListResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午3:31
 */
public class MyDishesOrderAdapter extends BaseRecycleViewAdapter<MyDishesOrderAdapter.MyDishesOrderViewHolder, DishesOrderListResult.DishesOrderData.DishesOrder> {
    private static final int ORDER_STATE_SUBMIT = 0;//0:已提交
    private static final int ORDER_STATE_PAYED = 1;//1:已支付
    private static final int ORDER_STATE_CANCEL = 2;// 2:已取消
    private static final int ORDER_STATE_GET_DEISHES = 3; //3:已取餐

    private Context mContext;
    private View.OnClickListener mClickListener;

    public MyDishesOrderAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    protected MyDishesOrderViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected MyDishesOrderViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dishes_order_list, parent, false);
        return new MyDishesOrderViewHolder(view);
    }

    class MyDishesOrderViewHolder extends BaseRecycleViewHolder<DishesOrderListResult.DishesOrderData.DishesOrder> {
        TextView mSerialNumberTextView;//流水号
        TextView mOrderNumberTextView;//订单号
        TextView mOrderStateTextView;//状态
        TextView mDishesNameTextView;//场馆名称
        TextView mDishesMoneyTextView;//菜品金额
        TextView mGetMealsTimeTextView;//取餐时间
        TextView mGetMealsAddressTextView;//取餐地址
        TextView mPaySurplusTimeTextView;//剩余支付时间
        TextView mGoPayBtn;//去支付
        TextView mCancelOrderBtn;//取消
        TextView mConfirmGetDishesBtn;//确认点餐
        RelativeLayout mPayLayout;
        HImageView mDishesHImageView;

        public MyDishesOrderViewHolder(View itemView) {
            super(itemView);
            mSerialNumberTextView = (TextView) itemView.findViewById(R.id.serial_number);
            mOrderNumberTextView = (TextView) itemView.findViewById(R.id.order_number);
            mOrderStateTextView = (TextView) itemView.findViewById(R.id.order_state);
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.dishes_name);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.dishes_money);
            mGetMealsTimeTextView = (TextView) itemView.findViewById(R.id.get_meals_time);
            mGetMealsAddressTextView = (TextView) itemView.findViewById(R.id.get_meals_address);
            mPaySurplusTimeTextView = (TextView) itemView.findViewById(R.id.pay_surplus_time);
            mGoPayBtn = (TextView) itemView.findViewById(R.id.go_pay);
            mCancelOrderBtn = (TextView) itemView.findViewById(R.id.cancel_order);
            mConfirmGetDishesBtn = (TextView) itemView.findViewById(R.id.confirm_get_dishes_btn);
            mPayLayout = (RelativeLayout) itemView.findViewById(R.id.layout_order_pay);
            mDishesHImageView = (HImageView) itemView.findViewById(R.id.dishes_image);
        }

        @Override
        public void bindViews(DishesOrderListResult.DishesOrderData.DishesOrder object) {
            mSerialNumberTextView.setText("流水号：" + object.getSerialNumber());
            mOrderNumberTextView.setText("订单号：" + object.getOrderId());
            mDishesNameTextView.setText(object.getGymName());
            mDishesMoneyTextView.setText("¥ " + object.getOrderAmount());
            mGetMealsTimeTextView.setText(object.getFetchTime());
            mGetMealsAddressTextView.setText(object.getGymAddress());
            int state = object.getOrderStatus();
            setOrderState(state);
            String imageUrl = object.getGymImg();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mDishesHImageView, imageUrl);
            }
            mPaySurplusTimeTextView.setText("剩余支付时间：" + object.getLeftSecond());
            mGoPayBtn.setOnClickListener(mClickListener);
            mCancelOrderBtn.setOnClickListener(mClickListener);
            mConfirmGetDishesBtn.setOnClickListener(mClickListener);
        }

        private void setOrderState(int state) {
            if (state == ORDER_STATE_SUBMIT) {//已提交
                mConfirmGetDishesBtn.setVisibility(View.GONE);
                mPayLayout.setVisibility(View.VISIBLE);
                mOrderStateTextView.setText(R.string.dishes_order_state_submit);
            } else if (state == ORDER_STATE_PAYED) {//已支付
                mConfirmGetDishesBtn.setVisibility(View.VISIBLE);
                mPayLayout.setVisibility(View.GONE);
                mOrderStateTextView.setText(R.string.dishes_order_state_payed);
            } else if (state == ORDER_STATE_CANCEL) {//已取消
                mConfirmGetDishesBtn.setVisibility(View.GONE);
                mPayLayout.setVisibility(View.GONE);
                mOrderStateTextView.setText(R.string.dishes_order_state_cancel);
            } else if (state == ORDER_STATE_GET_DEISHES) {//已完成
                mConfirmGetDishesBtn.setVisibility(View.GONE);
                mPayLayout.setVisibility(View.GONE);
                mOrderStateTextView.setText(R.string.dishes_order_state_complete);
            }
        }
    }
}
