package com.goodchef.liking.fragment;

import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.adapter.MyGroupLessonAdapter;
import com.goodchef.liking.http.result.MyGroupCoursesResult;
import com.goodchef.liking.mvp.presenter.MyGroupCoursesPresenter;
import com.goodchef.liking.mvp.view.MyGroupCourseView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:42
 */
public class MyGroupLessonFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyGroupCourseView {


    private MyGroupLessonAdapter mGroupLessonAdapter;

    private MyGroupCoursesPresenter mMyGroupCoursesPresenter;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    @Override
    protected void initViews() {
    }

    private void sendRequest(int page) {
        mMyGroupCoursesPresenter = new MyGroupCoursesPresenter(getActivity(), this);
        mMyGroupCoursesPresenter.getMyGroupList(page, MyGroupLessonFragment.this);
    }


    @Override
    public void updateMyGroupCoursesView(MyGroupCoursesResult.MyGroupCoursesData myGroupCoursesData) {
        List<MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> myGroupCoursesDataList = myGroupCoursesData.getMyGroupCourses();
        if (myGroupCoursesDataList != null && myGroupCoursesDataList.size() > 0) {
            setPullType(PullMode.PULL_BOTH);
            mGroupLessonAdapter = new MyGroupLessonAdapter(getActivity());
            mGroupLessonAdapter.setData(myGroupCoursesDataList);
            setRecyclerAdapter(mGroupLessonAdapter);
        }

    }
}
