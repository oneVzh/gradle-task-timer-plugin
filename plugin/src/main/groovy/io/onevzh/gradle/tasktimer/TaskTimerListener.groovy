package io.onevzh.gradle.tasktimer

import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

import java.util.concurrent.TimeUnit

/**
 * Created by oneVzh on 2018/3/23.
 */
class TaskTimerListener extends BuildAdapter implements TaskExecutionListener {
    private static final String UNICODE_SQUARE = "▇"
    private static final String FILL = " "

    private long taskStartTime
    private List<TaskTime> timeList = []

    @Override
    void beforeExecute(Task task) {
        taskStartTime = System.currentTimeMillis()
    }

    @Override
    void afterExecute(Task task, TaskState state) {
        timeList.add(new TaskTime(System.currentTimeMillis() - taskStartTime, task.path, state))
    }

    @Override
    void buildFinished(BuildResult result) {
        formatTable(timeList)
        println(result.failure ? "== Build Failure ==" : "== Build Successful ==")
    }

    static void formatTable(List<TaskTime> list) {
        if (list.size() <= 0) return
        long totalTime = list.sum {
            it.ms
        }
        long maxTime = list.max { it.ms }.ms
        println("== Total task time: ${formatTime(totalTime, 0)}")
        int format = getTimeFormat(maxTime)
        list.forEach({
            printRow(it, totalTime, maxTime, format)
        })
    }

    static void printRow(TaskTime item, long totalTime, long maxTime, int format) {
        int spaceCount = (int) Math.ceil(item.ms / maxTime * 40)
        println(sprintf("%s%s %2d%%(%s) %s",
                FILL * (40 - spaceCount),
                UNICODE_SQUARE * spaceCount,
                (int) Math.ceil(item.ms / totalTime * 100),
                formatTime(item.ms, format),
                item.path))
    }

    static String formatTime(long ms, int format) {
        int minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        int seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - minutes * 60
        int millis = TimeUnit.MILLISECONDS.toMillis(ms) - seconds * 1000 - minutes * 60 * 1000
        switch (format) {
            case 0:
                return sprintf("%2d:%02d.%03d", minutes, seconds, millis)
            case 1:
                return sprintf("%2d.%03d s", seconds, millis)
            case 2:
                return sprintf("%3d ms", millis)
            default:
                return sprintf("%2d:%02d.%03d", minutes, seconds, millis)
        }
    }

    static int getTimeFormat(long maxTime) {
        int minutes = TimeUnit.MILLISECONDS.toMinutes(maxTime)
        if (minutes > 0) {
            return 0 // ' 0:00:000'
        }
        int seconds = TimeUnit.MILLISECONDS.toSeconds(maxTime) - minutes * 60
        if (seconds > 0) {
            return 1 // ' 0.000s'
        } else {
            return 2 // '  0ms'
        }
    }

}
