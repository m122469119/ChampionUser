package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.DateUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.SportListResult;
import com.goodchef.liking.module.smartspot.SmartspotDetailResult;

import java.util.Date;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/2
 * @Version 1.0
 */
public class SportDataAdapter extends BaseRecyclerAdapter<SportListResult.DataBean.ListBean> {
    private OnNextClickListener mOnNextClickListener;

    public OnNextClickListener getOnNextClickListener() {
        return mOnNextClickListener;
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.mOnNextClickListener = onNextClickListener;
    }

    public SportDataAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new SportDataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sport_data, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, final SportListResult.DataBean.ListBean data) {
        if (viewHolder instanceof SportDataViewHolder) {
            SportDataViewHolder holder = (SportDataViewHolder) viewHolder;
            holder.bindViews(data);
            holder.mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNextClickListener != null) {
                        mOnNextClickListener.onClick(data);
                    }
                }
            });
        }
    }


    public interface OnNextClickListener {
        void onClick(SportListResult.DataBean.ListBean bean);
    }


    public static class SportDataViewHolder extends BaseRecycleViewHolder<SportListResult.DataBean.ListBean> {
        @BindView(R.id.image_icon)
        ImageView mImageIcon;

        @BindView(R.id.text_title)
        TextView mTitle;

        @BindView(R.id.text_content)
        TextView mContent;

        @BindView(R.id.image_next)
        ImageView mPlay;

        private StringBuilder sb = new StringBuilder();

        public SportDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(SportListResult.DataBean.ListBean object) {
            setImageIcon(object);
            mTitle.setText(object.getName() + " " + object.getPeriod());


        }

        private void setImageIcon(SportListResult.DataBean.ListBean object) {
            sb.setLength(0);
            switch (object.getType()) {
                case SportListResult.DataBean.ListBean.TYPE_LEAGUE_LESSON:
                    mImageIcon.setImageResource(R.mipmap.sport_league_lesson);
                    mPlay.setVisibility(View.GONE);
                    sb.append(ResourceUtils.getString(R.string.sport_league_lesson));
                    break;
                case SportListResult.DataBean.ListBean.TYPE_PRIVATE_LESSON:
                    mImageIcon.setImageResource(R.mipmap.sport_private_lesson);
                    mPlay.setVisibility(View.GONE);
                    sb.append(ResourceUtils.getString(R.string.sport_private_lesson));
                    break;
                case SportListResult.DataBean.ListBean.TYPE_TREADMILL:
                    mImageIcon.setImageResource(R.mipmap.sport_treadmill);
                    mPlay.setVisibility(View.GONE);
                    sb.append(ResourceUtils.getString(R.string.sport_treadmill));
                    break;
                case SportListResult.DataBean.ListBean.TYPE_SMARTSPOT:
                    mImageIcon.setImageResource(R.mipmap.sport_smartspot);
                    mPlay.setVisibility(View.VISIBLE);
                    sb.append(ResourceUtils.getString(R.string.sport_smartspot));
                    break;
            }
            sb.append("  ");
            sb.append(dateParse(object.getDate()));
            mContent.setText(sb.toString());
        }

        private String dateParse(String data) {
            try {
                return DateUtils.formatDate("HH:mm:ss", DateUtils.parseString("yyyy-MM-dd HH:mm:ss", data));
            }catch (Exception e){
                return "";
            }
        }
    }

}
