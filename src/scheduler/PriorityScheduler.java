package scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import metrics.MetricsCalculator.ScheduledTask;
import model.Task;

public class PriorityScheduler implements Scheduler {

    @Override
    public String name() {
        return "Priority (higher first)";
    }

    @Override
    public List<ScheduledTask> schedule(List<Task> tasks) {
        List<Task> copy = new ArrayList<>(tasks);

        // Sort: higher priority first; tie-breaker: earlier deadline
        copy.sort(Comparator
                .comparingInt(Task::getPriority).reversed()
                .thenComparingInt(Task::getDeadline));

        List<ScheduledTask> result = new ArrayList<>();
        int time = 0;

        for (Task t : copy) {
            int start = time;
            int finish = time + t.getEstimatedRuntime();
            result.add(new ScheduledTask(t, start, finish));
            time = finish;
        }

        return result;
    }
}
