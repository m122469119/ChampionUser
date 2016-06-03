package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:31
 */
public class PrivateLessonAdapter extends HBaseAdapter<String> {
    private Context mContext;

    public PrivateLessonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected BaseViewHolder<String> createViewHolder() {
        return new PrivateLessonViewHolder();
    }

    class PrivateLessonViewHolder extends BaseViewHolder {
        View mRootView;
        TextView mTeacherNameTextView;

        @Override
        public View inflateItemView() {
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_private_my_lesson, null, false);
            mTeacherNameTextView = (TextView) mRootView.findViewById(R.id.private_teacher_name);
            return mRootView;
        }

        @Override
        public void bindViews(Object object) {
            mTeacherNameTextView.setText("Jack");
        }
    }
}
