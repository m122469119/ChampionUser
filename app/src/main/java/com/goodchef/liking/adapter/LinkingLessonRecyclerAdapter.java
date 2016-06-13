package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CoursesResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 上午10:48
 */
public class LinkingLessonRecyclerAdapter extends RecyclerView.Adapter<LinkingLessonRecyclerAdapter.RecyclerViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;
    private OnItemClickListener mListener;


    public static final int TYPE_GROUP_LESSON = 1;//团体课
    public static final int TYPE_PRIVATE_LESSON = 2;//私教课
    private List<CoursesResult.Courses.CoursesData> mList = new ArrayList<>();
    private Context mContext;

    public LinkingLessonRecyclerAdapter(Context context) {
        mContext = context;
    }

    public List<CoursesResult.Courses.CoursesData> getData() {
        return mList;
    }

    public void setData(List<CoursesResult.Courses.CoursesData> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new RecyclerViewHolder(mHeaderView);
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_liking_lesson, parent, false);
        return new RecyclerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        final CoursesResult.Courses.CoursesData coursesData = mList.get(pos);
        if (holder instanceof RecyclerViewHolder) {
            List<String> imageList = coursesData.getImgs();
            if (imageList != null && imageList.size() > 0) {
                String imageUrl = coursesData.getImgs().get(0);
                if (!StringUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.getInstance().requestImage(holder.mHImageView, imageUrl);
                }
            }
            int type = coursesData.getType();
            if (type == TYPE_GROUP_LESSON) {
                holder.mLessonTypeLayout.setBackgroundResource(R.drawable.icon_group_teach_lesson);
                holder.mLessonTypeTextView.setText("团体课");
                holder.mLessonTypeTextView.setTextColor(ResourceUtils.getColor(R.color.liking_lesson_group_text));
                holder.mLessonNameTextView.setText(coursesData.getCourseName());

                String courseDate = coursesData.getCourseDate();
                if (!StringUtils.isEmpty(courseDate)) {
                    holder.mLessonTimeTextView.setText(courseDate);
                }

                holder.mImageView.setVisibility(View.VISIBLE);
                holder.mAddressTextView.setText(coursesData.getGymAddress());
                String distance = coursesData.getDistance();
                if (!StringUtils.isEmpty(distance)) {
                    holder.mDistanceTextView.setText(distance + " km");
                }
                holder.mSurplusPersonTextView.setVisibility(View.VISIBLE);
                holder.mSurplusPersonTextView.setText(coursesData.getQuota());
            } else if (type == TYPE_PRIVATE_LESSON) {
                holder.mLessonTypeLayout.setBackgroundResource(R.drawable.icon_pivate_teach_lesson);
                holder.mLessonTypeTextView.setText("私教课");
                holder.mLessonTypeTextView.setTextColor(ResourceUtils.getColor(R.color.white));
                holder.mLessonNameTextView.setText(coursesData.getName());
                holder.mImageView.setVisibility(View.GONE);
                holder.mAddressTextView.setText(coursesData.getDescription());
                holder.mDistanceTextView.setText("");
                holder.mLessonTimeTextView.setText("");
                holder.mSurplusPersonTextView.setVisibility(View.INVISIBLE);
            }

            List<String> tagList = coursesData.getTags();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < tagList.size(); i++) {
                stringBuffer.append("#" + tagList.get(i));
            }
            holder.mLessonUseTextView.setText(stringBuffer.toString());

            if (mListener == null) return;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, coursesData);
                }
            });
        }

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mList.size() : mList.size() + 1;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
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

        public RecyclerViewHolder(View itemView) {
            super(itemView);
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
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CoursesResult.Courses.CoursesData data);
    }
}
