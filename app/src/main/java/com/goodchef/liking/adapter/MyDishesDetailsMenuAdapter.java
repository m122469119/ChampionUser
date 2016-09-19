package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.MyDishesOrderDetailsResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午9:58
 */
public class MyDishesDetailsMenuAdapter extends BaseRecycleViewAdapter<MyDishesDetailsMenuAdapter.MyDishesDetailsMenuViewHolder, MyDishesOrderDetailsResult.OrderDetailsData.FoodListData> {

    private Context mContext;

    public MyDishesDetailsMenuAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected MyDishesDetailsMenuViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_confirm_dishes, parent, false);
        return new MyDishesDetailsMenuViewHolder(view);
    }

    public class MyDishesDetailsMenuViewHolder extends BaseRecycleViewHolder<MyDishesOrderDetailsResult.OrderDetailsData.FoodListData> {
        TextView mDishesNameTextView;
        TextView mDishesNumberTextView;
        TextView mDishesMoneyTextView;

        public MyDishesDetailsMenuViewHolder(View itemView) {
            super(itemView);
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_name);
            mDishesNumberTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_number);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_money);
        }

        @Override
        public void bindViews(MyDishesOrderDetailsResult.OrderDetailsData.FoodListData object) {
            mDishesNameTextView.setText(object.getFoodName());
            mDishesNumberTextView.setText("x " + object.getFoodNum());
            mDishesMoneyTextView.setText("¥ " + object.getTotalAmount());
        }
    }
}
