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
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.imageloader.ImageLoaderCallback;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
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

    private String mRecordId;

    private SmartDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartspot_history);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRecordId = getIntent().getStringExtra(KEY_RDID);

        mAdapter = new SmartDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    public void updateData(SmartspotDetailResult.DataBean data) {
        if (null == data) {
            mStateView.setState(StateView.State.NO_DATA);
            return;
        }

        mStateView.setState(StateView.State.SUCCESS);
        SmartspotDetailResult.DataBean.InfoBean info = data.getInfo();
        mTvTitle.setText(info.getTitle());
        mTvDateTime.setText(calDateString(info.getStartTime(), info.getEndTime()));// TODO: 2017/9/2

        //mAdapter.addData(data.getList());
        mAdapter.setData(data.getList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new SmartSpotDetailContract.Presenter();
    }

    class SmartDetailAdapter extends BaseRecycleViewAdapter<SmartDetailAdapter.SmartspotViewHolder, SmartspotDetailResult.DataBean.ListBean> {

        private Context mContext;

        public SmartDetailAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected SmartspotViewHolder createViewHolder(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_smartspot_detail, parent, false);
            return new SmartspotViewHolder(view,mContext);
        }

        class SmartspotViewHolder extends BaseRecycleViewHolder<SmartspotDetailResult.DataBean.ListBean> {
            @BindView(R.id.tv_set)
            TextView tvSet;
            @BindView(R.id.tv_reps)
            TextView tvReps;
            @BindView(R.id.tv_time)
            TextView tvTime;
            @BindView(R.id.tv_rest_time)
            TextView tvRestTime;
            @BindView(R.id.tv_end)
            TextView tvEnd;
            @BindView(R.id.hiv_capture)
            HImageView hivCapture;
            @BindView(R.id.view_image)
            View viewImage;
            @BindView(R.id.play_video_icon)
            ImageView iconImageView;

            public SmartspotViewHolder(View itemView, Context context) {
                super(itemView, context);

                ButterKnife.bind(this, itemView);

                mTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Impact.ttf");
                tvSet.setTypeface(mTypeFace);
                tvReps.setTypeface(mTypeFace);
                tvTime.setTypeface(mTypeFace);
                tvRestTime.setTypeface(mTypeFace);
                tvEnd.setTypeface(mTypeFace);
            }

            @OnClick({R.id.view_image})
            public void onViewClicked(View view) {
                SmartspotDetailResult.DataBean.ListBean item = (SmartspotDetailResult.DataBean.ListBean) view.getTag();
                if (null != item.getMedias()) {
                    SmartspotDetailResult.DataBean.ListBean.MediasBean bean = item.getMedias();
                    VideoPlayActivity.launch(SmartSpotDetailActivity.this, bean.getImg(), bean.getVideo(), mTvTitle.getText().toString());
                }

//                List<SmartspotDetailResult.DataBean.ListBean> dataList = getDataList();
//                ArrayList<String> mVideos = new ArrayList<>();
//                ArrayList<String> mImages = new ArrayList<>();
//                for (SmartspotDetailResult.DataBean.ListBean item : dataList) {
//                    String video = item.getMedias().getVideo();
//                    mVideos.add(video);
//                    System.out.println(video);
//                    String image = item.getMedias().getImg();
//                    mImages.add(image);
//                }
//                VideoPlayActivity.launch(SmartSpotDetailActivity.this, mImages, mVideos);
            }

            private Typeface mTypeFace;




            @Override
            public void bindViews(SmartspotDetailResult.DataBean.ListBean data) {
                viewImage.setTag(data);
                List<SmartspotDetailResult.DataBean.ListBean> dataList = getDataList();
                int index = dataList.indexOf(data) + 1;
                tvEnd.setVisibility(index == dataList.size() ? View.VISIBLE : View.GONE);

                tvSet.setText("SET " + index);
                tvReps.setText(data.getReps());
                tvTime.setText(String.valueOf(data.getTime()));
                tvRestTime.setText(String.valueOf(data.getRestTime()));
                SmartspotDetailResult.DataBean.ListBean.MediasBean bean = data.getMedias();
                if (null != bean) {
                    HImageLoaderSingleton.loadImage(hivCapture, bean.getImg(), new ImageLoaderCallback() {
                        @Override
                        public void finish(Bitmap bitmap) {
                            super.finish(bitmap);
                            viewImage.setBackgroundColor(ResourceUtils.getColor(R.color.action_bar_gray));
                            iconImageView.setImageResource(R.drawable.ic_video_load_success);
                        }
                    }, SmartSpotDetailActivity.this);
                }
            }
        }
    }

    private static final String KEY_RDID = "key_record_id";

    public static void launch(Context context, String recordID) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, SmartSpotDetailActivity.class);
        intent.putExtra(KEY_RDID, recordID);
        context.startActivity(intent);
    }
}
