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

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/25 下午2:33
 */
public class LikingNearbyAdapter extends BaseRecycleViewAdapter<LikingNearbyAdapter.LikingNearbyViewHolder, FoodListResult.FoodData.Food> {

    private Context mContext;
    public View.OnClickListener addListener;
    public View.OnClickListener reduceListener;

    public LikingNearbyAdapter(Context context) {
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
    protected LikingNearbyViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected LikingNearbyViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_liking_nearby, parent, false);
        ImageView mReduceImageView = (ImageView) view.findViewById(R.id.reduce_image);
        ImageView mAddImageView = (ImageView) view.findViewById(R.id.add_image);
        if (addListener != null) {
            mAddImageView.setOnClickListener(addListener);
        }
        if (reduceListener != null) {
            mReduceImageView.setOnClickListener(reduceListener);
        }
        return new LikingNearbyViewHolder(view);
    }


    public static class LikingNearbyViewHolder extends BaseRecycleViewHolder<FoodListResult.FoodData.Food> {
        HImageView mFoodHImageView;
        TextView mDishesNameTextView;//菜品名称
        TextView mSurplusNumberTextView;//剩余份数
        TextView mDishesMoneyTextView;//菜品价格
        TextView mDishesTypeTextView;//菜品标签
        TextView mBuyPersonTextView;//购买过的人数
        TextView mBuyNumberTextView;//购买的数量
        ImageView mReduceImageView;//减按钮
        ImageView mAddImageView;//加按钮

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
        }

        @Override
        public void bindViews(FoodListResult.FoodData.Food object) {
            mDishesNameTextView.setText(object.getGoodsName());
            mDishesMoneyTextView.setText("¥ " + object.getPrice());
            mSurplusNumberTextView.setText("今日还剩" + object.getLeftNum() + "份");
            List<String> tagList = object.getTags();
            StringBuffer stringBuffer = new StringBuffer();
            if (tagList != null && tagList.size() > 0) {
                for (int i = 0; i < tagList.size(); i++) {
                    stringBuffer.append("#" + tagList.get(i) + " ");
                }
            }
            mDishesTypeTextView.setText(stringBuffer.toString());
            mBuyPersonTextView.setText(object.getAllEat() + "人购买过");
            String imgUrl = object.getCoverImg();
            if (!StringUtils.isEmpty(imgUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mFoodHImageView, imgUrl);
            }
        }
    }


}
