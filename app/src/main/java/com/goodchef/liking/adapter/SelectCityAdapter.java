package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.CityData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 下午2:21
 */
public class SelectCityAdapter extends HBaseAdapter<CityData> {

    private Context mContext;

    private View.OnClickListener mClickListener;

    public SelectCityAdapter(Context context, View.OnClickListener listener) {
        super(context);
        this.mContext = context;
        this.mClickListener = listener;
    }

    @Override
    protected BaseViewHolder<CityData> createViewHolder() {
        return new CityDataViewHolder();
    }

    public class CityDataViewHolder extends BaseViewHolder<CityData> {
        TextView mCityTextView;
        CheckBox mCheckBox;
        RelativeLayout mLayout;

        @Override
        public View inflateItemView() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_city, null, false);
            mCityTextView = (TextView) view.findViewById(R.id.tv_city);
            mCheckBox = (CheckBox) view.findViewById(R.id.select_city_checkBox);
            mLayout = (RelativeLayout)view.findViewById(R.id.layout_city);
            return view;
        }

        @Override
        public void bindViews(CityData object) {
            boolean isSelect = object.isSelct();
            if (isSelect) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
            mCityTextView.setText(object.getCityName());
            mLayout.setOnClickListener(mClickListener);
            mLayout.setTag(object);
        }
    }
}
