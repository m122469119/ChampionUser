package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.FoodListResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/22 上午10:05
 */
public class ShoppingCartAdapter extends BaseRecycleViewAdapter<ShoppingCartAdapter.ShopPingCartViewHolder, FoodListResult.FoodData.Food> {

    private Context mContext;
    public View.OnClickListener addListener;
    public View.OnClickListener reduceListener;

    public ShoppingCartAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setAddListener(View.OnClickListener listener) {
        this.addListener = listener;
    }

    public void setReduceListener(View.OnClickListener listener) {
        this.reduceListener = listener;
    }

    @Override
    protected ShopPingCartViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected ShopPingCartViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart, parent, false);
        ImageView mReduceImageView = (ImageView) view.findViewById(R.id.reduce_image);
        ImageView mAddImageView = (ImageView) view.findViewById(R.id.add_image);
        if (addListener != null) {
            mAddImageView.setOnClickListener(addListener);
        }
        if (reduceListener != null) {
            mReduceImageView.setOnClickListener(reduceListener);
        }
        return new ShopPingCartViewHolder(view);
    }

    public static class ShopPingCartViewHolder extends BaseRecycleViewHolder<FoodListResult.FoodData.Food> {
        HImageView mFoodHImageView;
        TextView mDishesNameTextView;//菜品名称
        TextView mSurplusNumberTextView;//剩余份数
        TextView mDishesMoneyTextView;//菜品价格
        TextView mBuyNumberTextView;//购买的数量
        ImageView mReduceImageView;//减按钮
        ImageView mAddImageView;//加按钮

        public ShopPingCartViewHolder(View itemView) {
            super(itemView);
            mFoodHImageView = (HImageView) itemView.findViewById(R.id.food_image);
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.dishes_name);
            mSurplusNumberTextView = (TextView) itemView.findViewById(R.id.surplus_number);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.dishes_money);
            mBuyNumberTextView = (TextView) itemView.findViewById(R.id.food_buy_number);
            mReduceImageView = (ImageView) itemView.findViewById(R.id.reduce_image);
            mAddImageView = (ImageView) itemView.findViewById(R.id.add_image);
        }

        @Override
        public void bindViews(FoodListResult.FoodData.Food object) {
            mDishesNameTextView.setText(object.getGoodsName());
            mDishesMoneyTextView.setText("¥ " + object.getPrice());
            mSurplusNumberTextView.setText("今日还剩" + object.getLeftNum() + "份");
            String imgUrl = object.getCoverImg();
            if (!StringUtils.isEmpty(imgUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mFoodHImageView, imgUrl);
            }
        }
    }


}
