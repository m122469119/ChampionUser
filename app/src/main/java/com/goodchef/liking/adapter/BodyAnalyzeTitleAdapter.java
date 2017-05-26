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
import com.goodchef.liking.data.remote.retrofit.result.BodyModelNavigationResult;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:48
 * version 1.0.0
 */

public class BodyAnalyzeTitleAdapter extends BaseRecycleViewAdapter<BodyAnalyzeTitleAdapter.BodyAnalyzeTitleViewHolder, BodyModelNavigationResult.HistoryTitleData.NavData> {

    private Context mContext;
    private View.OnClickListener mClickListener;

    public BodyAnalyzeTitleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    protected BodyAnalyzeTitleViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_body_analyze_title, parent, false);
        return new BodyAnalyzeTitleViewHolder(view);
    }

    class BodyAnalyzeTitleViewHolder extends BaseRecycleViewHolder<BodyModelNavigationResult.HistoryTitleData.NavData> {
        TextView mBodyAnalyzeTextView;

        public BodyAnalyzeTitleViewHolder(View itemView) {
            super(itemView);
            mBodyAnalyzeTextView = (TextView) itemView.findViewById(R.id.body_analyze_title_TextView);
        }

        @Override
        public void bindViews(BodyModelNavigationResult.HistoryTitleData.NavData object) {
            boolean isselect = object.isSelect();
            if (isselect) {
                mBodyAnalyzeTextView.setTextColor(ResourceUtils.getColor(R.color.white));
                mBodyAnalyzeTextView.setBackground(ResourceUtils.getDrawable(R.drawable.shape_radius_green_body_title));
            } else {
                mBodyAnalyzeTextView.setTextColor(ResourceUtils.getColor(R.color.c7c8289));
                mBodyAnalyzeTextView.setBackground(ResourceUtils.getDrawable(R.drawable.shape_radius_gray_body_title));
            }
            mBodyAnalyzeTextView.setText(object.getChineseName());
            if (mClickListener != null) {
                mBodyAnalyzeTextView.setOnClickListener(mClickListener);
                mBodyAnalyzeTextView.setTag(object);
            }
        }
    }
}
