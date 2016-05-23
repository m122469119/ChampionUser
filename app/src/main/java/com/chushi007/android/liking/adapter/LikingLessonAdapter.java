package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.chushi007.android.liking.R;

/**
 * 说明:首页listView适配器
 * Author shaozucheng
 * Time:16/5/23 上午11:27
 */
public class LikingLessonAdapter extends HBaseAdapter<String> {
    private Context mContext;

    public LikingLessonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected BaseViewHolder<String> createViewHolder() {
        return new LikingLessonViewHolder();
    }

    class LikingLessonViewHolder extends BaseViewHolder<String> {
        private View mRootView;
        private HImageView mHImageView;//底部图片
        private TextView mLessonNameTextView;//课程名称
        private TextView mLessonUseTextView;//课程用途
        private TextView mLessonTypeTextView;//课程类型
        private RelativeLayout mLessonTypeLayout;//课程类型布局
        private TextView mLessonTimeTextView;//课程时间
        private TextView mSurplusPersonTextView;//剩余名额
        private ImageView mImageView;
        private TextView mAddressTextView;//地址
        private TextView mDistanceTextView;//距离

        @Override
        public View inflateItemView() {
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_liking_lesson, null, false);
            mHImageView = (HImageView) mRootView.findViewById(R.id.lesson_image);
            mLessonNameTextView = (TextView) mRootView.findViewById(R.id.lesson_name);
            mLessonUseTextView = (TextView) mRootView.findViewById(R.id.lesson_use);
            mLessonTypeTextView = (TextView) mRootView.findViewById(R.id.lesson_type_text);
            mLessonTypeLayout = (RelativeLayout) mRootView.findViewById(R.id.layout_lesson_type);
            mLessonTimeTextView = (TextView) mRootView.findViewById(R.id.lesson_time);
            mSurplusPersonTextView = (TextView) mRootView.findViewById(R.id.surplus_person);
            mImageView = (ImageView) mRootView.findViewById(R.id.lesson_address_icon);
            mAddressTextView = (TextView) mRootView.findViewById(R.id.lesson_address);
            mDistanceTextView = (TextView) mRootView.findViewById(R.id.lesson_distance);


            return mRootView;
        }

        @Override
        public void bindViews(String object) {
            mDistanceTextView.setText(object + " km");
        }
    }
}
