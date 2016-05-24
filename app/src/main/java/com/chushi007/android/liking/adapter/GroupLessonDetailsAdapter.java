package com.chushi007.android.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chushi007.android.liking.R;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午4:25
 */
public class GroupLessonDetailsAdapter extends RecyclerView.Adapter<GroupLessonDetailsAdapter.GroupLessonDetailsViewHolder> {

    private Context mContext;

    private List<String> mList;

    public GroupLessonDetailsAdapter(Context context) {
        mContext = context;
    }

    public List<String> getData() {
        return mList;
    }

    public void setData(List<String> data) {
        this.mList = data;
    }

    @Override
    public GroupLessonDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson_details, parent, false);
        return new GroupLessonDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupLessonDetailsViewHolder holder, int position) {
        String str = mList.get(position);
        holder.mTextView.setText("有养区" + str);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class GroupLessonDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public GroupLessonDetailsViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.motor_area);
        }
    }
}
