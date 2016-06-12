package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aaron.android.framework.base.BaseFragment;
import com.goodchef.liking.R;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingRechargeFragment extends BaseFragment {

    ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mImageView = (ImageView) view.findViewById(R.id.buy_card_image);
      //  Bitmap bitmap = BitmapBase64Util.stringToBitmap("iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MkNEMDBFNzMxNjdCMTFFNkI3NzJFRDJEQzc1NTYyQkQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MkNEMDBFNzQxNjdCMTFFNkI3NzJFRDJEQzc1NTYyQkQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoyQ0QwMEU3MTE2N0IxMUU2Qjc3MkVEMkRDNzU1NjJCRCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoyQ0QwMEU3MjE2N0IxMUU2Qjc3MkVEMkRDNzU1NjJCRCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrAXsI8AAADCSURBVHjaYvz//z/DYAZMDIMcjDqQUsACIlTe1uFVdEe4aWAdCAVsQNwNxDFAzAjEi4G4FIh/wRQQ8gglAFcgIDuwC4jzkPgg9ncgrkDTQ4tsz0hMGozFIp86mklIcOBiLPJzBkUuhoIyaFqIRXJwLSnphRaAEVTVDeZihnG0Lh514GhdPIjrYmI8PpB1MSOpaXC0Lh6ti0fr4tG6eNSBo3XxEO4X4wqYwdIvxllXj9bFo3XxaF08WhePOnCQA4AAAwDKW1Cu6dwYpgAAAABJRU5ErkJggg==");
       // mImageView.setImageBitmap(bitmap);
       // String str = BitmapBase64Util.bitmapToString(bitmap);
       // Log.e("base64", str);
    }








}
