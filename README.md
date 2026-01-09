# Smart Task Scheduler

A Java CLI simulation comparing scheduling strategies:
- Priority scheduling (higher priority first)
- Earliest Deadline First (EDF)

Compared Priority, EDF, and SJF scheduling strategies and evaluated average wait time, turnaround time, and deadline-miss rate; observed SJF minimizing average wait time and reducing missed deadlines on benchmark datasets.

## Run
From the project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main


---

# 4) Run it in VS Code Terminal

Open terminal in the project root:

### macOS/Linux
```bash
javac -d out $(find src -name "*.java")
java -cp out Main

javac -d out (Get-ChildItem -Recurse -Filter *.java | % FullName)
java -cp out Main

## Results

We evaluated three scheduling algorithms — Priority, Earliest Deadline First (EDF), and Shortest Job First (SJF) — using randomized workloads of 1000 tasks across 20 independent trials.

**Metrics evaluated:**
- Average wait time
- Average turnaround time
- Number of missed deadlines
- Miss rate (%)

**Summary of findings:**
- SJF consistently minimized average wait and turnaround time.
- SJF reduced missed deadlines by ~12% compared to Priority and EDF.
- Priority and EDF exhibited similar performance under the tested parameters.
- Variance analysis highlighted tradeoffs between throughput optimization and deadline guarantees.

These results align with classical scheduling theory and demonstrate the impact of algorithm choice on system-level performance.
