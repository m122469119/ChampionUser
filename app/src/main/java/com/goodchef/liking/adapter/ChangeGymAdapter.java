package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CheckGymListResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/14 下午5:41
 */
public class ChangeGymAdapter extends BaseRecycleViewAdapter<ChangeGymAdapter.ChangeGymViewHolder, CheckGymListResult.CheckGymData.CheckGym> {

    private Context mContext;

    public ChangeGymAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected ChangeGymViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_change_gym, parent, false);
        return new ChangeGymViewHolder(view);
    }

    class ChangeGymViewHolder extends BaseRecycleViewHolder<CheckGymListResult.CheckGymData.CheckGym> {

        HImageView mHImageView;
        TextView mGymNameTextView;
        TextView mGymAddressTextView;
        TextView mGymDistanceTextView;
        CheckBox mCheckBox;

        public ChangeGymViewHolder(View itemView) {
            super(itemView);
            mHImageView = (HImageView) itemView.findViewById(R.id.gym_image);
            mGymNameTextView = (TextView) itemView.findViewById(R.id.gym_name);
            mGymAddressTextView = (TextView) itemView.findViewById(R.id.gym_address);
            mGymDistanceTextView = (TextView) itemView.findViewById(R.id.gym_distance);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.change_gym_checkBox);
        }

        @Override
        public void bindViews(CheckGymListResult.CheckGymData.CheckGym object) {
            String imageUrl = object.getImg();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mHImageView, imageUrl);
            }

            if (object.isSelect()) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }

            if (object.isReCently() && object.islocation()) {
                mGymDistanceTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                mGymDistanceTextView.setText("距您最近 " + object.getDistance());
            } else {
                mGymDistanceTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mGymDistanceTextView.setText(object.getDistance());
            }

            mGymNameTextView.setText(object.getGymName());
            mGymAddressTextView.setText(object.getGymAddress());
            mGymNameTextView.setTag(object);
        }
    }
}
