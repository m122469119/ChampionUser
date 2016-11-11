package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        private TextView mGroupCoursesStrongTextView;
        private TextView mCoursesIntroduceTextView;
        private LinearLayout mLinearLayout;

        public SelectCoursesListViewHolder(View itemView) {
            super(itemView);
            mSelfGymHImageView = (HImageView) itemView.findViewById(R.id.self_help_gym_image);
            mCoursesTrainTextView = (TextView) itemView.findViewById(R.id.group_courses_train_object);
            mGroupCoursesStrongTextView = (TextView) itemView.findViewById(R.id.group_courses_strong);
            mCoursesIntroduceTextView = (TextView) itemView.findViewById(R.id.courses_list_introduce);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_courses);
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
            mCoursesTrainTextView.setText(object.getName());
            mCoursesIntroduceTextView.setText(object.getDesc());
            if (mOnClickListener != null) {
                mLinearLayout.setOnClickListener(mOnClickListener);
                mLinearLayout.setTag(object);
            }
        }
    }
}
