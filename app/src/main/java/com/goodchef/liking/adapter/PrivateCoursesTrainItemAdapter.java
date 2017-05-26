package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesConfirmResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午10:16
 */
public class PrivateCoursesTrainItemAdapter extends BaseRecycleViewAdapter<PrivateCoursesTrainItemAdapter.PrivateCoursesTrainItemViewHolder, PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> {

    private Context mContext;

    public PrivateCoursesTrainItemAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected PrivateCoursesTrainItemViewHolder createViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_private_courses_train_item, parent, false);
        return new PrivateCoursesTrainItemViewHolder(itemView);
    }

    final static class PrivateCoursesTrainItemViewHolder extends BaseRecycleViewHolder<PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses> {
        TextView mTrainItemTextView;

        public PrivateCoursesTrainItemViewHolder(View itemView) {
            super(itemView);
            mTrainItemTextView = (TextView) itemView.findViewById(R.id.train_item_text);
        }

        @Override
        public void bindViews(PrivateCoursesConfirmResult.PrivateCoursesConfirmData.Courses object) {
            boolean isSelect = object.isSelect();
            if (isSelect) {
                mTrainItemTextView.setBackgroundResource(R.drawable.shape_train_item_green_back);
                mTrainItemTextView.setTextColor(ResourceUtils.getColor(R.color.white));
            } else {
                mTrainItemTextView.setBackgroundResource(R.drawable.shape_train_item_gray_back);
                mTrainItemTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            }
            String tranName = object.getName();
            int testLine = 6;
            StringBuilder sb = new StringBuilder(tranName);
            int tranNameLength = sb.length();
            if (tranNameLength <= testLine) {
                mTrainItemTextView.setText(tranName);
            } else if (tranNameLength > testLine) {
                int tranNameLine = tranNameLength % 2;
                if (tranNameLine == 0) {
                    int tran = sb.length() / 2;
                    sb.insert(tran, "\n");
                    mTrainItemTextView.setText(sb.toString());
                } else {
                    int tran = sb.length() / 2 +1;
                    sb.insert(tran, "\n");
                    mTrainItemTextView.setText(sb.toString());
                }
            }
            mTrainItemTextView.setTag(object);
        }
    }
}
