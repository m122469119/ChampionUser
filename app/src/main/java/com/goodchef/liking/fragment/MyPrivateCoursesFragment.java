package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.MyPrivateCoursesDetailsActivity;
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
public class MyPrivateCoursesFragment extends NetworkPagerLoaderRecyclerViewFragment implements MyPrivateCoursesView {

    public static final String KEY_ORDER_ID = "key_order_id";
    private PrivateLessonAdapter mPrivateLessonAdapter;

    private MyPrivateCoursesPresenter mMyPrivateCoursesPresenter;

    @Override
    protected void requestData(int page) {
        sendRequest(page);
    }

    private void sendRequest(int page) {
        mMyPrivateCoursesPresenter = new MyPrivateCoursesPresenter(getActivity(), this);
        mMyPrivateCoursesPresenter.getMyPrivateCourses(page, MyPrivateCoursesFragment.this);
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
            mPrivateLessonAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.private_teacher_name);
                    if (textView != null) {
                        MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses privateCourses = (MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses) textView.getTag();
                        if (privateCourses != null) {
                            Intent intent = new Intent(getActivity(), MyPrivateCoursesDetailsActivity.class);
                            intent.putExtra(KEY_ORDER_ID,privateCourses.getOrderId());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, int position) {
                    return false;
                }
            });
        }
    }
}
