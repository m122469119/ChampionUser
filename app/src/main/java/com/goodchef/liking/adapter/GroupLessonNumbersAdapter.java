package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.GroupCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午4:25
 */
public class GroupLessonNumbersAdapter extends BaseRecycleViewAdapter<GroupLessonNumbersAdapter.GroupLessonNumbersViewHolder, GroupCoursesResult.GroupLessonData.GymNumbersData> {

    public static final int SEX_KEY_WOMAN = 0;//女

    public static final int SEX_KEY_MAN = 1;//男

    private Context mContext;

    public GroupLessonNumbersAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected GroupLessonNumbersViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson_numbers, parent, false);
        return new GroupLessonNumbersViewHolder(view, mContext);
    }

    public static class GroupLessonNumbersViewHolder extends BaseRecycleViewHolder<GroupCoursesResult.GroupLessonData.GymNumbersData> {
        HImageView mHImageView;
        TextView mUserName;
        TextView mUserPhone;
        TextView mJoinTime;

        public GroupLessonNumbersViewHolder(View itemView, Context context) {
            super(itemView, context);
            mHImageView = (HImageView) itemView.findViewById(R.id.group_lesson_gym_head_image);
            mUserName = (TextView) itemView.findViewById(R.id.group_lesson_gym_person_name);
            mUserPhone = (TextView) itemView.findViewById(R.id.group_lesson_gym_person_phone);
            mJoinTime = (TextView) itemView.findViewById(R.id.group_lesson_gym_join_time);

        }

        @Override
        public void bindViews(GroupCoursesResult.GroupLessonData.GymNumbersData object) {
            String url = object.getAvatar();
            if (!TextUtils.isEmpty(url)) {
                HImageLoaderSingleton.loadImage(mHImageView, url, (Activity) mContext);
            }
            mUserName.setText(object.getName());
            mJoinTime.setText(object.getTime());
            mUserPhone.setText(object.getPhone());
            if(SEX_KEY_WOMAN == object.getGender()) {
                mUserName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_women,0);
            }else {
                mUserName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_man,0);
            }

        }

    }
}
