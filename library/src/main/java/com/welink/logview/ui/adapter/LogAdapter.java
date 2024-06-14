package com.welink.logview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import com.welink.logview.LogCore;
import com.welink.logview.R;
import com.welink.logview.model.LogItem;
import com.welink.logview.model.LogLevel;
import com.welink.logview.ui.base.ClickableRecyclerViewAdapter;

import java.util.LinkedList;

/**
 * A adapter to show log.
 * <p/>
 * Created by fanhl on 16/4/29.
 */
public class LogAdapter extends ClickableRecyclerViewAdapter<LogAdapter.ViewHolder> {


    private final LinkedList<LogItem> list;

    public LogAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        list = LogCore.getFilteredLogs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.logview_item_log, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends ClickableRecyclerViewAdapter.ClickableViewHolder {

        private final TextView mLog;

        private LogItem data;

        public ViewHolder(View itemView) {
            super(itemView);
            mLog = ((TextView) itemView.findViewById(R.id.log));
        }

        public void bind(LogItem data) {
            mLog.setText(data.getLog());

            if (data.getLevel() == LogLevel.V) {
                mLog.setTextColor(getColor(R.color.logview_log_verbose));
            } else if (data.getLevel() == LogLevel.D) {
                mLog.setTextColor(getColor(R.color.logview_log_debug));
            } else if (data.getLevel() == LogLevel.I) {
                mLog.setTextColor(getColor(R.color.logview_log_info));
            } else if (data.getLevel() == LogLevel.W) {
                mLog.setTextColor(getColor(R.color.logview_log_warning));
            } else if (data.getLevel() == LogLevel.E) {
                mLog.setTextColor(getColor(R.color.logview_log_error));
            } else if (data.getLevel() == LogLevel.A) {
                mLog.setTextColor(getColor(R.color.logview_log_assert));
            }

            this.data = data;
        }

        private int getColor(@ColorRes int colorResId) {
            return itemView.getContext().getResources().getColor(colorResId);
        }

        public LogItem getData() {
            return data;
        }
    }
}
