import io.onevzh.gradle.task_timer.TaskTimerListener
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by oneVzh on 2018/3/23.
 */
class TaskTimerPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        project.gradle.addListener(new TaskTimerListener())

    }
}
