package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/1
 * @Version 1.0
 */
public class MyPersonAdapter extends  BaseRecyclerAdapter<MyPersonAdapter.MyPersonEntity> {

    List<MyPersonEntity> myPersonEntities = new ArrayList<>();

    public MyPersonAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new MyPersonHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_group, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, MyPersonEntity data) {
        if (viewHolder instanceof MyPersonHolder) {
            MyPersonHolder holder = (MyPersonHolder) viewHolder;
            holder.bindViews(data);
        }
    }

    public static class MyPersonHolder extends BaseRecycleViewHolder<MyPersonEntity> {

        @BindView(R.id.item_icon)
        ImageView mIcon;

        @BindView(R.id.item_text)
        TextView mTextView;

        public MyPersonHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void bindViews(MyPersonEntity object) {
            mIcon.setImageResource(object.getDrawValue());
            mTextView.setText(object.getStringValue());
        }
    }


    public static class MyPersonEntity implements Serializable {
        private String stringValue;
        private int drawValue;

        public MyPersonEntity(String stringValue, int drawValue) {
            this.stringValue = stringValue;
            this.drawValue = drawValue;
        }

        public MyPersonEntity() {
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public int getDrawValue() {
            return drawValue;
        }

        public void setDrawValue(int drawValue) {
            this.drawValue = drawValue;
        }
    }

}
