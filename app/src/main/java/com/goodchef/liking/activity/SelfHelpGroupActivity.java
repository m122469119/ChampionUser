package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.SelfHelpCoursesRoomAdapter;
import com.goodchef.liking.adapter.SelfHelpGroupCourseTimeAdapter;
import com.goodchef.liking.adapter.SelfHelpGroupCoursesDateAdapter;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.mvp.presenter.SelfHelpGroupCoursesPresenter;
import com.goodchef.liking.mvp.view.SelfHelpGroupCoursesView;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.widgets.stickyheaderrecyclerview.AnimalsAdapter;
import com.goodchef.liking.widgets.stickyheaderrecyclerview.DividerDecoration;
import com.goodchef.liking.widgets.stickyheaderrecyclerview.RecyclerItemClickListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author: shaozucheng
 * Time: 下午4:05
 */

public class SelfHelpGroupActivity extends AppBarActivity implements View.OnClickListener, SelfHelpGroupCoursesView {

    private TextView mImmediatelyTextView;
    private RecyclerView mCoursesTimeRecyclerView;
    private TextView mUserTimeTextView;//使用时段
    private RecyclerView mGymRecyclerView;
    private HImageView mSelfGymHImageView;
    private TextView mCoursesTrainTextView;
    private TextView mGroupCoursesTimeTextView;
    private TextView mGroupCoursesStrongTextView;
    private TextView mCoursesIntroduceTextView;
    private TextView mAccommodateNumberTextView;//最多容纳人数
    private LinearLayout mSelectCoursesLayout;//选择课程布局
    private LikingStateView mLikingStateView;

    private SelfHelpGroupCoursesPresenter mSelfHelpGroupCoursesPresenter;
    private SelfHelpGroupCoursesDateAdapter mSelfHelpGroupCoursesDateAdapter;
    private SelfHelpCoursesRoomAdapter helpCoursesRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_help_group);
        initView();
        setViewOnClickListener();
        sendRequest();
    }

    private void initView() {
        mImmediatelyTextView = (TextView) findViewById(R.id.self_help_immediately_appointment_TextView);
        mCoursesTimeRecyclerView = (RecyclerView) findViewById(R.id.self_help_group_courses_time_RecyclerView);
        mUserTimeTextView = (TextView) findViewById(R.id.self_help_user_time);
        mGymRecyclerView = (RecyclerView) findViewById(R.id.self_help_gym_RecyclerView);
        mSelfGymHImageView = (HImageView) findViewById(R.id.self_help_gym_image);
        mCoursesTrainTextView = (TextView) findViewById(R.id.group_courses_train_object);
        mGroupCoursesTimeTextView = (TextView) findViewById(R.id.group_courses_time);
        mGroupCoursesStrongTextView = (TextView) findViewById(R.id.group_courses_strong);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.self_help_courses_introduce);
        mAccommodateNumberTextView = (TextView) findViewById(R.id.accommodate_user_number);
        mSelectCoursesLayout = (LinearLayout) findViewById(R.id.layout_select_group_courses);
        mLikingStateView = (LikingStateView) findViewById(R.id.self_help_stateView);
    }

    private void setViewOnClickListener() {
        mSelectCoursesLayout.setOnClickListener(this);
        mImmediatelyTextView.setOnClickListener(this);
    }

    private void sendRequest() {
        if (mSelfHelpGroupCoursesPresenter == null) {
            mSelfHelpGroupCoursesPresenter = new SelfHelpGroupCoursesPresenter(this, this);
        }
        mLikingStateView.setState(StateView.State.LOADING);
        mSelfHelpGroupCoursesPresenter.getSelfHelpCourses(LikingHomeActivity.gymId);
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectCoursesLayout) {//选择课程

        } else if (v == mImmediatelyTextView) {//立即预约
        }
    }

    @Override
    public void updateSelfHelpGroupCoursesView(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData selfHelpGroupCoursesData) {
        mLikingStateView.setState(StateView.State.SUCCESS);
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData> DataList = selfHelpGroupCoursesData.getTime();
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> timeList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < DataList.size(); i++) {
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> list = DataList.get(i).getHour();
            timeList.addAll(list);
            timeList.get(index).setDate(DataList.get(i).getDate());
            index += list.size();
        }


        if (DataList != null) {
            mCoursesTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            mSelfHelpGroupCoursesDateAdapter = new SelfHelpGroupCoursesDateAdapter(this);
//            mSelfHelpGroupCoursesDateAdapter.setData(timeDataList);
//            mCoursesTimeRecyclerView.setAdapter(mSelfHelpGroupCoursesDateAdapter);
            final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();
            adapter.addAll(timeList);
            mCoursesTimeRecyclerView.setAdapter(adapter);
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
            mCoursesTimeRecyclerView.addItemDecoration(headersDecor);
            mCoursesTimeRecyclerView.addItemDecoration(new DividerDecoration(this));
            mCoursesTimeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.self_help_courses_time);
                    if (textView != null) {
                        SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData = (SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData) textView.getTag();
                        if (hourData != null && !StringUtils.isEmpty(hourData.getHour())) {
                            setClickTimeRightData(hourData);
                        }
                    }
                }
            }));
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });
        }
    }

    /**
     * 设置点击左边时间更改右边的数据
     *
     * @param hourData
     */
    private void setClickTimeRightData(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData) {
        mUserTimeTextView.setText(hourData.getHour());
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> roomList = new ArrayList<>();
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> roomDataList = hourData.getRoom();
        if (roomDataList != null && roomDataList.size() > 0) {
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> isScheduledtemporary = new ArrayList<>();
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> notScheduledtemporary = new ArrayList<>();
            for (int i = 0; i < roomDataList.size(); i++) {
                if (roomDataList.get(i).isScheduled()) {
                    isScheduledtemporary.add(roomDataList.get(i));
                } else {
                    notScheduledtemporary.add(roomDataList.get(i));
                }
            }
            if (notScheduledtemporary != null && notScheduledtemporary.size() > 0) {
                for (int i = 0; i < notScheduledtemporary.size(); i++) {
                    if (i == 0) {
                        notScheduledtemporary.get(i).setCheck(true);
                        mAccommodateNumberTextView.setText(notScheduledtemporary.get(i).getQuota() + "人");
                    } else {
                        notScheduledtemporary.get(i).setCheck(false);
                    }
                }
                roomList.addAll(notScheduledtemporary);
            }
            if (isScheduledtemporary != null && isScheduledtemporary.size() > 0) {
                roomList.addAll(isScheduledtemporary);
            }

            mGymRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            helpCoursesRoomAdapter = new SelfHelpCoursesRoomAdapter(this);
            helpCoursesRoomAdapter.setData(roomList);
            mGymRecyclerView.setAdapter(helpCoursesRoomAdapter);
            helpCoursesRoomAdapter.setSelectRoomOnClickListener(SelectRoomClickListener);
        }
    }

    private View.OnClickListener SelectRoomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout mLayout = (RelativeLayout) v.findViewById(R.id.layout_self_help_select_room);
            if (mLayout != null) {
                SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData roomData = (SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData) mLayout.getTag();
                if (roomData != null) {
                    List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> roomDataList = helpCoursesRoomAdapter.getDataList();
                    if (roomDataList != null && roomDataList.size() > 0) {
                        for (int i = 0; i < roomDataList.size(); i++) {
                            if (roomDataList.get(i).getId() == roomData.getId()) {
                                roomDataList.get(i).setCheck(true);
                            } else {
                                roomDataList.get(i).setCheck(false);
                            }
                        }
                        mAccommodateNumberTextView.setText(roomData.getQuota() + "人");
                        helpCoursesRoomAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };


    private class AnimalsHeadersAdapter extends AnimalsAdapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        TextView mHeadTextView;
        TextView mTimeTextView;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_self_help_group_time, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData = getItem(position);
            if (hourData != null) {
                mTimeTextView.setText(hourData.getHour());
                mTimeTextView.setTag(hourData);
            }
        }

        @Override
        public long getHeaderId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_self_help_group_date, parent, false);
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            String date = getItem(position).getDate();
            if (!StringUtils.isEmpty(date)) {
                mHeadTextView.setVisibility(View.VISIBLE);
                mHeadTextView.setText(date);
            } else {
                mHeadTextView.setVisibility(View.GONE);
            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
                mHeadTextView = (TextView) itemView.findViewById(R.id.self_help_courses_date);
            }
        }

        class ContentViewHolder extends RecyclerView.ViewHolder {
            public ContentViewHolder(View itemView) {
                super(itemView);
                mTimeTextView = (TextView) itemView.findViewById(R.id.self_help_courses_time);
            }
        }

    }

    @Override
    public void handleNetworkFailure() {
        mLikingStateView.setState(StateView.State.FAILED);
    }
}
