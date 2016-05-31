package com.chushi007.android.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aaron.android.framework.base.BaseFragment;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.adapter.GroupLessonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:42
 */
public class GroupLessonFragment extends BaseFragment {

    private ListView mListView;
    private GroupLessonAdapter mGroupLessonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lesson, container, false);
        mListView = (ListView) view.findViewById(R.id.lesson_listView);
        initData();
        return view;
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i=1;i<30;i++){
            list.add(i+"");
        }

        mGroupLessonAdapter = new GroupLessonAdapter(getActivity());
        mGroupLessonAdapter.setData(list);
        mListView.setAdapter(mGroupLessonAdapter);
    }
}
