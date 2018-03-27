package io.onevzh.gradle.tasktimer

import org.gradle.api.tasks.TaskState

/**
 * Created by oneVzh on 2018/3/27.
 */
class TaskTime {
    long ms
    String path
    TaskState state

    TaskTime(long ms, String path, TaskState state) {
        this.ms = ms
        this.path = path
        this.state = state
    }
}
