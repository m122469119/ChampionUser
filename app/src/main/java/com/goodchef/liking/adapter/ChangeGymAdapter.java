package com.goodchef.liking.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.goodchef.liking.activity.ArenaActivity;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CheckGymListResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/14 下午5:41
 */
public class ChangeGymAdapter extends BaseRecycleViewAdapter<ChangeGymAdapter.ChangeGymViewHolder, CheckGymListResult.CheckGymData.CheckGym> {

    private Context mContext;
    private StringBuilder sb = null;
    private SimpleDateFormat sdf = null;

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
        TextView mOperatingTimeTextView;
        TextView mGymAddressTextView;
        TextView mGymDistanceTextView;
        CheckBox mCheckBox;

        public ChangeGymViewHolder(View itemView) {
            super(itemView);
            mHImageView = (HImageView) itemView.findViewById(R.id.gym_image);
            mGymNameTextView = (TextView) itemView.findViewById(R.id.gym_name);
            mOperatingTimeTextView = (TextView) itemView.findViewById(R.id.gym_operating_time_TextView);
            mGymAddressTextView = (TextView) itemView.findViewById(R.id.gym_address);
            mGymDistanceTextView = (TextView) itemView.findViewById(R.id.gym_distance);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.change_gym_checkBox);
            mHImageView.setOnClickListener(gymImagClickListener);
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

            if (object.isReCently() && object.islocation() && !StringUtils.isEmpty(object.getDistance())) {
                mGymDistanceTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                mGymDistanceTextView.setText("距您最近 " + object.getDistance());
            } else {
                mGymDistanceTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mGymDistanceTextView.setText(object.getDistance());
            }

            if(!StringUtils.isEmpty(object.getOpenTime()) && !StringUtils.isEmpty(object.getCloseTime())) {
                mOperatingTimeTextView.setVisibility(View.VISIBLE);
                mOperatingTimeTextView.setText(getOperatingTime(object.getOpenTime(), object.getCloseTime()));
            } else {
                mOperatingTimeTextView.setText("");
                mOperatingTimeTextView.setVisibility(View.INVISIBLE);
            }

            mGymNameTextView.setText(object.getGymName());
            mGymAddressTextView.setText(object.getGymAddress());
            mGymNameTextView.setTag(object);
            mHImageView.setTag(object);
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

    private String getOperatingTime(String openTime, String closeTime) {
        StringBuilder sb = getStringBuilder();
        try {
            if (isAllDay(openTime, closeTime)) {
                sb.append("24小时");
            } else {
                sb.append(getAmPm(openTime));
                sb.append(" - ");
                sb.append(getAmPm(closeTime));
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public boolean isAllDay(String openTime, String closeTime) {
        try {
            Date openDate = getSimpleDateFormat().parse(openTime);
            Date closeDate = getSimpleDateFormat().parse(closeTime);
            long diff = closeDate.getTime() - openDate.getTime();
            return diff > (23 * 60 + 30) * 60 * 1000; //大于12:30 营业
        } catch (Exception e) {
        }
        return false;
    }

    public String getAmPm(String hour) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getSimpleDateFormat().parse(hour));
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            return calendar.get(Calendar.AM_PM) == Calendar.AM ? h + "AM" :  h + "PM";
        } catch (ParseException e) {}
        return "";
    }

    private StringBuilder getStringBuilder() {
        if(sb == null) {
            sb = new StringBuilder();
        }
        sb.setLength(0);
        return sb;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        if(sdf == null) {
            sdf = new SimpleDateFormat("HH:mm");
        }
        return sdf;
    }


}
