package com.loneyyao.logview;

import android.app.Activity;

import com.loneyyao.logview.model.LogItem;
import com.loneyyao.logview.model.LogLevel;

/**
 * The Api.
 * <p/>
 * Created by fanhl on 16/4/29.
 */
public class LogViewApi {

    private static boolean enable = true;

    public static void bind(Activity activity) {
        if (!enable) return;
        LogViewUI.bind(activity);
    }

    public static void setEnable(boolean enable) {
        LogViewApi.enable = enable;
    }

    public static void v(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.V, tag, msg));
    }

    public static void d(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.D, tag, msg));
    }

    public static void i(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.I, tag, msg));
    }

    public static void w(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.W, tag, msg));
    }

    public static void e(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.E, tag, msg));
    }

    public static void a(String tag, String msg) {
        if (!enable) return;
        LogCore.addLog(new LogItem(System.currentTimeMillis(), LogLevel.A, tag, msg));
    }

    public static void setLogPerTime(long mill){
        LogCore.setLogPerTime(mill);
    }

    public static void setLogCount(int count){
        LogCore.setLogCount(count);
    }

}
