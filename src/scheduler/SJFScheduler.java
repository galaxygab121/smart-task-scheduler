package scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import metrics.MetricsCalculator.ScheduledTask;
import model.Task;

public class SJFScheduler implements Scheduler {

    @Override
    public String name() {
        return "Shortest Job First (SJF)";
    }

    @Override
    public List<ScheduledTask> schedule(List<Task> tasks) {
        List<Task> copy = new ArrayList<>(tasks);

        // Sort: shortest runtime first; tie-breaker: earlier deadline; then higher priority
        copy.sort(Comparator
                .comparingInt(Task::getEstimatedRuntime)
                .thenComparingInt(Task::getDeadline)
                .thenComparing(Comparator.comparingInt(Task::getPriority).reversed()));

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
