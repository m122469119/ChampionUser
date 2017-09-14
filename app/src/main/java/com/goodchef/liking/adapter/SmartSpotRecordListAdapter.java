package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.module.smartspot.SmartspotDetailResult;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.utils.TypefaseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaa on 17/9/14.
 */

public class SmartSpotRecordListAdapter extends BaseRecycleViewAdapter<SmartSpotRecordListAdapter.SmartSpotRecordListViewHolder, SmartspotDetailResult.DataBean.ListBean> {

    private Typeface mTypeFace;
    private Context mContext;

    public SmartSpotRecordListAdapter(Context context) {
        super(context);
        mTypeFace = TypefaseUtil.getImpactTypeface(context);
        mContext = context;
    }

    @Override
    protected SmartSpotRecordListViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_smartspot_detail, parent, false);
        return new SmartSpotRecordListViewHolder(view,mContext);
    }

    class SmartSpotRecordListViewHolder extends BaseRecycleViewHolder<SmartspotDetailResult.DataBean.ListBean> {

        @BindView(R.id.tv_set)
        TextView tvSet;
        @BindView(R.id.tv_reps)
        TextView tvReps;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_rest_time)
        TextView tvRestTime;
        @BindView(R.id.hiv_capture)
        HImageView hivCapture;
        @BindView(R.id.play_video_icon)
        ImageView playVideoIcon;
        @BindView(R.id.view_image)
        FrameLayout viewImage;
        @BindView(R.id.tv_end)
        TextView tvEnd;

        public SmartSpotRecordListViewHolder(View itemView, Context context) {
            super(itemView, context);
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void bindViews(SmartspotDetailResult.DataBean.ListBean data) {
            tvSet.setTypeface(mTypeFace);
            tvReps.setTypeface(mTypeFace);
            tvTime.setTypeface(mTypeFace);
            tvRestTime.setTypeface(mTypeFace);
            tvEnd.setTypeface(mTypeFace);

            viewImage.setTag(data);
            List<SmartspotDetailResult.DataBean.ListBean> dataList = getDataList();
            int index = dataList.indexOf(data) + 1;
            tvEnd.setVisibility(index == dataList.size() ? View.VISIBLE : View.GONE);

            tvSet.setText("SET " + index);
            tvReps.setText(data.getReps());
            tvTime.setText(data.getTime() + " s");
            tvRestTime.setText(data.getRestTime() + " s");
            SmartspotDetailResult.DataBean.ListBean.MediasBean bean = data.getMedias();
            if (null != bean) {
                HImageLoaderSingleton.loadImage(hivCapture, bean.getImg(), (Activity) mContext);
            }

        }
    }
}
