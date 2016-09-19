package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.CityData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/18 下午6:32
 */
public class ChangeGymCityAdapter extends HBaseAdapter<CityData> {
    private Context mContext;

    @Override
    protected BaseViewHolder<CityData> createViewHolder() {
        return new ChangeGymViewHolder();
    }

    public ChangeGymCityAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public class ChangeGymViewHolder extends BaseViewHolder<CityData> {

        TextView mCityTextView;
        @Override
        public View inflateItemView() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_gym_city, null, false);
            mCityTextView = (TextView) view.findViewById(R.id.city_name);
            return view;
        }

        @Override
        public void bindViews(CityData object) {
            boolean isSelect = object.isSelct();
            if (isSelect) {
                mCityTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            } else {
                mCityTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
            }
            mCityTextView.setText(object.getCityName());
            mCityTextView.setTag(object);
        }
    }
}
