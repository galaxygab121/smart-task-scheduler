package simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metrics.MetricsCalculator.ScheduledTask;
import model.Task;

public class GanttPrinter {

    // Each time unit prints as a fixed-width block: 4 chars like "[A ]" or "[A!]"
    private static final int BLOCK_WIDTH = 4;

    /**
     * Prints:
     * - Boxed Gantt timeline: [A ] [B!] ...
     * - Time markers aligned with boundaries
     * - Legend: ID -> (priority, deadline, runtime) + missed deadline info
     */
    public static void print(List<ScheduledTask> schedule) {
        if (schedule.isEmpty()) {
            System.out.println("Timeline: (no tasks)");
            return;
        }

        // Track missed deadlines by task id
        Map<String, Boolean> missedById = new HashMap<>();
        // Track first-seen task details for legend
        Map<String, Task> taskById = new HashMap<>();

        for (ScheduledTask st : schedule) {
            Task t = st.getTask();
            taskById.putIfAbsent(t.getId(), t);
            boolean missed = st.getFinishTime() > t.getDeadline();
            missedById.put(t.getId(), missed);
        }

        StringBuilder timeline = new StringBuilder();
        StringBuilder markers  = new StringBuilder();

        String prefixTimeline = "Timeline: ";
        String prefixTime     = "Time:     ";

        timeline.append(prefixTimeline);
        markers.append(prefixTime);

        int currentTime = 0;

        for (ScheduledTask st : schedule) {
            Task t = st.getTask();
            String id = t.getId();

            // Fill gaps (shouldn't happen in current model) with empty blocks
            while (currentTime < st.getStartTime()) {
                timeline.append(block("..", false)); // gap
                currentTime++;
            }

            // Add blocks for runtime
            int runtime = t.getEstimatedRuntime();
            boolean missed = missedById.getOrDefault(id, false);

            for (int i = 0; i < runtime; i++) {
                timeline.append(block(id, missed));
                currentTime++;
            }
        }

        // Build time markers aligned under timeline boundaries
        // Place 0 at beginning
        placeNumber(markers, 0, 0, prefixTime.length());

        // Place each task finish time at its boundary
        for (ScheduledTask st : schedule) {
            placeNumber(markers, st.getFinishTime(), st.getFinishTime(), prefixTime.length());
        }

        // Print timeline + markers
        System.out.println("Gantt: [X ] = on-time, [X!] = finishes after deadline");
        System.out.println(timeline);
        System.out.println(markers);

        // Print legend
        System.out.println("Legend:");
        taskById.keySet().stream()
        .sorted()
        .forEach(id -> {
            Task t = taskById.get(id);
            boolean missed = missedById.getOrDefault(id, false);
            System.out.printf("  %s -> (p=%d, d=%d, r=%d)%s%n",
                    id,
                    t.getPriority(),
                    t.getDeadline(),
                    t.getEstimatedRuntime(),
                    missed ? "  MISSED" : "");
        });

    }

    /**
     * Creates a fixed-width block.
     * If id is longer than 1 char, we display the first char to keep alignment.
     * Missed deadline tasks show [X!], otherwise [X ].
     */
    private static String block(String id, boolean missed) {
        String label = id;
        if (label == null || label.isEmpty()) label = "?";
        if (label.length() > 1) label = label.substring(0, 1);

        // Two-character inside area: char + flag
        char flag = missed ? '!' : ' ';
        return "[" + label + flag + "]";
    }

    /**
     * Places a number into the markers line aligned with a given time offset.
     * Each time unit corresponds to one BLOCK_WIDTH chars in timeline.
     */
    private static void placeNumber(StringBuilder markers, int number, int timeOffset, int baseIndex) {
        String s = String.valueOf(number);

        int charIndex = baseIndex + (timeOffset * BLOCK_WIDTH);

        // Ensure markers is long enough
        while (markers.length() < charIndex + s.length()) {
            markers.append(" ");
        }

        // Write number chars
        for (int i = 0; i < s.length(); i++) {
            markers.setCharAt(charIndex + i, s.charAt(i));
        }
    }
}

