package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aaron.android.framework.base.BaseFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.PrivateLessonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午4:43
 */
public class PrivateLessonFragment extends BaseFragment {
    private ListView mListView;
    private PrivateLessonAdapter mPrivateLessonAdapter;

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
        for (int i = 1; i < 30; i++) {
            list.add(i + "");
        }

        mPrivateLessonAdapter = new PrivateLessonAdapter(getActivity());
        mPrivateLessonAdapter.setData(list);
        mListView.setAdapter(mPrivateLessonAdapter);
    }
}
