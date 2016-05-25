package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public LikingNearbyAdapter(Context context) {
        mContext = context;
    }

    public List<String> getData() {
        return mList;
    }

    public void setData(List<String> list) {
        mList = list;
    }

    @Override
    public LikingNearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_liking_nearby, parent, false);
        return new LikingNearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikingNearbyViewHolder holder, int position) {
        String str = mList.get(position);
        holder.mDishesMoneyTextView.setText("¥ " + str);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LikingNearbyViewHolder extends RecyclerView.ViewHolder {
        TextView mDishesMoneyTextView;//菜品价格

        public LikingNearbyViewHolder(View itemView) {
            super(itemView);

            mDishesMoneyTextView = (TextView) itemView.findViewById(R.id.dishes_money);
        }
    }
}
