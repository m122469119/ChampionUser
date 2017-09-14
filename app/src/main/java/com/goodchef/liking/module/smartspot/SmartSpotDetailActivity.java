package com.goodchef.liking.module.smartspot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.imageloader.ImageLoaderCallback;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.SmartSpotRecordListAdapter;
import com.goodchef.liking.module.paly.VideoPlayActivity;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ttdevs
 * 2017-09-01 (AndroidLiking)
 * https://github.com/ttdevs
 */
public class SmartSpotDetailActivity extends AppBarMVPSwipeBackActivity<SmartSpotDetailContract.Presenter> implements SmartSpotDetailContract.View {
    @BindView(R.id.view_state)
    LikingStateView mStateView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_datetime)
    TextView mTvDateTime;

    private static final String KEY_RDID = "key_record_id";
    private String mRecordId;
    private SmartSpotRecordListAdapter smartSpotRecordListAdapter;
    List<String> videoUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartspot_history);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRecordId = getIntent().getStringExtra(KEY_RDID);

        smartSpotRecordListAdapter = new SmartSpotRecordListAdapter(this);
        mRecyclerView.setAdapter(smartSpotRecordListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setOnItemListener();

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                requestSmartspotDetail();
            }
        });


        setTitle("SmartSpot训练记录");
        requestSmartspotDetail();
    }

    private void requestSmartspotDetail() {
        mPresenter.requestSmartDetail(mRecordId);
    }

    private String calDateString(String start, String end) {
        String result = null;
        try {
            Calendar startCal = Calendar.getInstance();
            startCal.setTimeInMillis(Long.parseLong(start) * 1000);
            Calendar endCal = Calendar.getInstance();
            endCal.setTimeInMillis(Long.parseLong(end) * 1000);
            int year = startCal.get(Calendar.YEAR);
            int month = startCal.get(Calendar.MONTH) + 1;
            int day = startCal.get(Calendar.DAY_OF_MONTH);
            String week = getWeek(startCal);
            int startHour = startCal.get(Calendar.HOUR_OF_DAY);
            int startMinute = startCal.get(Calendar.MINUTE);
            int endHour = endCal.get(Calendar.HOUR_OF_DAY);
            int endMinute = endCal.get(Calendar.MINUTE);
            String format = "%d.%d.%d(%s) %d:%d - %d:%d";
            result = String.format(format,
                    year, month, day,
                    week,
                    startHour, startMinute,
                    endHour, endMinute);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getWeek(Calendar calendar) {
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (week) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";

            default:
                return "周日";
        }
    }

    @Override
    public void updateData(final SmartspotDetailResult.DataBean data) {
        if (null == data) {
            mStateView.setState(StateView.State.NO_DATA);
            return;
        }
        mStateView.setState(StateView.State.SUCCESS);
        SmartspotDetailResult.DataBean.InfoBean info = data.getInfo();
        mTvTitle.setText(info.getTitle());
        mTvDateTime.setText(calDateString(info.getStartTime(), info.getEndTime()));
        smartSpotRecordListAdapter.setData(data.getList());
        smartSpotRecordListAdapter.notifyDataSetChanged();
        doPlayUrlList();
    }

    /**
     * 处理视频的url
     */
    private void doPlayUrlList() {
        videoUrlList.clear();
        for (SmartspotDetailResult.DataBean.ListBean dataBean : smartSpotRecordListAdapter.getDataList()) {
            videoUrlList.add(dataBean.getMedias().getVideo());
        }
    }

    private void setOnItemListener() {
        smartSpotRecordListAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                launch(SmartSpotDetailActivity.this, new ArrayList<String>(), (ArrayList<String>) videoUrlList, mTvTitle.getText().toString(), position);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }


    public void launch(Context context, String img, String video, String title, int postion) {
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> videos = new ArrayList<>();
        imgs.add(img);
        videos.add(video);
        launch(context, imgs, videos, title, postion);
    }

    public void launch(Context context,
                       ArrayList<String> img,
                       ArrayList<String> video,
                       String title,
                       int postion) {
        if (null == context) {
            return;
        }
        if (null == video || video.size() == 0) {
            return;
        }
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putStringArrayListExtra(VideoPlayActivity.KEY_IMG, img);
        intent.putStringArrayListExtra(VideoPlayActivity.KEY_VIDEO, video);
        intent.putExtra(VideoPlayActivity.KEY_TITLE, title);
        intent.putExtra(VideoPlayActivity.VIDEO_POSTION, postion);
        context.startActivity(intent);
        overridePendingTransition(R.anim.silde_bottom_in, 0);
    }


    @Override
    public void setPresenter() {
        mPresenter = new SmartSpotDetailContract.Presenter();
    }

    public static void launch(Context context, String recordID) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, SmartSpotDetailActivity.class);
        intent.putExtra(KEY_RDID, recordID);
        context.startActivity(intent);
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
