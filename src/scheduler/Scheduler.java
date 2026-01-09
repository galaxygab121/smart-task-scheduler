package scheduler;

import java.util.List;
import metrics.MetricsCalculator.ScheduledTask;
import model.Task;

public interface Scheduler {
    String name();
    List<ScheduledTask> schedule(List<Task> tasks);
}
