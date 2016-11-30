package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.PrivateCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 下午4:34
 */
public class TrainItemAdapter extends BaseRecycleViewAdapter<TrainItemAdapter.TrainViewHolder, PrivateCoursesResult.PrivateCoursesData.CoursesData> {

    private Context mContext;

    public TrainItemAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected TrainViewHolder createViewHolder(ViewGroup parent) {
        View itemViw = LayoutInflater.from(mContext).inflate(R.layout.item_train_couses, parent, false);
        return new TrainViewHolder(itemViw);
    }

    class TrainViewHolder extends BaseRecycleViewHolder<PrivateCoursesResult.PrivateCoursesData.CoursesData> {

        TextView mTrainNameTextView;

        public TrainViewHolder(View itemView) {
            super(itemView);
            mTrainNameTextView = (TextView) itemView.findViewById(R.id.train_name);
        }

        @Override
        public void bindViews(PrivateCoursesResult.PrivateCoursesData.CoursesData object) {
            String tranName = object.getName();
            int testLine = 6;
            StringBuilder sb = new StringBuilder(tranName);
            int tranNameLength = sb.length();
            if (tranNameLength <= testLine) {
                mTrainNameTextView.setText(tranName);
            } else if (tranNameLength > testLine) {
                int tranNameLine = tranNameLength % 2;
                if (tranNameLine == 0) {
                    int tran = sb.length() / 2;
                    sb.insert(tran, "\n");
                    mTrainNameTextView.setText(sb.toString());
                } else {
                    int tran = sb.length() / 2 + 1;
                    sb.insert(tran, "\n");
                    mTrainNameTextView.setText(sb.toString());
                }
            }
        }
    }
}
