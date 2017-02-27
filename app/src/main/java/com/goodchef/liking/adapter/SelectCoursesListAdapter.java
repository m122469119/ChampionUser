package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:11
 */

public class SelectCoursesListAdapter extends BaseRecycleViewAdapter<SelectCoursesListAdapter.SelectCoursesListViewHolder, SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData> {

    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public SelectCoursesListAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setViewOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    @Override
    protected SelectCoursesListViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_select_courses_list, parent, false);
        return new SelectCoursesListViewHolder(view);
    }

    class SelectCoursesListViewHolder extends BaseRecycleViewHolder<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData> {
        private HImageView mSelfGymHImageView;
        private TextView mCoursesTrainTextView;
        private TextView mGroupCoursesDurationTextView;
        private TextView mCoursesIntroduceTextView;
        private LinearLayout mLinearLayout;
        private CheckBox mCoursesCheckBox;
        private LinearLayout mSelectLayout;

        public SelectCoursesListViewHolder(View itemView) {
            super(itemView);
            mSelfGymHImageView = (HImageView) itemView.findViewById(R.id.self_help_gym_image);
            mCoursesTrainTextView = (TextView) itemView.findViewById(R.id.group_courses_train_object);
            mGroupCoursesDurationTextView = (TextView) itemView.findViewById(R.id.group_courses_duration);
            mCoursesIntroduceTextView = (TextView) itemView.findViewById(R.id.courses_list_introduce);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_courses);
            mCoursesCheckBox = (CheckBox) itemView.findViewById(R.id.select_courses_checkbox);
            mSelectLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_courses_checkbox);
        }

        @Override
        public void bindViews(SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData object) {
            List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData.ImgData> imageUrlList = object.getImg();
            if (imageUrlList != null && imageUrlList.size() > 0) {
                String imageUrl = imageUrlList.get(0).getUrl();
                if (!StringUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.getInstance().loadImage(mSelfGymHImageView, imageUrl);
                } else {
                    HImageLoaderSingleton.getInstance().loadImage(mSelfGymHImageView, "");
                }
            }
            String duration = "";
            try{
                duration = Integer.parseInt(object.getVideoDuration()) / 60 + mContext.getString(R.string.min);
            }catch (Exception e){
            }
            mGroupCoursesDurationTextView.setText(mContext.getString(R.string.self_courses_time)+ duration);
            mCoursesTrainTextView.setText(object.getName());
            mCoursesIntroduceTextView.setText(object.getDesc());
            mCoursesCheckBox.setChecked(object.isSelect());
            if (mOnClickListener != null) {
                mSelectLayout.setOnClickListener(mOnClickListener);
                mSelectLayout.setTag(object);
                mLinearLayout.setOnClickListener(mOnClickListener);
                mLinearLayout.setTag(object);
            }
        }
    }
}
