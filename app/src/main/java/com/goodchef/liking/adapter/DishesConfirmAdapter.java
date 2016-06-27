package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.Food;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/26 下午3:58
 */
public class DishesConfirmAdapter extends BaseRecycleViewAdapter<DishesConfirmAdapter.DishesConfirmViewHolder, Food> {

    private Context mContext;

    public DishesConfirmAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected DishesConfirmViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected DishesConfirmViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_confirm_dishes, parent, false);
        return new DishesConfirmViewHolder(view);
    }

    public class DishesConfirmViewHolder extends BaseRecycleViewHolder<Food> {
        TextView mDishesNameTextView;
        TextView mDishesNumberTextView;
        TextView mDishesMoneyTextView;

        public DishesConfirmViewHolder(View itemView) {
            super(itemView);
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_name);
            mDishesNumberTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_number);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.confirm_dishes_money);
        }

        @Override
        public void bindViews(Food object) {
            mDishesNameTextView.setText(object.getGoodsName());
            mDishesNumberTextView.setText("x " + object.getSelectedOrderNum());
            mDishesMoneyTextView.setText("¥ " + object.getPrice());
        }
    }
}
