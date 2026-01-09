package model;

public class Task {
    private final String id;
    private final int priority;          // higher = more important
    private final int deadline;          // "time units" from 0 (smaller = sooner)
    private final int estimatedRuntime;  // duration in time units

    public Task(String id, int priority, int deadline, int estimatedRuntime) {
        if (priority < 0) throw new IllegalArgumentException("priority must be >= 0");
        if (deadline < 0) throw new IllegalArgumentException("deadline must be >= 0");
        if (estimatedRuntime <= 0) throw new IllegalArgumentException("estimatedRuntime must be > 0");

        this.id = id;
        this.priority = priority;
        this.deadline = deadline;
        this.estimatedRuntime = estimatedRuntime;
    }

    public String getId() { return id; }
    public int getPriority() { return priority; }
    public int getDeadline() { return deadline; }
    public int getEstimatedRuntime() { return estimatedRuntime; }

    @Override
    public String toString() {
        return String.format("Task{id='%s', priority=%d, deadline=%d, runtime=%d}",
                id, priority, deadline, estimatedRuntime);
    }
}
