package io.onevzh.gradle.task_timer

import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

/**
 * Created by oneVzh on 2018/3/23.
 */
class TaskTimerListener extends BuildAdapter implements TaskExecutionListener {
    private long taskStartTime
    private List<TaskTime> timeList = []

    @Override
    void beforeExecute(Task task) {
        taskStartTime = System.currentTimeMillis()
    }

    @Override
    void afterExecute(Task task, TaskState state) {
        timeList.add(new TaskTime(System.currentTimeMillis() - taskStartTime, state))
    }

    @Override
    void buildFinished(BuildResult result) {


    }


    static class TaskTime {
        long ms
        TaskState state
    }

}
