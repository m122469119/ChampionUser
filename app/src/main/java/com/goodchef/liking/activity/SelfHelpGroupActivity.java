package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.framework.utils.ViewUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.SelfHelpCoursesRoomAdapter;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.NoCardMessage;
import com.goodchef.liking.eventmessages.OrderGroupMessageSuccess;
import com.goodchef.liking.eventmessages.SelectCoursesMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.mvp.presenter.SelfHelpGroupCoursesPresenter;
import com.goodchef.liking.mvp.view.SelfHelpGroupCoursesView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
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
    private TextView mCoursesTrainTextView;//课程训练名称
    private TextView mCoursesDurationTextView;//课程训练时长
    private TextView mGroupCoursesStrongTextView;//课程强度
    private TextView mCoursesIntroduceTextView;//课程介绍
    private TextView mAccommodateNumberTextView;//最多容纳人数
    private LinearLayout mSelectCoursesLayout;//选择课程布局
    private LinearLayout mNoneCoursesLayout;//没有选择课程布局
    private LikingStateView mLikingStateView;

    private View selfCoursesView;
    private LinearLayout otherCoursesView;
    private ImageView noDataImageView;//当前时间预留教练排团体课
    private TextView noDataText;
    private TextView refreshView;

    private SelfHelpGroupCoursesPresenter mSelfHelpGroupCoursesPresenter;
    private SelfHelpCoursesRoomAdapter helpCoursesRoomAdapter;
    private AnimalsHeadersAdapter mAnimalsHeadersAdapter;

    private StickyRecyclerHeadersDecoration headersDecor;

    private List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> timeList = new ArrayList<>();
    private String roomId = "";
    private String coursesId = "";
    private String courseDate = "";
    private String startTime = "";
    private String endTime = "";
    private String price = "";
    private String peopleNum = "0";

    private SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData mSelectLastCoursesData =  null;

    private String mCurrSelectDate = "";//当前选中的时间段
    private String mCurrSelectHour = "";//当前选中的小时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_help_group);
        initView();
        setViewOnClickListener();
        sendRequest();
        setTitle(LikingHomeActivity.gymName);
    }

    private void initView() {
        selfCoursesView = findViewById(R.id.layout_self_right);
        otherCoursesView = (LinearLayout)findViewById(R.id.layout_self_right_overlap);
        otherCoursesView.setPadding(0,DisplayUtils.getHeightPixels() / 6,0,0);
        otherCoursesView.setGravity(Gravity.START);

        mImmediatelyTextView = (TextView) findViewById(R.id.self_help_immediately_appointment_TextView);
        mCoursesTimeRecyclerView = (RecyclerView) findViewById(R.id.self_help_group_courses_time_RecyclerView);
        mUserTimeTextView = (TextView) findViewById(R.id.self_help_user_time);
        mGymRecyclerView = (RecyclerView) findViewById(R.id.self_help_gym_RecyclerView);
        mSelfGymHImageView = (HImageView) findViewById(R.id.self_help_gym_image);
        mCoursesTrainTextView = (TextView) findViewById(R.id.group_courses_train_object);
        mCoursesDurationTextView = (TextView) findViewById(R.id.group_courses_duration);
        mGroupCoursesStrongTextView = (TextView) findViewById(R.id.group_courses_strong);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.self_help_courses_introduce);
        mAccommodateNumberTextView = (TextView) findViewById(R.id.accommodate_user_number);
        mSelectCoursesLayout = (LinearLayout) findViewById(R.id.layout_select_group_courses);
        mNoneCoursesLayout = (LinearLayout) findViewById(R.id.layout_self_group_courses_none) ;
        mLikingStateView = (LikingStateView) findViewById(R.id.self_help_stateView);


        noDataImageView = (ImageView) findViewById(R.id.imageview_no_data);
        noDataText = (TextView) findViewById(R.id.textview_no_data);
        refreshView = (TextView) findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
        noDataText.setText(R.string.self_courses_other);
        refreshView.setText(R.string.self_courses_goto_home);
        refreshView.setOnClickListener(skipOnClickListener);
    }

    /***
     * 没哟课程跳转事件
     */
    private View.OnClickListener skipOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SelfHelpGroupActivity.this, LikingHomeActivity.class);
            intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, 0);
            startActivity(intent);
            finish();
        }
    };

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
            Intent intent = new Intent(this, SelectCoursesListActivity.class);
            String ssid = mSelectLastCoursesData.getCourseId();
            if(TextUtils.isEmpty(ssid)) {
                ssid = "";
            }
            intent.putExtra(SelectCoursesListActivity.KEY_SELECT_COURSES_ID,ssid);
            startActivity(intent);
        } else if (v == mImmediatelyTextView) {//立即预约
            if (!Preference.isLogin()) {
                startActivity(LoginActivity.class);
                return;
            }
            if (StringUtils.isEmpty(roomId)) {
                PopupUtils.showToast("您还没有选择可用的房间");
            }
            if (StringUtils.isEmpty(coursesId)) {
                PopupUtils.showToast("您还没有选择课程");
                return;
            }
            sendOrderRequest();
        }
    }

    private void sendOrderRequest() {
        mSelfHelpGroupCoursesPresenter.sendOrderRequest(LikingHomeActivity.gymId, roomId, coursesId, courseDate, startTime, endTime, price, peopleNum);
    }


    @Override
    public void updateSelfHelpGroupCoursesView(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData selfHelpGroupCoursesData) {
        mLikingStateView.setState(StateView.State.SUCCESS);
        mSelectLastCoursesData = selfHelpGroupCoursesData.getLastCourse();
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData> DataList = selfHelpGroupCoursesData.getTime();
        if(timeList.size() > 0)  timeList.clear();
        for (int i = 0; i < DataList.size(); i++) {//将两个集合组装成一个集合
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> list = DataList.get(i).getHour();
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setDate(DataList.get(i).getDate());
                list.get(j).setDay(DataList.get(i).getDay());
            }
            timeList.addAll(list);
        }
        setLeftTimeListData(timeList);
        setLastCourseInfo(mSelectLastCoursesData);
    }

    /**
     * 设置 last_course
     *
     * @param coursesData
     */
    private void setLastCourseInfo(SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData){
        if(coursesData != null && !TextUtils.isEmpty(coursesData.getCourseId())){
            setSelectCoursesInfo(coursesData);
        }else {
            mNoneCoursesLayout.setVisibility(View.VISIBLE);
            mCoursesIntroduceTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateOrderView() {
        PopupUtils.showToast("预约成功");
        Intent intent = new Intent(this, MyLessonActivity.class);
        intent.putExtra(MyLessonActivity.KEY_CURRENT_ITEM, 0);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateNoCardView(String message) {
        showNoCardDialog(message);
    }

    @Override
    public void updateSelectCourserView() {
        sendRequest();
    }

    private void showNoCardDialog(String message) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SelfHelpGroupActivity.this, LikingHomeActivity.class);
                intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, 1);
                startActivity(intent);
                postEvent(new NoCardMessage(1));
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }


    /**
     * 设置左边的时间数据
     *
     * @param timeList
     */
    private void setLeftTimeListData(final List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> timeList) {
        if (timeList != null && timeList.size() > 0) {
            int poi = 0 ;
            for (int i = 0; i < timeList.size(); i++) {//设置默认选中第一个时间
                timeList.get(i).setHourId(i);//自定义一个id，为了点击左边时间事件，判断选中状态埋下伏笔
                timeList.get(i).setSelect(false);
            }
            if(TextUtils.isEmpty(mCurrSelectDate)) {//默认选中的时间
                mCurrSelectDate = timeList.get(poi).getDate();
                mCurrSelectHour = timeList.get(poi).getHour();
                timeList.get(poi).setSelect(true);
                selectUserTime(timeList.get(poi));
                setClickTimeRightData(timeList.get(poi));//默认选中地0个
            } else {  // 刷新数据
                for (SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData:timeList) {
                    if(hourData.getDate().equals(mCurrSelectDate) && hourData.getHour().equals(mCurrSelectHour)) {
                        if(mAnimalsHeadersAdapter != null) {
                            timeList.get(poi).setSelect(true);
                            selectUserTime(timeList.get(poi));
                            setClickTimeRightData(timeList.get(poi));
                        }
                        break;
                    }
                    poi++;
                }
            }

            if( mAnimalsHeadersAdapter == null) {
                mCoursesTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mAnimalsHeadersAdapter = new AnimalsHeadersAdapter();
                mCoursesTimeRecyclerView.setAdapter(mAnimalsHeadersAdapter);
            }

            mAnimalsHeadersAdapter.clear();
            mAnimalsHeadersAdapter.addAll(timeList);

            if(headersDecor == null) {
                headersDecor = new StickyRecyclerHeadersDecoration(mAnimalsHeadersAdapter);
                mCoursesTimeRecyclerView.addItemDecoration(headersDecor);
                mCoursesTimeRecyclerView.addItemDecoration(new DividerDecoration(this));
                mCoursesTimeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.self_help_courses_time);
                        if (textView != null) {
                            SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData = (SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData) textView.getTag();
                            setRigthSelectViewData(hourData);
                        }
                    }
                }));

                mAnimalsHeadersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        headersDecor.invalidateHeaders();
                    }
                });
            }

        }
    }

    /**
     * 设置右边View数据
     * @param hourData
     */

    private void  setRigthSelectViewData(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData) {
        if(hourData != null) {
            mCurrSelectDate = hourData.getDate();
            mCurrSelectHour = hourData.getHour();
            if (hourData != null && !StringUtils.isEmpty(hourData.getHour())) {
                for (int i = 0; i < timeList.size(); i++) {
                    if (timeList.get(i).getHourId() == hourData.getHourId()) {
                        timeList.get(i).setSelect(true);
                    } else {
                        timeList.get(i).setSelect(false);
                    }
                }
                mAnimalsHeadersAdapter.notifyDataSetChanged();
                setClickTimeRightData(hourData);
            }
        }
    }

    /**
     * 选中左边时间，改变右边使用时段字段
     *
     * @param hourData
     */
    private void selectUserTime(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData) {
        String day = hourData.getDay();
        String arr[] = day.split("\n");
        if (arr.length > 0) {
            String selectDay = arr[0];
            mUserTimeTextView.setText(selectDay + " " + hourData.getDuration());
            courseDate = hourData.getDate();//获取选中时间的当天时间  （今天还是明天）
            startTime = hourData.getHour();//获取选中的开始时间（课程的开始时间）
//            Date endDate = DateUtils.parseString("HH-mm", startTime);
//            Date afterDate = new Date(endDate.getTime() + (50 * 60 * 1000));
//            endTime = DateUtils.formatDate("HH-mm", afterDate);//课程的结束时间为开始时间+50分钟
        }
    }

    /**
     * 设置点击左边时间更改右边的数据
     *
     * @param hourData
     */
    private void setClickTimeRightData(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData) {
        if(hourData.isAvailable()) {//操房是否可用
            selectUserTime(hourData);
            selfCoursesView.setVisibility(View.VISIBLE);
            otherCoursesView.setVisibility(View.GONE);
            if(hourData.isFilled()) {//是否所有操房均排课
                mSelectCoursesLayout.setVisibility(View.GONE);
                mImmediatelyTextView.setVisibility(View.GONE);
            }else {
                mSelectCoursesLayout.setVisibility(View.VISIBLE);
                mImmediatelyTextView.setVisibility(View.VISIBLE);
            }
            setLastCourseInfo(mSelectLastCoursesData);
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> roomList = new ArrayList<>();
            List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> roomDataList = hourData.getRoom();
            if (roomDataList != null && roomDataList.size() > 0) {

                List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> isScheduledtemporary = new ArrayList<>();
                List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData> notScheduledtemporary = new ArrayList<>();
                for (int i = 0; i < roomDataList.size(); i++) {//将操房刷选出来重现排列
                    if (roomDataList.get(i).isScheduled()) {//可以安排的操房
                        isScheduledtemporary.add(roomDataList.get(i));
                    } else {//不能安排的操房
                        notScheduledtemporary.add(roomDataList.get(i));
                    }
                }
                if (notScheduledtemporary.size() > 0) {
                    for (int i = 0; i < notScheduledtemporary.size(); i++) {
                        if (i == 0) {
                            notScheduledtemporary.get(i).setCheck(true);
                            mAccommodateNumberTextView.setText(notScheduledtemporary.get(i).getCapacity()+ "人");
                            roomId = notScheduledtemporary.get(i).getId() + "";
                            peopleNum = notScheduledtemporary.get(i).getCapacity()+"";
                        } else {
                            notScheduledtemporary.get(i).setCheck(false);
                        }
                    }
                    roomList.addAll(notScheduledtemporary);
                }
                if (isScheduledtemporary.size() > 0) {
                    roomList.addAll(isScheduledtemporary);
                }
                mGymRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                helpCoursesRoomAdapter = new SelfHelpCoursesRoomAdapter(this);
                helpCoursesRoomAdapter.setData(roomList);
                mGymRecyclerView.setAdapter(helpCoursesRoomAdapter);
                helpCoursesRoomAdapter.setSelectRoomOnClickListener(SelectRoomClickListener);
                helpCoursesRoomAdapter.setSelectRoomJoinClickListener(joinRoomClickListener);
            }
        }else{
            mImmediatelyTextView.setVisibility(View.GONE);
            selfCoursesView.setVisibility(View.GONE);
            otherCoursesView.setVisibility(View.VISIBLE);
        }


    }


    /**
     * 点击操房选择事件
     */
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
                        roomId = roomData.getId() + "";
                        peopleNum = roomData.getCapacity()+"";
                        mAccommodateNumberTextView.setText(roomData.getCapacity() + "人");
                        helpCoursesRoomAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    /**
     * 点击加入课程选择事件
     */
    private View.OnClickListener joinRoomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView mJoinCoursesTextView = (TextView) v.findViewById(R.id.join_courses_TextView);
            if (mJoinCoursesTextView != null) {
                SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData roomData = (SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData) v.getTag();
                if (roomData != null) {
                    joinGroupDetails(roomData);
                }
            }
        }
    };

    /**
     * 跳转到团体课详情
     *
     * @param data 课程对象
     */
    private void joinGroupDetails(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData.RoomData data) {
        UMengCountUtil.UmengCount(this, UmengEventId.GROUPLESSONDETAILSACTIVITY);
        Intent intent = new Intent(this, GroupLessonDetailsActivity.class);
        intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID, data.getScheduleId() + "");
        startActivity(intent);
    }

    private class AnimalsHeadersAdapter extends AnimalsAdapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_self_help_group_time, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData hourData = getItem(position);
            if (hourData.isSelect()) {
                textView.setBackgroundColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                textView.setTextColor(ResourceUtils.getColor(R.color.white));
            } else {
                textView.setBackgroundColor(ResourceUtils.getColor(R.color.cedeff3));
                textView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
            }
            textView.setText(hourData.getHour());
            textView.setTag(hourData);
        }

        @Override
        public long getHeaderId(int position) {
            return Long.parseLong(getItem(position).getDate());
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_self_help_group_date, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            String date = getItem(position).getDay();
            TextView textView = (TextView) holder.itemView;
            textView.setText(date);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(SelectCoursesMessage message) {
        SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData mCoursesData = message.getCoursesData();
        setSelectCoursesInfo(mCoursesData);
    }

    public void onEvent(OrderGroupMessageSuccess message){
        this.finish();
    }

    public void setSelectCoursesInfo(SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData mCoursesData){
        mSelectLastCoursesData = mCoursesData;
        mNoneCoursesLayout.setVisibility(View.GONE);
        mCoursesTrainTextView.setText(mCoursesData.getName());
        mCoursesIntroduceTextView.setVisibility(View.VISIBLE);
        mCoursesIntroduceTextView.setText(mCoursesData.getDesc());
        String duration = "";
        try{
            duration = Integer.parseInt(mCoursesData.getVideoDuration()) / 60 + "min";
        }catch (Exception e){}
        mCoursesDurationTextView.setText(getResources().getString(R.string.self_courses_time) + duration);
        mGroupCoursesStrongTextView.setText("课程强度:" + mCoursesData.getIntensity());
        List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData.ImgData> imageUrlList = mCoursesData.getImg();
        if (imageUrlList != null && imageUrlList.size() > 0) {
            String imageUrl = imageUrlList.get(0).getUrl();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mSelfGymHImageView, imageUrl);
            } else {
                HImageLoaderSingleton.getInstance().loadImage(mSelfGymHImageView, "");
            }
        }
        coursesId = mCoursesData.getCourseId();
        price = mCoursesData.getDefaultPrice();
    }


    @Override
    public void handleNetworkFailure() {
        mLikingStateView.setState(StateView.State.FAILED);
    }
}
