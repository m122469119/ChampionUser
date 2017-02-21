package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.City;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:14
 * version 1.0.0
 */

public class ChangeCityAdapter extends BaseRecycleViewAdapter<ChangeCityAdapter.ChangeCityViewHolder, City.RegionsData.CitiesData> {


    private Context mContext;

    public ChangeCityAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void setData(List<City.RegionsData.CitiesData> list) {
        super.setData(list);
        if (mOnDataIsNullListener != null) {
            if (list == null || list.size() == 0){
                mOnDataIsNullListener.onDataIsNull(true);
            } else {
                mOnDataIsNullListener.onDataIsNull(false);
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;
    private OnDataIsNullListener mOnDataIsNullListener;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public interface OnDataIsNullListener{
        void onDataIsNull(boolean isNull);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnDataIsNullListener(OnDataIsNullListener mOnDataIsNullListener) {
        this.mOnDataIsNullListener = mOnDataIsNullListener;
    }

    @Override
    protected ChangeCityViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_change_city, null, false);
        return new ChangeCityViewHolder(view);
    }

    public class ChangeCityViewHolder extends BaseRecycleViewHolder<City.RegionsData.CitiesData> {
        @BindView(R.id.city_name_TextView)
        TextView mCityNameTextView;

        public ChangeCityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindViews(City.RegionsData.CitiesData object) {
            mCityNameTextView.setText(object.getCityName());
        }

        @OnClick({R.id.city_name_TextView})
        public void onViewClick(View view){
            switch (view.getId()){
                case R.id.city_name_TextView:
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(mCityNameTextView, mPosition);
                    }
                    break;
            }
        }
    }
}
