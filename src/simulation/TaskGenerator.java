package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Task;

public class TaskGenerator {

    /**
     * Generate N tasks with random priority, deadline, and runtime.
     *
     * Design choices:
     * - priority: 1..10
     * - runtime: 1..maxRuntime
     * - deadline: runtime + random slack up to maxSlack, plus current index-ish offset
     *
     * This creates a realistic mix where some tasks will miss deadlines depending on schedule.
     */
    public static List<Task> generate(int n, int maxPriority, int maxRuntime, int maxSlack, long seed) {
        Random rand = new Random(seed);
        List<Task> tasks = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            String id = "T" + (i + 1);

            int priority = 1 + rand.nextInt(maxPriority);
            int runtime = 1 + rand.nextInt(maxRuntime);

            // Slack controls how tight deadlines are
            int slack = rand.nextInt(maxSlack + 1);

            // Create a deadline that *sometimes* is tight
            // Using i/5 adds mild growth to deadlines to avoid everything missing.
            int deadline = runtime + slack + (i / 5);

            tasks.add(new Task(id, priority, deadline, runtime));
        }

        return tasks;
    }
}
