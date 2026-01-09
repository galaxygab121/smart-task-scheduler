package simulation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import metrics.MetricsCalculator;
import metrics.MetricsCalculator.ScheduledTask;
import model.Task;
import scheduler.Scheduler;

public class BenchmarkRunner {

    public static void runSingle(List<Scheduler> schedulers, List<Task> tasks) {
        System.out.println("=== Benchmark (" + tasks.size() + " tasks) ===");
        Map<String, Stats> stats = new LinkedHashMap<>();

        for (Scheduler s : schedulers) {
            Stats st = computeStats(s, tasks);
            stats.put(s.name(), st);
        }

        printTable(stats);
        System.out.println();
    }

    /**
     * Runs multiple trials with different seeds and prints mean ± stddev.
     */
    public static void runMultiSeed(
            List<Scheduler> schedulers,
            int nTasks,
            int nSeeds,
            int maxPriority,
            int maxRuntime,
            int maxSlack,
            long baseSeed
    ) {
        System.out.println("=== Benchmark Study ===");
        System.out.println("Tasks per trial: " + nTasks);
        System.out.println("Trials (seeds):  " + nSeeds);
        System.out.println("Params: maxPriority=" + maxPriority + ", maxRuntime=" + maxRuntime + ", maxSlack=" + maxSlack);
        System.out.println();

        Map<String, RunningStats> agg = new LinkedHashMap<>();
        for (Scheduler s : schedulers) {
            agg.put(s.name(), new RunningStats());
        }

        for (int i = 0; i < nSeeds; i++) {
            long seed = baseSeed + i;
            List<Task> tasks = TaskGenerator.generate(nTasks, maxPriority, maxRuntime, maxSlack, seed);

            for (Scheduler s : schedulers) {
                Stats st = computeStats(s, tasks);
                // Store as a vector: [avgWait, avgTurn, missed, missRate]
                agg.get(s.name()).add(st.avgWait, st.avgTurn, st.missedDeadlines, st.missRate);
            }
        }

        printStudyTable(agg);
        System.out.println();
    }

    private static Stats computeStats(Scheduler scheduler, List<Task> tasks) {
        List<ScheduledTask> schedule = scheduler.schedule(tasks);

        double avgWait = MetricsCalculator.averageWaitTime(schedule);
        double avgTurn = MetricsCalculator.averageTurnaroundTime(schedule);
        int missed = MetricsCalculator.missedDeadlines(schedule);
        double missRate = tasks.isEmpty() ? 0.0 : (missed * 100.0 / tasks.size());

        return new Stats(avgWait, avgTurn, missed, missRate);
    }

    private static void printTable(Map<String, Stats> results) {
        String fmtHeader = "%-28s | %12s | %16s | %16s | %10s%n";
        String fmtRow    = "%-28s | %12.2f | %16.2f | %16d | %9.2f%%%n";

        System.out.printf(fmtHeader, "Algorithm", "Avg Wait", "Avg Turnaround", "Missed Deadlines", "Miss Rate");
        System.out.println("-".repeat(28) + "-+-" + "-".repeat(12) + "-+-" + "-".repeat(16) + "-+-" + "-".repeat(16) + "-+-" + "-".repeat(10));

        for (Map.Entry<String, Stats> e : results.entrySet()) {
            Stats v = e.getValue();
            System.out.printf(fmtRow, e.getKey(), v.avgWait, v.avgTurn, v.missedDeadlines, v.missRate);
        }
    }

    private static void printStudyTable(Map<String, RunningStats> agg) {
        String fmtHeader = "%-28s | %18s | %22s | %22s | %16s%n";
        String fmtRow    = "%-28s | %8.2f ± %-6.2f | %10.2f ± %-8.2f | %10.2f ± %-8.2f | %7.2f ± %-6.2f%%%n";

        System.out.printf(fmtHeader, "Algorithm", "Avg Wait", "Avg Turnaround", "Missed Deadlines", "Miss Rate");
        System.out.println("-".repeat(28) + "-+-" + "-".repeat(18) + "-+-" + "-".repeat(22) + "-+-" + "-".repeat(22) + "-+-" + "-".repeat(16));

        for (Map.Entry<String, RunningStats> e : agg.entrySet()) {
            String algo = e.getKey();
            RunningStats rs = e.getValue();

            // indices: 0=avgWait, 1=avgTurn, 2=missedDeadlines, 3=missRate
            double meanWait = rs.mean(0), sdWait = rs.stddev(0);
            double meanTurn = rs.mean(1), sdTurn = rs.stddev(1);
            double meanMissed = rs.mean(2), sdMissed = rs.stddev(2);
            double meanMissRate = rs.mean(3), sdMissRate = rs.stddev(3);

            System.out.printf(fmtRow, algo, meanWait, sdWait, meanTurn, sdTurn, meanMissed, sdMissed, meanMissRate, sdMissRate);
        }
    }

    private static class Stats {
        final double avgWait;
        final double avgTurn;
        final int missedDeadlines;
        final double missRate;

        Stats(double avgWait, double avgTurn, int missedDeadlines, double missRate) {
            this.avgWait = avgWait;
            this.avgTurn = avgTurn;
            this.missedDeadlines = missedDeadlines;
            this.missRate = missRate;
        }
    }

    /**
     * Tracks running mean and variance for 4 metrics using Welford’s algorithm.
     */
    private static class RunningStats {
        private static final int K = 4;
        private long n = 0;

        private final double[] mean = new double[K];
        private final double[] m2   = new double[K]; // sum of squares of differences from the mean

        void add(double avgWait, double avgTurn, double missedDeadlines, double missRate) {
            double[] x = new double[] { avgWait, avgTurn, missedDeadlines, missRate };
            n++;

            for (int i = 0; i < K; i++) {
                double delta = x[i] - mean[i];
                mean[i] += delta / n;
                double delta2 = x[i] - mean[i];
                m2[i] += delta * delta2;
            }
        }

        double mean(int idx) {
            return mean[idx];
        }

        double variance(int idx) {
            if (n < 2) return 0.0;
            return m2[idx] / (n - 1);
        }

        double stddev(int idx) {
            return Math.sqrt(variance(idx));
        }
    }
}

