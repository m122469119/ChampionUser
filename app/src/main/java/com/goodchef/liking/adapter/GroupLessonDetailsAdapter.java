package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class GroupLessonDetailsAdapter extends BaseRecycleViewAdapter<GroupLessonDetailsAdapter.GroupLessonDetailsViewHolder, GroupCoursesResult.GroupLessonData.GymImgsData> {

    private Context mContext;

    public GroupLessonDetailsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected GroupLessonDetailsViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson_details, parent, false);
        return new GroupLessonDetailsViewHolder(view, mContext);
    }

    public static class GroupLessonDetailsViewHolder extends BaseRecycleViewHolder<GroupCoursesResult.GroupLessonData.GymImgsData> {
        HImageView mHImageView;

        public GroupLessonDetailsViewHolder(View view, Context context) {
            super(view, context);
            mHImageView = (HImageView) itemView.findViewById(R.id.group_lesson_gym_image);
        }

        @Override
        public void bindViews(GroupCoursesResult.GroupLessonData.GymImgsData object) {
            String url = object.getUrl();
            if (!TextUtils.isEmpty(url)) {
                HImageLoaderSingleton.loadImage(mHImageView, url, (Activity) mContext);
            }
        }
    }
}
