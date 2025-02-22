package com.loneyyao.logview;

import com.loneyyao.logview.model.LogFilterCondition;
import com.loneyyao.logview.model.LogItem;
import com.loneyyao.logview.model.LogLevel;
import com.loneyyao.logview.util.ListUtil;
import com.loneyyao.logview.util.Stabilizer;
import com.loneyyao.logview.util.StringUtil;

import java.util.LinkedList;

/**
 * The core of log.Just logic.No relationship of ui.
 * <p/>
 * Created by fanhl on 16/5/3.
 */
public class LogCore {
    private static final int STABILIZER_TIME = 100;
    private static int LIMIT_LENGTH = 100;

    private static final Stabilizer stabilizer;

    private static final LinkedList<LogItem> fullLogs;
    private static final LinkedList<LogItem> filteredLogs;
    private static final LinkedList<LogItem> bufferFullLogs;
    private static final LinkedList<LogItem> bufferFilteredLogs;

    private static final ListUtil.Filter<LogItem> logItemFilter;

    private static LogFilterCondition logFilterCondition;

    private static Callback callback;

    static {
        stabilizer = new Stabilizer(STABILIZER_TIME);

        fullLogs = new LinkedList<LogItem>();// Collections.synchronizedList(new LinkedList<LogItem>());
        filteredLogs = new LinkedList<LogItem>();// Collections.synchronizedList(new LinkedList<LogItem>());
        bufferFullLogs = new LinkedList<LogItem>();//Collections.synchronizedList(new LinkedList<LogItem>());
        bufferFilteredLogs = new LinkedList<LogItem>();//Collections.synchronizedList(new LinkedList<LogItem>());

        logItemFilter = new ListUtil.Filter<LogItem>() {
            @Override
            public boolean filter(LogItem logItem) {
                if (logFilterCondition == null) return true;
                if (logFilterCondition.getQuery()==null) {
                    return logItem.getLevel().getIndex() >= logFilterCondition.getLogLevel().getIndex();
                }else{
                    return logItem.getLevel().getIndex() >= logFilterCondition.getLogLevel().getIndex()
                            && ((logItem.getMessage() != null && logItem.getMessage().contains(logFilterCondition.getQuery()))
                            || (logItem.getTag() != null && logItem.getTag().contains(logFilterCondition.getQuery())))
                            ;

                }
            }
        };

        logFilterCondition = new LogFilterCondition(LogLevel.D, "");
    }

    /**
     * fixme synchronized logic
     *
     * @param logItem
     */
    public static void addLog(LogItem logItem) {
        synchronized (LogCore.class) {
            //add buffer log
            bufferFullLogs.add(logItem);
            while (bufferFullLogs.size() > LIMIT_LENGTH) bufferFullLogs.poll();


            //refresh log ui per 100 milliseconds
            if (!stabilizer.check()) return;
            stabilizer.actived();


            //add log from butter log
            fullLogs.addAll(bufferFullLogs);

            ListUtil.filter(bufferFullLogs, bufferFilteredLogs, logItemFilter);
            filteredLogs.addAll(bufferFilteredLogs);

            bufferFullLogs.clear();
            bufferFilteredLogs.clear();

            while (fullLogs.size() > LIMIT_LENGTH) fullLogs.poll();
            while (filteredLogs.size() > LIMIT_LENGTH) filteredLogs.poll();

            if (callback != null) callback.onFilteredLogsChanged();
        }
    }

    public static void setCallback(Callback callback) {
        LogCore.callback = callback;
    }

    public static void setLogCount(int count){
        LIMIT_LENGTH = count;
    }

    public static void setLogFilterCondition(LogFilterCondition logFilterCondition) {
        //if same
        if (LogCore.logFilterCondition != null && LogCore.logFilterCondition.getLogLevel() == logFilterCondition.getLogLevel() && StringUtil.equals(LogCore.logFilterCondition.getQuery(), logFilterCondition.getQuery())) {
            return;
        }
        LogCore.logFilterCondition.setLogLevel(logFilterCondition.getLogLevel());
        LogCore.logFilterCondition.setQuery(logFilterCondition.getQuery());

        synchronized (LogCore.class) {
            filteredLogs.clear();
            ListUtil.filter(fullLogs, filteredLogs, logItemFilter);
        }

        if (callback != null) callback.onLogFilterConditionChanged();
    }

    public static LinkedList<LogItem> getFilteredLogs() {
        return filteredLogs;
    }

    public static void setLogPerTime(long mill) {
        stabilizer.setStabilizerTime(mill);
    }

    public interface Callback {
        void onFilteredLogsChanged();

        void onLogFilterConditionChanged();
    }
}
