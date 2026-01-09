package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskCsvLoader {

    /**
     * CSV format:
     * id,priority,deadline,runtime
     * A,3,6,3
     */
    public static List<Task> load(String filePath) throws IOException {
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().startsWith("id,")) continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Invalid CSV row (expected 4 columns): " + line);
                }

                String id = parts[0].trim();
                int priority = Integer.parseInt(parts[1].trim());
                int deadline = Integer.parseInt(parts[2].trim());
                int runtime = Integer.parseInt(parts[3].trim());

                tasks.add(new Task(id, priority, deadline, runtime));
            }
        }

        return tasks;
    }
}
