package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.Food;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/22 上午10:05
 */
public class ShoppingCartAdapter extends BaseRecycleViewAdapter<ShoppingCartAdapter.ShopPingCartViewHolder, Food> {

    private Context mContext;

    private ShoppingDishChangedListener dishChangedListener;
    public ShoppingCartAdapter(Context context) {
        super(context);
        try {
            dishChangedListener = (ShoppingDishChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getName() + " must implements ShoppingDishChangedListener");
        }
        this.mContext = context;
    }

    public interface ShoppingDishChangedListener {
        void onShoppingDishAdded(Food foodData);

        void onShoppingDishRemove(Food foodData);
    }

    @Override
    protected ShopPingCartViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart, parent, false);
        return new ShopPingCartViewHolder(view);
    }

      class ShopPingCartViewHolder extends BaseRecycleViewHolder<Food> {
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
        public void bindViews(Food object) {
            ButtonClickListener buttonClickListener = new ButtonClickListener();
            mDishesNameTextView.setText(object.getGoodsName());
            mDishesMoneyTextView.setText("¥ " + object.getPrice());
            mSurplusNumberTextView.setText("还剩" + object.getLeftNum() + "份");
            String imgUrl = object.getCoverImg();
            if (!StringUtils.isEmpty(imgUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mFoodHImageView, imgUrl);
            }
            int restStock = object.getRestStock();
            int selectNum = object.getSelectedOrderNum();
            if (selectNum == 0) {
                mReduceImageView.setVisibility(View.INVISIBLE);
                mBuyNumberTextView.setVisibility(View.INVISIBLE);
                mAddImageView.setVisibility(View.VISIBLE);
                mAddImageView.setEnabled(true);
            } else if (selectNum == restStock) {
                mReduceImageView.setVisibility(View.VISIBLE);
                mBuyNumberTextView.setVisibility(View.VISIBLE);
                mBuyNumberTextView.setText(String.valueOf(object.getSelectedOrderNum()));
                mAddImageView.setEnabled(false);
            } else {
                mReduceImageView.setVisibility(View.VISIBLE);
                mBuyNumberTextView.setVisibility(View.VISIBLE);
                mAddImageView.setVisibility(View.VISIBLE);
                mAddImageView.setEnabled(true);
                mBuyNumberTextView.setText(String.valueOf(object.getSelectedOrderNum()));
            }
            buttonClickListener.setData(object);
            mReduceImageView.setOnClickListener(buttonClickListener);
            mAddImageView.setOnClickListener(buttonClickListener);
        }
    }


    class ButtonClickListener implements View.OnClickListener {
        private Food data;

        public void setData(Food data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            int selectedOrderNum = data.getSelectedOrderNum();
            switch (v.getId()) {
                case R.id.reduce_image:
                    --selectedOrderNum;
                    if (selectedOrderNum < 0) {
                        selectedOrderNum = 0;
                    }
                    data.setSelectedOrderNum(selectedOrderNum);
                    dishChangedListener.onShoppingDishRemove(data);
                    if (selectedOrderNum == 0) {
                        getDataList().remove(data);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.add_image:
                    data.setSelectedOrderNum(++selectedOrderNum);
                    dishChangedListener.onShoppingDishAdded(data);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

}
