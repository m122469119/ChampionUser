package com.goodchef.liking.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
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
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.Food;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/25 下午2:33
 */
public class LikingNearbyAdapter extends BaseRecycleViewAdapter<LikingNearbyAdapter.LikingNearbyViewHolder, Food> {

    private Context mContext;
    private ShoppingDishChangedListener dishChangedListener;

    public interface ShoppingDishChangedListener {
        void onShoppingDishAdded(Food foodData);

        void onShoppingDishRemove(Food foodData);
    }

    public LikingNearbyAdapter(Context context) {
        super(context);
        try {
            dishChangedListener = (ShoppingDishChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getName() + " must implements ShoppingDishChangedListener");
        }
        this.mContext = context;
    }

    @Override
    protected LikingNearbyViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_liking_nearby, parent, false);
        return new LikingNearbyViewHolder(view);
    }


    class LikingNearbyViewHolder extends BaseRecycleViewHolder<Food> {
        HImageView mFoodHImageView;
        TextView mDishesNameTextView;//菜品名称
        TextView mSurplusNumberTextView;//剩余份数
        TextView mDishesMoneyTextView;//菜品价格
        TextView mDishesTypeTextView;//菜品标签
        TextView mBuyPersonTextView;//购买过的人数
        TextView mBuyNumberTextView;//购买的数量
        ImageView mReduceImageView;//减按钮
        ImageView mAddImageView;//加按钮
        CardView mCardView;

        public LikingNearbyViewHolder(View itemView) {
            super(itemView);
            mFoodHImageView = (HImageView) itemView.findViewById(R.id.food_image);
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.dishes_name);
            mSurplusNumberTextView = (TextView) itemView.findViewById(R.id.surplus_number);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.dishes_money);
            mDishesTypeTextView = (TextView) itemView.findViewById(R.id.dishes_type);
            mBuyPersonTextView = (TextView) itemView.findViewById(R.id.buy_person);
            mBuyNumberTextView = (TextView) itemView.findViewById(R.id.food_buy_number);
            mReduceImageView = (ImageView) itemView.findViewById(R.id.reduce_image);
            mAddImageView = (ImageView) itemView.findViewById(R.id.add_image);
            mCardView = (CardView) itemView.findViewById(R.id.nearby_card_view);
            setCardView();
        }

        private void setCardView() {//兼容低版本
            if (mCardView != null) {
                if (Build.VERSION.SDK_INT < 21) {
                    mCardView.setCardElevation(0);
                } else {
                    mCardView.setCardElevation(10);
                }
            }
        }

        @Override
        public void bindViews(Food object) {
            ButtonClickListener buttonClickListener = new ButtonClickListener();
            mDishesNameTextView.setText(object.getGoodsName());
            mDishesMoneyTextView.setText(mContext.getString(R.string.money_symbol) + object.getPrice());

            List<String> tagList = object.getTags();
            StringBuffer stringBuffer = new StringBuffer();
            if (tagList != null && tagList.size() > 0) {
                for (int i = 0; i < tagList.size(); i++) {
                    stringBuffer.append("#" + tagList.get(i) + " ");
                }
            }
            mDishesTypeTextView.setText(stringBuffer.toString());
            String allEat = object.getAllEat();
            if (Integer.parseInt(allEat) > 0) {
                mBuyPersonTextView.setText(allEat + mContext.getString(R.string.people_buyed));
            } else {
                mBuyPersonTextView.setText(R.string.waite_you_taste_first);
            }
            String imgUrl = object.getCoverImg();
            if (!StringUtils.isEmpty(imgUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mFoodHImageView, imgUrl);
            }
            mDishesNameTextView.setTag(object);
            int restStock = object.getRestStock();
            int selectNum = object.getSelectedOrderNum();
            int leftNum = object.getLeftNum();

            if (leftNum > 0) {//剩余份数大于0时才能购买
                mSurplusNumberTextView.setText(mContext.getString(R.string.remian_number) + leftNum + mContext.getString(R.string.part));
                mSurplusNumberTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
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
            } else {
                mSurplusNumberTextView.setText(R.string.sell_out);
                mSurplusNumberTextView.setTextColor(ResourceUtils.getColor(R.color.bg_gray_text));
                mReduceImageView.setVisibility(View.INVISIBLE);
                mBuyNumberTextView.setVisibility(View.INVISIBLE);
                mAddImageView.setVisibility(View.INVISIBLE);
                mAddImageView.setEnabled(false);
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
