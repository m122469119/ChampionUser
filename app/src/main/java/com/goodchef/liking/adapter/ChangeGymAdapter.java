package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.module.gym.details.ArenaActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.data.remote.retrofit.result.CheckGymListResult;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        return new ChangeGymViewHolder(view, mContext);
    }

    class ChangeGymViewHolder extends BaseRecycleViewHolder<CheckGymListResult.CheckGymData.CheckGym> {

//        HImageView mHImageView;
//        TextView mGymNameTextView;
//        TextView mOperatingTimeTextView;
//        TextView mGymAddressTextView;
//        TextView mGymDistanceTextView;
//        CheckBox mCheckBox;

        @BindView(R.id.gym_image)
        HImageView mGymImage;
        @BindView(R.id.gym_operating_time_TextView)
        TextView mGymOperatingTimeTextView;
        @BindView(R.id.gym_name)
        TextView mGymName;
        @BindView(R.id.gym_address)
        TextView mGymAddress;
        @BindView(R.id.gym_distance)
        TextView mGymDistance;
        @BindView(R.id.change_gym_checkBox)
        CheckBox mChangeGymCheckBox;

        public ChangeGymViewHolder(View itemView, Context context) {
            super(itemView, context);
            ButterKnife.bind(this, itemView);
            mGymImage.setOnClickListener(gymImagClickListener);
        }

        @Override
        public void bindViews(CheckGymListResult.CheckGymData.CheckGym object) {
            String imageUrl = object.getImg();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.loadImage(mGymImage, imageUrl, (Activity) mContext);
            }

            if (object.isSelect()) {
                mChangeGymCheckBox.setChecked(true);
            } else {
                mChangeGymCheckBox.setChecked(false);
            }

            if (object.isReCently() && object.islocation() && !StringUtils.isEmpty(object.getDistance())) {
                mGymDistance.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                mGymDistance.setText(mContext.getString(R.string.distance_recently) + object.getDistance());
            } else {
                mGymDistance.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mGymDistance.setText(object.getDistance());
            }

            if (!StringUtils.isEmpty(object.getGymTime())) {
                mGymOperatingTimeTextView.setVisibility(View.VISIBLE);
                mGymOperatingTimeTextView.setText(object.getGymTime());
            } else {
                mGymOperatingTimeTextView.setText("");
                mGymOperatingTimeTextView.setVisibility(View.INVISIBLE);
            }

            mGymName.setText(object.getGymName());
            mGymAddress.setText(object.getGymAddress());
            mGymName.setTag(object);
            mGymImage.setTag(object);
        }
    }

    private View.OnClickListener gymImagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckGymListResult.CheckGymData.CheckGym checkGym = (CheckGymListResult.CheckGymData.CheckGym) v.getTag();
            if (checkGym != null) {
                Intent intent = new Intent(mContext, ArenaActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, String.valueOf(checkGym.getGymId()));
                mContext.startActivity(intent);
            }
        }
    };
}
