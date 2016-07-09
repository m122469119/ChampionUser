package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CoursesResult;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 上午10:48
 */
public class LikingLessonRecyclerAdapter extends BaseRecycleViewAdapter<LikingLessonRecyclerAdapter.LessonViewHolder, CoursesResult.Courses.CoursesData> {
    public static final int TYPE_GROUP_LESSON = 1;//团体课
    public static final int TYPE_PRIVATE_LESSON = 2;//私教课

    private View.OnClickListener mClickListener;

    public void setGroupOnClickListener(View.OnClickListener listener) {
        this.mClickListener = listener;
    }

    class LessonViewHolder extends BaseRecycleViewHolder<CoursesResult.Courses.CoursesData> {
        private HImageView mHImageView;//底部图片
        private TextView mLessonNameTextView;//课程名称
        private TextView mLessonUseTextView;//课程用途
        private TextView mLessonTypeTextView;//课程类型
        private RelativeLayout mLessonTypeLayout;//课程类型布局
        private TextView mLessonTimeTextView;//课程时间
        private TextView mSurplusPersonTextView;//剩余名额
        private ImageView mImageView;
        private TextView mAddressTextView;//地址
        public TextView mDistanceTextView;
        private RelativeLayout mGroupLessonLayout;

        public LessonViewHolder(View itemView) {
            super(itemView);
            if (isHeaderHolder()) {
                return;
            }
            mHImageView = (HImageView) itemView.findViewById(R.id.lesson_image);
            mLessonNameTextView = (TextView) itemView.findViewById(R.id.lesson_name);
            mLessonUseTextView = (TextView) itemView.findViewById(R.id.lesson_use);
            mLessonTypeTextView = (TextView) itemView.findViewById(R.id.lesson_type_text);
            mLessonTypeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_lesson_type);
            mLessonTimeTextView = (TextView) itemView.findViewById(R.id.lesson_time);
            mSurplusPersonTextView = (TextView) itemView.findViewById(R.id.surplus_person);
            mImageView = (ImageView) itemView.findViewById(R.id.lesson_address_icon);
            mAddressTextView = (TextView) itemView.findViewById(R.id.lesson_address);
            mDistanceTextView = (TextView) itemView.findViewById(R.id.lesson_distance);
            mGroupLessonLayout = (RelativeLayout) itemView.findViewById(R.id.layout_group_lesson);
        }

        @Override
        public void bindViews(CoursesResult.Courses.CoursesData object) {
            if (object == null) {
                return;
            }
            List<String> imageList = object.getImgs();
            if (imageList != null && imageList.size() > 0) {
                String imageUrl = imageList.get(0);
                if (!StringUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
                }
            }
            int type = object.getType();
            if (type == TYPE_GROUP_LESSON) {
                mLessonTypeLayout.setBackgroundResource(R.drawable.icon_group_teach_lesson);
                mLessonTypeTextView.setText("团体课");
                mLessonTypeTextView.setTextColor(ResourceUtils.getColor(R.color.liking_lesson_group_text));
                mLessonNameTextView.setText(object.getCourseName());

                String courseDate = object.getCourseDate();
                if (!StringUtils.isEmpty(courseDate)) {
                    mLessonTimeTextView.setText(courseDate);
                }

                mImageView.setVisibility(View.VISIBLE);
                mAddressTextView.setText(object.getGymName());
                String distance = object.getDistance();
                if (!StringUtils.isEmpty(distance)) {
                    mDistanceTextView.setText(distance + " km");
                }
                mSurplusPersonTextView.setVisibility(View.VISIBLE);
                String quota = object.getQuota();
                if (StringUtils.isEmpty(quota)){
                    mSurplusPersonTextView.setText("");
                }else {
                    mSurplusPersonTextView.setText(quota);
                }
                mGroupLessonLayout.setOnClickListener(mClickListener);
                mGroupLessonLayout.setTag(object);
            } else if (type == TYPE_PRIVATE_LESSON) {
                mLessonTypeLayout.setBackgroundResource(R.drawable.icon_pivate_teach_lesson);
                mLessonTypeTextView.setText("私教课");
                mLessonTypeTextView.setTextColor(ResourceUtils.getColor(R.color.white));
                mLessonNameTextView.setText(object.getCourseName());
                mImageView.setVisibility(View.GONE);
                mAddressTextView.setText(object.getDescription());
                mDistanceTextView.setText("");
                mLessonTimeTextView.setText("");
                mSurplusPersonTextView.setVisibility(View.INVISIBLE);
                mGroupLessonLayout.setOnClickListener(null);
            }

            List<String> tagList = object.getTags();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < tagList.size(); i++) {
                stringBuffer.append(" #" + tagList.get(i));
            }
            mLessonUseTextView.setText(stringBuffer.toString());
        }
    }

    public LikingLessonRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected LessonViewHolder createHeaderViewHolder() {
        return new LessonViewHolder(getHeaderView());
    }

    @Override
    protected LessonViewHolder createViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_liking_lesson, parent, false);
        return new LessonViewHolder(itemView);
    }
}
