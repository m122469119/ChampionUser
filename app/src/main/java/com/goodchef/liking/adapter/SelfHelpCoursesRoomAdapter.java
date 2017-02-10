package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;

/**
 * 说明:
 * Author : liking
 * Time: 下午11:07
 */

public class SelfHelpCoursesRoomAdapter extends BaseRecycleViewAdapter<SelfHelpCoursesRoomAdapter.SelfHelpCoursesRoomViewHolder, SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> {


    private Context mContext;
    private View.OnClickListener mSelectRoomOnClickListener;

    private View.OnClickListener mSelectRoomJoinClickListener;

    public SelfHelpCoursesRoomAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setSelectRoomOnClickListener(View.OnClickListener roomOnClickListener) {
        this.mSelectRoomOnClickListener = roomOnClickListener;
    }

    public void setSelectRoomJoinClickListener(View.OnClickListener selectRoomJoinClickListener) {
        mSelectRoomJoinClickListener = selectRoomJoinClickListener;
    }
    @Override
    protected SelfHelpCoursesRoomViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_self_help_room, parent, false);
        return new SelfHelpCoursesRoomViewHolder(view);
    }

    class SelfHelpCoursesRoomViewHolder extends BaseRecycleViewHolder<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> {
        TextView mRoomNameTextView;
        TextView mGymRoomOrderPromptTextView;//是否被预定提示
        CheckBox mGymCheckBox;
        TextView mSurplusNumberTextView;//剩余名额
        TextView mJoinCoursesTextView;//加入课程
        ImageView mImageView;
        ImageView mLineImageView;
        RelativeLayout mSelectRoomRelativeLayout;

        public SelfHelpCoursesRoomViewHolder(View itemView) {
            super(itemView);
            mRoomNameTextView = (TextView) itemView.findViewById(R.id.gym_room_name_TextView);
            mGymRoomOrderPromptTextView = (TextView) itemView.findViewById(R.id.gym_room_order_prompt);
            mGymCheckBox = (CheckBox) itemView.findViewById(R.id.gym_room_checkBox);
            mSurplusNumberTextView = (TextView) itemView.findViewById(R.id.surplus_number_TextView);
            mJoinCoursesTextView = (TextView) itemView.findViewById(R.id.join_courses_TextView);
            mImageView = (ImageView) itemView.findViewById(R.id.self_help_image_view);
            mLineImageView = (ImageView) itemView.findViewById(R.id.self_help_image_view_line);
            mSelectRoomRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_self_help_select_room);
        }

        @Override
        public void bindViews(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData object) {
            boolean ismScheduled = object.isScheduled();
            if (ismScheduled) {//已有排课
                mGymCheckBox.setVisibility(View.GONE);
                mSurplusNumberTextView.setVisibility(View.VISIBLE);
                mSurplusNumberTextView.setText(mContext.getString(R.string.remain) + object.getQuota() + mContext.getString(R.string.number_people));
                mJoinCoursesTextView.setVisibility(View.VISIBLE);
                mGymRoomOrderPromptTextView.setVisibility(View.VISIBLE);
                mGymRoomOrderPromptTextView.setText(mContext.getString(R.string.hased) + object.getScheduleName() + ")");
                mImageView.setVisibility(View.GONE);
                mLineImageView.setVisibility(View.VISIBLE);
                if(mSelectRoomJoinClickListener != null){
                    mJoinCoursesTextView.setOnClickListener(mSelectRoomJoinClickListener);
                    mJoinCoursesTextView.setTag(object);
                }
            } else {//没有排课
                mGymCheckBox.setVisibility(View.VISIBLE);
                boolean isCheck = object.isCheck();
                if (isCheck) {
                    mGymCheckBox.setChecked(true);
                } else {
                    mGymCheckBox.setChecked(false);
                }
                mSurplusNumberTextView.setVisibility(View.GONE);
                mJoinCoursesTextView.setVisibility(View.GONE);
                mGymRoomOrderPromptTextView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mLineImageView.setVisibility(View.GONE);
                if (mSelectRoomOnClickListener != null) {
                    mSelectRoomRelativeLayout.setOnClickListener(mSelectRoomOnClickListener);
                    mSelectRoomRelativeLayout.setTag(object);
                }
            }
            mRoomNameTextView.setText(object.getName());
        }
    }
}
