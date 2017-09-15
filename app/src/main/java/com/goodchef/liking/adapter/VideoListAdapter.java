package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;

/**
 * Created by aaa on 17/9/15.
 */

public class VideoListAdapter extends BaseRecycleViewAdapter<VideoListAdapter.videoListHolder, String> {


    private Context mContext;

    public VideoListAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected videoListHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_video_play, parent, false);
        return new videoListHolder(view, mContext);
    }

    class videoListHolder extends BaseRecycleViewHolder<String> {

        public videoListHolder(View itemView, Context context) {
            super(itemView, context);
        }

        @Override
        public void bindViews(String object) {

        }
    }
}
