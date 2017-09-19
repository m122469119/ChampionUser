package com.goodchef.liking.module.home.myfragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BaseRecyclerAdapter;
import com.goodchef.liking.adapter.MyPersonAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.MyUserOtherInfoResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.module.brace.braceletdata.BraceletDataActivity;
import com.goodchef.liking.module.brace.mybracelet.MyBraceletActivity;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.card.my.MyCardActivity;
import com.goodchef.liking.module.card.my.UpgradeAndContinueCardActivity;
import com.goodchef.liking.module.card.order.MyOrderActivity;
import com.goodchef.liking.module.coupons.CouponsActivity;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.module.home.myfragment.water.WaterRateActivity;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.module.message.ShowCodeMessage;
import com.goodchef.liking.module.more.MoreActivity;
import com.goodchef.liking.module.train.SportDataActivity;
import com.goodchef.liking.module.train.SportDataDayFragment;
import com.goodchef.liking.module.userinfo.MyInfoActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 16/5/20.
 * 我的界面
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingMyFragment extends BaseMVPFragment<LikingMyContract.Presenter> implements android.view.View.OnClickListener, LikingMyContract.View {
    public static final String KEY_MY_BRACELET_MAC = "key_my_bracelet_mac";
    public static final String KEY_UUID = "key_UUID";
    LikingStateView mStateView;
    @BindView(R.id.head_image_background)
    HImageView mHImageViewBackground;//头像背景;
    @BindView(R.id.head_image)
    HImageView mHeadHImageView;//头像
    @BindView(R.id.person_name)
    TextView mPersonNameTextView;//用户名称;
    @BindView(R.id.is_vip)
    TextView mIsVip;
    @BindView(R.id.layout_head_info)
    View mHeadInfoLayout;//头像布局;
    @BindView(R.id.person_phone)
    TextView mPersonPhoneTextView;//用户手机;

    @BindView(R.id.login_text)
    TextView mLoginBtn;//登录按钮;

    @BindView(R.id.end_time)
    TextView mEndTime;

    @BindView(R.id.continuation_card)
    TextView mContinuationCard;

    MyPersonAdapter.MyPersonEntity mMyWaterEntity;

    RecyclerView mMyRecyclerView;
    private MyPersonAdapter mMyAdapter;
    private List<MyPersonAdapter.MyPersonEntity> mPersonEntities;

    private String gymId = "";

    @BindView(R.id.layout_body_score)
    View mBodyScoreLayout;//体测评分

    @BindView(R.id.layout_bracelet)
    View mBraceletLinearLayout;

    @BindView(R.id.layout_all_sport)
    View mAllSportLayout;

    public static final int SPORT_DOWN = -1; //体侧分数减少
    public static final int SPORT_UP = 1;  //体侧分数增加
    public static final int SPORT_NORMAL = 0; //体侧分数没变
    public static final int SPORT_NULL = 2; //无

    private TextView mBodyScoreData;//个人训练数据
    private TextView mBraceletData; // 手环数据
    private TextView mContentTextViewMin, mContentTextViewDay, mContentTextViewTime;

    private View mBodyScoreDataTitle;
    private ImageView mBodyLevelImageView;
    private LinearLayout mBodyScoreDataContent;

    private boolean isRetryRequest = true;
    private Typeface mTypeface;//字体
    private String mBraceletMac;//手环mac地址
    private String UUID;//蓝牙UUID
    private int showCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        mTypeface = TypefaseUtil.getImpactTypeface(getActivity());
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
            setRecycleViewItem();
            showUpdate();
            setLogonView();
            getMyUserInfoOther();
            getUserExerciseData();
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    private void showUpdate() {
        int update = LikingPreference.getUpdateApp();
//        if (update == 0) {//不更新
//            mUpdateAppImageView.setVisibility(android.view.View.GONE);
//        } else if (update == 1 || update == 2) {//有更新
//            mUpdateAppImageView.setVisibility(android.view.View.VISIBLE);
//        }
    }

    /**
     * 获个人信息
     */
    private void getMyUserInfoOther() {
        if (LikingPreference.isLogin()) {
            if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                clearExerciseData();
            } else {
                if (isRetryRequest) {
                    mStateView.setState(StateView.State.LOADING);
                    isRetryRequest = false;
                }
                mPresenter.getUserData();
            }
        } else {
            if (EnvironmentUtils.Network.isNetWorkAvailable()) {
                mStateView.setState(StateView.State.SUCCESS);
            }
            clearExerciseData();
        }
    }


    /**
     * 获取我的锻炼数据
     */
    private void getUserExerciseData() {
        if (LikingPreference.isLogin()) {
            mPresenter.getUserExerciseData();
        } else {
            clearExerciseData();
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(StateView.State.FAILED);
        isRetryRequest = true;
        clearExerciseData();
    }

    @Override
    public void updateInfoData(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData) {
        mStateView.setState(StateView.State.SUCCESS);
        LikingPreference.setIsBind(userOtherInfoData.getIsBind());
        LikingPreference.setIsVip(Integer.parseInt(userOtherInfoData.getIsVip()));
        mBraceletMac = userOtherInfoData.getBraceletMac();
        UUID = userOtherInfoData.getUuid();
        showCode = userOtherInfoData.getShowCode();
        postEvent(new ShowCodeMessage(showCode));

        if (LikingPreference.isVIP()) {
            mIsVip.setVisibility(android.view.View.VISIBLE);
        } else {
            mIsVip.setVisibility(android.view.View.GONE);
        }

        if (userOtherInfoData.getCan_renew() == MyUserOtherInfoResult.UserOtherInfoData.CAN_RENEW) {
            mContinuationCard.setVisibility(View.VISIBLE);
        } else {
            mContinuationCard.setVisibility(View.GONE);
        }

        mEndTime.setVisibility(View.GONE);
        gymId = userOtherInfoData.getCard().getGym_id();
        if (!StringUtils.isEmpty(userOtherInfoData.getCard().getEnd_time())) {
            mEndTime.setVisibility(View.VISIBLE);
            Date date = DateUtils.parseString("yyyy-MM-dd", userOtherInfoData.getCard().getEnd_time());
            String endTime = DateUtils.formatDate("yyyy.MM.dd到期", date);
            mEndTime.setText(endTime);
        }

        if (LikingPreference.isBind()) {
            mBraceletData.setText(userOtherInfoData.getAllDistance());
        }

        //mWaterRateLinearLayout.setVisibility(View.GONE);
        // mWaterSurplus.setVisibility(View.GONE);

        if (userOtherInfoData.getWaterData() != null) {
            if (userOtherInfoData.getWaterData().getWater_status() == MyUserOtherInfoResult.UserOtherInfoData.WaterData.CHARGE_WATER) {
//                mWaterRateLinearLayout.setVisibility(View.VISIBLE);
//                mWaterSurplus.setVisibility(View.VISIBLE);
//                mWaterSurplus.setText(getString(R.string.time_remaining) + userOtherInfoData.getWaterData().getWater_time() + getString(R.string.min));
                addWater(mPersonEntities, mMyWaterEntity, 5);
                updateAdapter(mPersonEntities);
            } else {
//                mWaterRateLinearLayout.setVisibility(View.GONE);
//                mWaterSurplus.setVisibility(View.GONE);
                removeWater(mPersonEntities, mMyWaterEntity, 5);
                updateAdapter(mPersonEntities);
            }
        }

        setHeadPersonData();
    }

    @Override
    public void updateExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        mStateView.setState(StateView.State.SUCCESS);
        doExerciseData(exerciseData);
    }


    /**
     * 处理
     *
     * @param exerciseData
     */
    private void doExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mBodyScoreData.setText(exerciseData.getScore());
            mContentTextViewMin.setText(exerciseData.getTotal_min());
            mContentTextViewDay.setText(exerciseData.getSport_date());
            mContentTextViewTime.setText(exerciseData.getTotal_times());
            int level = exerciseData.getIs_up();
            if (level == SPORT_DOWN) {
                mBodyLevelImageView.setVisibility(View.VISIBLE);
                mBodyLevelImageView.setImageResource(R.drawable.icon_decline);
            } else if (level == SPORT_UP) {
                mBodyLevelImageView.setVisibility(View.VISIBLE);
                mBodyLevelImageView.setImageResource(R.drawable.icon_rise);
            } else if (level == SPORT_NORMAL) {
                mBodyLevelImageView.setVisibility(View.VISIBLE);
                mBodyLevelImageView.setImageResource(R.drawable.icon_hold_the_line);
            } else if (level == SPORT_NULL) {
                mBodyLevelImageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 清除训练数据
     */
    private void clearExerciseData() {
        if (mBodyScoreData != null) {
            mBodyScoreData.setText("--");
            mContentTextViewMin.setText("--");
            mContentTextViewDay.setText("--");
            mContentTextViewTime.setText("--");
            mBodyLevelImageView.setVisibility(View.GONE);
        }
    }


    private void setLogonView() {
        if (LikingPreference.isLogin()) {
            mLoginBtn.setVisibility(android.view.View.GONE);
            mPersonNameTextView.setVisibility(android.view.View.VISIBLE);
            mPersonPhoneTextView.setVisibility(android.view.View.VISIBLE);
            mPersonNameTextView.setText(LikingPreference.getNickName());
            mPersonPhoneTextView.setText(LikingPreference.getUserPhone());
            if (!StringUtils.isEmpty(LikingPreference.getUserIconUrl())) {
                HImageLoaderSingleton.loadImage(mHeadHImageView, LikingPreference.getUserIconUrl(), getActivity());
                HImageLoaderSingleton.loadImage(mHImageViewBackground, LikingPreference.getUserIconUrl(), getActivity());
            }
            if (LikingPreference.isVIP()) {
                mIsVip.setVisibility(android.view.View.VISIBLE);
            } else {
                mIsVip.setVisibility(android.view.View.GONE);
            }

            mContinuationCard.setVisibility(View.GONE);
            mEndTime.setVisibility(View.GONE);

        } else {
            mLoginBtn.setVisibility(android.view.View.VISIBLE);
            mPersonNameTextView.setVisibility(android.view.View.GONE);
            mPersonPhoneTextView.setVisibility(android.view.View.GONE);
            mIsVip.setVisibility(android.view.View.GONE);
            mHeadHImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_head_default_image));
            HImageLoaderSingleton.loadImage(mHImageViewBackground, "", getActivity());
            mContinuationCard.setVisibility(View.GONE);
            mEndTime.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        mStateView = (LikingStateView) view.findViewById(R.id.my_state_view);
        mMyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_my);
        mMyAdapter = new MyPersonAdapter(getActivity());
        mPersonEntities = new ArrayList<>();

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_my_head, null);
        initHeadView(headerView);
        mMyAdapter.setHeaderView(headerView);

        setEntitiesItemListener();

        mMyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mMyRecyclerView.setAdapter(mMyAdapter);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                setLogonView();
                isRetryRequest = true;
                getMyUserInfoOther();
                getUserExerciseData();
            }
        });
    }

    /**
     * 设置主布局的item点击事件
     */
    private void setRecycleViewItem() {
        mPersonEntities.clear();
        String[] array = getActivity().getResources().getStringArray(R.array.my_item_array);
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[0], R.mipmap.my_lesson));
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[1], R.mipmap.my_order));
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[2], R.mipmap.my_coupon));
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[3], R.mipmap.my_card));
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[4], R.mipmap.my_bracelet));
        mMyWaterEntity = new MyPersonAdapter.MyPersonEntity(array[5], R.mipmap.my_water);
        mPersonEntities.add(new MyPersonAdapter.MyPersonEntity(array[6], R.mipmap.my_more));
        updateAdapter(mPersonEntities);
    }

    /**
     * 设置主布局内容
     */
    private void setEntitiesItemListener() {
        mMyAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<MyPersonAdapter.MyPersonEntity>() {
            @Override
            public void onItemClick(int position, MyPersonAdapter.MyPersonEntity data) {
                switch (data.getDrawValue()) {
                    case R.mipmap.my_lesson:
                        if (LikingPreference.isLogin()) {
                            UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYLESSONACTIVITY);
                            Intent myCourseIntent = new Intent(getActivity(), MyLessonActivity.class);
                            startActivity(myCourseIntent);
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                    case R.mipmap.my_order:
                        if (LikingPreference.isLogin()) {
                            Intent myOrderIntent = new Intent(getActivity(), MyOrderActivity.class);
                            startActivity(myOrderIntent);
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                    case R.mipmap.my_coupon:
                        if (LikingPreference.isLogin()) {
                            UMengCountUtil.UmengCount(getActivity(), UmengEventId.COUPONSACTIVITY);
                            Intent couponsIntent = new Intent(getActivity(), CouponsActivity.class);
                            couponsIntent.putExtra(CouponsActivity.TYPE_MY_COUPONS, CouponsActivity.TYPE_MY_COUPONS);
                            startActivity(couponsIntent);
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                    case R.mipmap.my_water:
                        UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.WATERRATEACTIVITY);
                        startActivity(WaterRateActivity.class);
                        break;
                    case R.mipmap.my_bracelet:
                        mPresenter.jumpBraceletActivity(getActivity(), LikingMyFragment.this, UUID, mBraceletMac);
                        break;
                    case R.mipmap.my_more:
                        startActivity(MoreActivity.class);
                        break;
                    case R.mipmap.my_card:
                        startActivity(MyCardActivity.class);
                        break;

                }
            }
        });
    }


    private void initHeadView(View headerView) {
        ButterKnife.bind(this, headerView);
        setHeadPersonData();
        setLogonView();
    }

    private void setHeadPersonData() {
        setHeadPersonDataView(mBodyScoreLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
        setHeadPersonDataView(mBraceletLinearLayout, R.string.everyday_sport_title, R.string.everyday_sport_unit);
        setHeadAllSportView(mAllSportLayout, R.string.today_train_data, new int[]{R.string.Mins, R.string.Days, R.string.Times});

        if (LikingPreference.isBind()) {//已绑定
            mBraceletLinearLayout.setVisibility(View.VISIBLE);
            setCenter(false);

        } else {//没有绑定
            mBraceletLinearLayout.setVisibility(View.GONE);
            setCenter(true);
        }
    }

    /**
     * 设置头部个人数据
     *
     * @param view
     * @param title
     * @param unitText
     */
    private void setHeadPersonDataView(android.view.View view, int title, int unitText) {
        TextView titleTextView = (TextView) view.findViewById(R.id.person_body_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.text_point);
        TextView unitTextView = (TextView) view.findViewById(R.id.text_point_unit);
        ImageView bg = (ImageView) view.findViewById(R.id.bg_image);
        ImageView leverImage = (ImageView) view.findViewById(R.id.person_body_level);
        titleTextView.setText(title);
        unitTextView.setText(unitText);
        unitTextView.setTypeface(mTypeface);
        contentTextView.setTypeface(mTypeface);
        switch (view.getId()) {
            case R.id.layout_body_score:
                leverImage.setVisibility(View.VISIBLE);
                mBodyLevelImageView = leverImage;
                mBodyScoreData = contentTextView;
                bg.setImageResource(R.mipmap.bg_person);
                mBodyScoreDataTitle = titleTextView;
                mBodyScoreDataContent = (LinearLayout) view.findViewById(R.id.person_body_content);
                break;
            case R.id.layout_bracelet:
                leverImage.setVisibility(View.GONE);
                mBraceletData = contentTextView;
                bg.setImageResource(R.mipmap.bg_bracelet);
                break;
        }
    }


    /**
     * 设置头部个人数据
     *
     * @param view
     * @param title
     * @param unitText
     */
    private void setHeadAllSportView(View view, int title, int[] unitText) {
        TextView titleTextView = (TextView) view.findViewById(R.id.person_body_title);
        mContentTextViewMin = (TextView) view.findViewById(R.id.text_point_1);
        TextView unitTextViewMin = (TextView) view.findViewById(R.id.text_point_unit_1);
        mContentTextViewDay = (TextView) view.findViewById(R.id.text_point_2);
        TextView unitTextViewDay = (TextView) view.findViewById(R.id.text_point_unit_2);
        mContentTextViewTime = (TextView) view.findViewById(R.id.text_point_3);
        TextView unitTextViewTime = (TextView) view.findViewById(R.id.text_point_unit_3);

        ImageView bg = (ImageView) view.findViewById(R.id.bg_image);
        titleTextView.setText(title);
        unitTextViewMin.setText(unitText[0]);
        unitTextViewDay.setText(unitText[1]);
        unitTextViewTime.setText(unitText[2]);

        mContentTextViewMin.setTypeface(mTypeface);
        mContentTextViewDay.setTypeface(mTypeface);
        mContentTextViewTime.setTypeface(mTypeface);
        unitTextViewMin.setTypeface(mTypeface);
        unitTextViewDay.setTypeface(mTypeface);
        unitTextViewTime.setTypeface(mTypeface);

        bg.setImageResource(R.mipmap.bg_sport);
    }


    @OnClick({R.id.layout_all_sport,
            R.id.login_text,
            R.id.layout_head_info,
            R.id.head_image,
            R.id.layout_body_score,
            R.id.continuation_card,
            R.id.layout_bracelet})
    public void onClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.layout_all_sport://我的训练数据
                if (LikingPreference.isLogin()) {
                    Intent intent = new Intent(getActivity(), SportDataActivity.class);
//                    intent.setAction(SportDataDayFragment.SHOW_ACTION);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SportDataDayFragment.SPORT_MINS, mContentTextViewMin.getText().toString());
//                    bundle.putString(SportDataDayFragment.SPORT_DAYS, mContentTextViewDay.getText().toString());
//                    bundle.putString(SportDataDayFragment.SPORT_TIMES, mContentTextViewTime.getText().toString());
//                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.login_text://登录
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_head_info://个人信息
            case R.id.head_image:
                if (!LikingPreference.isLogin()) {
                    startActivity(LoginActivity.class);
                } else {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYINFOACTIVITY);
                    Intent myInfoIntent = new Intent(getActivity(), MyInfoActivity.class);
                    myInfoIntent.putExtra(LoginActivity.KEY_TITLE_SET_USER_INFO, getString(R.string.change_person_info));
                    startActivity(myInfoIntent);
                }
                break;
            case R.id.layout_body_score:
                if (LikingPreference.isLogin()) {
                    mPresenter.jumpBodyTestActivity(getActivity());
                } else {
                    startActivity(LoginActivity.class);
                }
                break;

            case R.id.continuation_card: //续卡
                if (StringUtils.isEmpty(gymId)) {
                    return;
                }
                Intent intentUp = new Intent(getActivity(), UpgradeAndContinueCardActivity.class);
                intentUp.putExtra(LikingBuyCardFragment.KEY_BUY_TYPE, NumberConstantUtil.TWO);
                intentUp.putExtra(MyCardActivity.KEY_INTENT_TITLE, getString(R.string.flow_card));
                intentUp.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
                startActivity(intentUp);
                break;
            case R.id.layout_bracelet://手环数据
                Intent braceletIntent = new Intent(getActivity(), BraceletDataActivity.class);
                braceletIntent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac);
                braceletIntent.putExtra(LikingMyFragment.KEY_UUID, UUID);
                startActivity(braceletIntent);
                break;
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }


    public void onEvent(LoginOutFialureMessage message) {
        setLogonView();
        clearExerciseData();
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            mStateView.setState(StateView.State.SUCCESS);
            setLogonView();
            getUserExerciseData();
        }
    }

    public void onEvent(LoginOutMessage loginOutMessage) {
        if (loginOutMessage != null) {
            //setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
            setHeadPersonData();
            setLogonView();
            clearExerciseData();
        }
    }

    /**
     * 打开蓝牙的回调
     *
     * @param requestCode 1
     * @param resultCode  0 为失败  -1 为成功
     * @param data        null
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                case 0:
                    break;
                case -1:
                    jumpMyBraceletActivity();
                    break;
            }
        }
    }


    @Override
    public void jumpMyBraceletActivity() {
        UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYBRACELETACTIVITY);
        Intent intent = new Intent(getActivity(), MyBraceletActivity.class);
        if (!StringUtils.isEmpty(mBraceletMac)) {
            intent.putExtra(LikingMyFragment.KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
        }
        intent.putExtra(LikingMyFragment.KEY_UUID, UUID);
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, "");
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, "");
        intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "LikingMyFragment");
        getActivity().startActivity(intent);
    }

    public void addWater(List<MyPersonAdapter.MyPersonEntity> list, MyPersonAdapter.MyPersonEntity entity, int index) {
        if (list.contains(entity)) {
            return;
        }

        list.add(list.get(list.size() - 1));
        for (int i = list.size() - 1; i > index; i--) {
            list.set(i, list.get(i - 1));
        }
        list.set(index, entity);
    }

    public void removeWater(List<MyPersonAdapter.MyPersonEntity> list, MyPersonAdapter.MyPersonEntity entity, int index) {
        if (!list.contains(entity)) {
            return;
        }
        for (int i = index; i < list.size() - 2; i++) {
            list.set(i, list.get(i + 1));
        }
        list.remove(list.size() - 1);
    }

    public void updateAdapter(List<MyPersonAdapter.MyPersonEntity> list) {
        mMyAdapter.setDatas(list);
        mMyAdapter.notifyDataSetChanged();
    }

    public void setPresenter() {
        mPresenter = new LikingMyContract.Presenter();
    }


    public void setCenter(boolean isCenter) {
        if (isCenter) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBodyScoreDataTitle.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mBodyScoreDataTitle.setLayoutParams(lp);
            mBodyScoreDataContent.setGravity(Gravity.CENTER);
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBodyScoreDataTitle.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            mBodyScoreDataTitle.setLayoutParams(lp);
            mBodyScoreDataContent.setGravity(Gravity.LEFT);
        }
    }

}
