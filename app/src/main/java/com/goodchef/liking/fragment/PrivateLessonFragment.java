package com.goodchef.liking.fragment;

import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.adapter.PrivateLessonAdapter;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.MyPrivateCoursesPresenter;
import com.goodchef.liking.mvp.view.MyPrivateCoursesView;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:43
 */
public class PrivateLessonFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyPrivateCoursesView {

    private PrivateLessonAdapter mPrivateLessonAdapter;

    private MyPrivateCoursesPresenter mMyPrivateCoursesPresenter;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    private void sendRequest(int page) {
        mMyPrivateCoursesPresenter = new MyPrivateCoursesPresenter(getActivity(), this);
        mMyPrivateCoursesPresenter.getMyPrivateCourses(page, PrivateLessonFragment.this);
    }


    @Override
    protected void initViews() {
        setPullType(PullMode.PULL_BOTH);
    }

    @Override
    public void updatePrivateCoursesView(MyPrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        List<MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> list = privateCoursesData.getPrivateCoursesList();
        if (list != null && list.size() > 0) {
            mPrivateLessonAdapter = new PrivateLessonAdapter(getActivity());
            mPrivateLessonAdapter.setData(list);
            setRecyclerAdapter(mPrivateLessonAdapter);
        }
    }
}
