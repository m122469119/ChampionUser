package com.goodchef.liking.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:34
 * version 1.0.0
 */

public class AnnouncementFragment extends BaseFragment {

    CoursesResult.Courses.Gym mNoticeGym;
    @BindView(R.id.notice_name)
    TextView mNoticeName;
    @BindView(R.id.notice_content)
    TextView mNoticeContent;
    @BindView(R.id.announcement_stateView)
    LikingStateView mAnnouncementStateView;

    public static AnnouncementFragment newInstance(CoursesResult.Courses.Gym mNoticeGym) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putSerializable(MessageActivity.NOTICE_DATA, mNoticeGym);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);
        mNoticeGym = (CoursesResult.Courses.Gym) getArguments().getSerializable(MessageActivity.NOTICE_DATA);
        ButterKnife.bind(this, view);
        showNotice();
        return view;
    }

    private void showNotice() {
        if (!StringUtils.isEmpty(mNoticeGym.getAnnouncementId()) && !StringUtils.isEmpty(mNoticeGym.getAnnouncementInfo())) {
            mAnnouncementStateView.setState(StateView.State.SUCCESS);
            mNoticeName.setText(mNoticeGym.getName());
            mNoticeContent.setText(mNoticeGym.getAnnouncementInfo());
            LikingPreference.setAnnouncementId(mNoticeGym.getAnnouncementId());
        } else {
            //没有数据
            setNoDataView();
        }
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.no_notices_data);
        noDataText.setText("暂时没有公告");
        refreshView.setVisibility(View.INVISIBLE);
        mAnnouncementStateView.setNodataView(noDataView);
        mAnnouncementStateView.setState(StateView.State.NO_DATA);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
