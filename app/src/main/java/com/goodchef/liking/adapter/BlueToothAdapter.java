package com.goodchef.liking.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:43
 * version 1.0.0
 */

public class BlueToothAdapter extends BaseRecycleViewAdapter<BlueToothAdapter.BlueToothViewHolder, BluetoothDevice> {

    private View.OnClickListener mOnClickListener;

    public BlueToothAdapter(Context context) {
        super(context);
    }

    public void setOnClickListenr(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    @Override
    protected BlueToothViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_blue_tooth, parent, false);
        return new BlueToothViewHolder(view, getContext());
    }

    class BlueToothViewHolder extends BaseRecycleViewHolder<BluetoothDevice> {
        @BindView(R.id.blue_tooth_name_TextView)
        TextView mBlueToothNameTextView;
        @BindView(R.id.connect_blue_tooth_TextView)
        TextView mConnectBlueToothTextView;

        public BlueToothViewHolder(View itemView, Context context) {
            super(itemView, context);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(BluetoothDevice object) {
            mBlueToothNameTextView.setText(object.getName());
            if (mOnClickListener != null) {
                mConnectBlueToothTextView.setOnClickListener(mOnClickListener);
                mConnectBlueToothTextView.setTag(object);
            }
        }
    }
}
