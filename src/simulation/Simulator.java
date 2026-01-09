package simulation;

import java.util.List;

import metrics.MetricsCalculator;
import metrics.MetricsCalculator.ScheduledTask;
import model.Task;
import scheduler.Scheduler;
import simulation.GanttPrinter;


public class Simulator {

    public static void run(Scheduler scheduler, List<Task> tasks) {
        System.out.println("=== " + scheduler.name() + " ===");

        List<ScheduledTask> schedule = scheduler.schedule(tasks);

        for (ScheduledTask st : schedule) {
            System.out.println(st);
        }
        
        GanttPrinter.print(schedule);


        double avgWait = MetricsCalculator.averageWaitTime(schedule);
        double avgTurn = MetricsCalculator.averageTurnaroundTime(schedule);
        int missed = MetricsCalculator.missedDeadlines(schedule);

        System.out.printf("Average wait time: %.2f%n", avgWait);
        System.out.printf("Average turnaround time: %.2f%n", avgTurn);
        System.out.printf("Missed deadlines: %d%n", missed);

        System.out.println();
    }
}
