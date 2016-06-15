package com.goodchef.liking.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午4:25
 */
public class GroupLessonDetailsAdapter extends BaseRecycleViewAdapter<GroupLessonDetailsAdapter.GroupLessonDetailsViewHolder, String> {

    private Context mContext;

    public GroupLessonDetailsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected GroupLessonDetailsViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected GroupLessonDetailsViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson_details, parent, false);
        return new GroupLessonDetailsViewHolder(view);
    }

    public static class GroupLessonDetailsViewHolder extends BaseRecycleViewHolder<String> {
        HImageView mHImageView;

        public GroupLessonDetailsViewHolder(View itemView) {
            super(itemView);
            mHImageView = (HImageView) itemView.findViewById(R.id.group_lesson_gym_image);
        }

        @Override
        public void bindViews(String object) {
            if (!TextUtils.isEmpty(object)) {
                HImageLoaderSingleton.getInstance().requestImage(mHImageView, object);
            }
        }
    }
}
