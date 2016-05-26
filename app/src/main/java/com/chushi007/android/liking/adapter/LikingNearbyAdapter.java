package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chushi007.android.liking.R;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/25 下午2:33
 */
public class LikingNearbyAdapter extends RecyclerView.Adapter<LikingNearbyAdapter.LikingNearbyViewHolder> {

    private Context mContext;
    private List<String> mList;

    private OnItemClickListener mListener;

    public LikingNearbyAdapter(Context context) {
        mContext = context;
    }

    public List<String> getData() {
        return mList;
    }

    public void setData(List<String> list) {
        mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public LikingNearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_liking_nearby, parent, false);
        return new LikingNearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikingNearbyViewHolder holder, final int position) {
        final String str = mList.get(position);
        if (holder instanceof LikingNearbyViewHolder) {
            holder.mDishesMoneyTextView.setText("¥ " + str);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position, str);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LikingNearbyViewHolder extends RecyclerView.ViewHolder {
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
            mDishesNameTextView = (TextView) itemView.findViewById(R.id.dishes_name);
            mSurplusNumberTextView = (TextView) itemView.findViewById(R.id.surplus_number);
            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.dishes_money);
            mDishesTypeTextView = (TextView) itemView.findViewById(R.id.dishes_type);
            mBuyPersonTextView = (TextView) itemView.findViewById(R.id.buy_person);
            mBuyNumberTextView = (TextView) itemView.findViewById(R.id.food_buy_number);
            mReduceImageView = (ImageView) itemView.findViewById(R.id.reduce_image);
            mAddImageView = (ImageView) itemView.findViewById(R.id.add_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
}
