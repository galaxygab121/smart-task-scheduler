import java.util.List;

import io.TaskCsvLoader;
import model.Task;
import scheduler.EDFScheduler;
import scheduler.PriorityScheduler;
import scheduler.SJFScheduler;
import scheduler.Scheduler;
import simulation.BenchmarkRunner;
import simulation.Simulator;
import simulation.TaskGenerator;

public class Main {
    public static void main(String[] args) {
        try {
            // Modes:
            // 1) CSV mode:
            //    java -cp out Main data/tasks.csv
            //
            // 2) Single benchmark:
            //    java -cp out Main --benchmark 1000
            //
            // 3) Multi-seed benchmark study:
            //    java -cp out Main --benchmark 1000 --seeds 20

            List<Scheduler> schedulers = List.of(
                    new PriorityScheduler(),
                    new EDFScheduler(),
                    new SJFScheduler()
            );

            if (args.length >= 2 && args[0].equalsIgnoreCase("--benchmark")) {
                int nTasks = Integer.parseInt(args[1]);

                int nSeeds = 1; // default: single run
                if (args.length >= 4 && args[2].equalsIgnoreCase("--seeds")) {
                    nSeeds = Integer.parseInt(args[3]);
                }

                // Tune these to change difficulty
                int maxPriority = 10;
                int maxRuntime = 8;
                int maxSlack = 10;
                long baseSeed = 42L;

                if (nSeeds <= 1) {
                    // Single benchmark run
                    List<Task> tasks = TaskGenerator.generate(nTasks, maxPriority, maxRuntime, maxSlack, baseSeed);
                    BenchmarkRunner.runSingle(schedulers, tasks);
                } else {
                    // Multi-seed study
                    BenchmarkRunner.runMultiSeed(
                            schedulers,
                            nTasks,
                            nSeeds,
                            maxPriority,
                            maxRuntime,
                            maxSlack,
                            baseSeed
                    );
                }
                return;
            }

            // CSV mode
            String csvPath = (args.length > 0) ? args[0] : "data/tasks.csv";
            List<Task> tasks = TaskCsvLoader.load(csvPath);

            Simulator.run(new PriorityScheduler(), tasks);
            Simulator.run(new EDFScheduler(), tasks);
            Simulator.run(new SJFScheduler(), tasks);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Usage:");
            System.err.println("  java -cp out Main data/tasks.csv");
            System.err.println("  java -cp out Main --benchmark 1000");
            System.err.println("  java -cp out Main --benchmark 1000 --seeds 20");
            e.printStackTrace();
        }
    }
}




