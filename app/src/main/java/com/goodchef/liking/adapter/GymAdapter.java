package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.GymData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 下午4:17
 */
public class GymAdapter extends BaseRecycleViewAdapter<GymAdapter.GymViewHolder, GymData> {

    private Context mContext;

    public GymAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected GymViewHolder createViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_gym, parent, false);
        return new GymViewHolder(itemView);
    }

    public class GymViewHolder extends BaseRecycleViewHolder<GymData> {

        TextView mGymNameTextView;

        public GymViewHolder(View itemView) {
            super(itemView);
            mGymNameTextView = (TextView) itemView.findViewById(R.id.gym_name);
        }

        @Override
        public void bindViews(GymData object) {
            mGymNameTextView.setText(object.getName());
        }
    }
}
