package com.welink.logview.ui.presenter;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.welink.logview.LogCore;
import com.welink.logview.R;
import com.welink.logview.model.LogFilterCondition;
import com.welink.logview.model.LogItem;
import com.welink.logview.model.LogLevel;
import com.welink.logview.ui.adapter.LogAdapter;
import com.welink.logview.ui.base.ClickableRecyclerViewAdapter;
import com.welink.logview.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanhl on 16/5/8.
 */
public class LogContainerPresenter {

    private LinearLayout logContainer;
    private Spinner typeSpinner;
    private EditText editText;
    private RecyclerView recyclerView;

    private LogAdapter adapter;

    private LogFilterCondition logFilterCondition = new LogFilterCondition();

    public LogContainerPresenter(View view) {
        this.logContainer = (LinearLayout) view.findViewById(R.id.log_container);

        this.typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);


//        typeSpinner.setAdapter();
//        typeSpinner.setp

        this.editText = (EditText) view.findViewById(R.id.editText);
        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                logFilterCondition.setQuery(s.toString());
                LogCore.setLogFilterCondition(logFilterCondition);

            }
        });

        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

    }

    public void initData() {
        List<String> logLevels = new ArrayList<>();
        logLevels.add("verbose");
        logLevels.add("debug");
        logLevels.add("info");
        logLevels.add("warn");
        logLevels.add("error");
        logLevels.add("assert");
        adapter = new LogAdapter(recyclerView.getContext(), recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClickableRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ClickableRecyclerViewAdapter.ClickableViewHolder holder) {
                LogItem data = ((LogAdapter.ViewHolder) holder).getData();
                SystemUtil.copyToClipboard(recyclerView.getContext(), data.getLog());
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(recyclerView.getContext(), R.layout.log_level_item, logLevels) {
            @Nullable
            @Override
            public String getItem(int position) {
                return "Level: "+logLevels.get(position);
            }
        };
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String level = logLevels.get(position);
                switch (level) {
                    case "verbose":
                        logFilterCondition.setLogLevel(LogLevel.V);
                        break;
                    case "debug":
                        logFilterCondition.setLogLevel(LogLevel.D);
                        break;
                    case "info":
                        logFilterCondition.setLogLevel(LogLevel.I);
                        break;
                    case "warn":
                        logFilterCondition.setLogLevel(LogLevel.W);
                        break;
                    case "error":
                        logFilterCondition.setLogLevel(LogLevel.E);
                        break;
                    case "assert":
                        logFilterCondition.setLogLevel(LogLevel.A);
                        break;
                    default:
                        logFilterCondition.setLogLevel(LogLevel.V);
                        break;

                }
                LogCore.setLogFilterCondition(logFilterCondition);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setVisible(boolean visible) {
        if (visible) logContainer.setVisibility(View.VISIBLE);
        else logContainer.setVisibility(View.GONE);
    }

    public void refreshData() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}
