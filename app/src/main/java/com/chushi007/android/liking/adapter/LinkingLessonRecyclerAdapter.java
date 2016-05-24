package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.library.imageloader.HImageView;
import com.chushi007.android.liking.R;

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

    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public LinkingLessonRecyclerAdapter(Context context) {
        mContext = context;
    }

    public List<String> getData() {
        return mList;
    }

    public void setData(List<String> list) {
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
        final String str = mList.get(pos);
        if (holder instanceof RecyclerViewHolder) {
            int back = Integer.parseInt(str);
            if ((back % 2) == 0) {
                holder.mLessonTypeLayout.setBackgroundResource(R.drawable.icon_group_teach_lesson);
                holder.mLessonTypeTextView.setText("团体课");
            } else {
                holder.mLessonTypeLayout.setBackgroundResource(R.drawable.icon_pivate_teach_lesson);
                holder.mLessonTypeTextView.setText("私教课");
            }
            holder.mDistanceTextView.setText(str + " km");


            if (mListener == null) return;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, str);
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
        void onItemClick(int position, String data);
    }
}
