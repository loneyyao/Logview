package com.fanhl.logview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * UI
 * <p/>
 * Created by fanhl on 16/5/4.
 */
public class LogViewUI {
    public static final String TAG = LogViewUI.class.getSimpleName();

    public static void bind(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.logview_container, null);
        activity.addContentView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
