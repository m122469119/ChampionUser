package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.CityData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/18 下午6:32
 */
public class ChangeGymCityAdapter extends BaseRecycleViewAdapter<ChangeGymCityAdapter.ChangeGymViewHolder, CityData> {


    private Context mContext;

    public ChangeGymCityAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected ChangeGymViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected ChangeGymViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gym_city, parent, false);
        return new ChangeGymViewHolder(view);
    }

    public class ChangeGymViewHolder extends BaseRecycleViewHolder<CityData> {

        TextView mCityTextView;

        public ChangeGymViewHolder(View itemView) {
            super(itemView);
            mCityTextView = (TextView) itemView.findViewById(R.id.city_name);
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
