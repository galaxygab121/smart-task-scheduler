package metrics;

import java.util.List;
import model.Task;

public class MetricsCalculator {

    public static class ScheduledTask {
        private final Task task;
        private final int startTime;
        private final int finishTime;

        public ScheduledTask(Task task, int startTime, int finishTime) {
            this.task = task;
            this.startTime = startTime;
            this.finishTime = finishTime;
        }

        public Task getTask() { return task; }
        public int getStartTime() { return startTime; }
        public int getFinishTime() { return finishTime; }

        public int waitTime() { return startTime; }
        public int turnaroundTime() { return finishTime; }

        @Override
        public String toString() {
            return String.format("%s | start=%d finish=%d wait=%d turnaround=%d",
                    task.getId(), startTime, finishTime, waitTime(), turnaroundTime());
        }
    }

    public static double averageWaitTime(List<ScheduledTask> schedule) {
        if (schedule.isEmpty()) return 0.0;
        long total = 0;
        for (ScheduledTask st : schedule) total += st.waitTime();
        return total / (double) schedule.size();
    }

    public static double averageTurnaroundTime(List<ScheduledTask> schedule) {
        if (schedule.isEmpty()) return 0.0;
        long total = 0;
        for (ScheduledTask st : schedule) total += st.turnaroundTime();
        return total / (double) schedule.size();
    }

    public static int missedDeadlines(List<ScheduledTask> schedule) {
        int missed = 0;
        for (ScheduledTask st : schedule) {
            if (st.getFinishTime() > st.getTask().getDeadline()) missed++;
        }
        return missed;
    }
}
