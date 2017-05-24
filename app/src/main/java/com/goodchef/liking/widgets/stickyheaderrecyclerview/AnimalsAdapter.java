package com.goodchef.liking.widgets.stickyheaderrecyclerview;

import android.support.v7.widget.RecyclerView;

import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class AnimalsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private ArrayList<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> items = new ArrayList<>();

    public AnimalsAdapter() {
        setHasStableIds(true);
    }

    public void add(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(String object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
