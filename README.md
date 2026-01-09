# Smart Task Scheduler (Java)

A Java-based task scheduling simulator comparing **Priority**, **Earliest Deadline First (EDF)**, and **Shortest Job First (SJF)** using:
- ✅ CSV task input
- ✅ Gantt-style terminal visualization with deadline indicators
- ✅ Benchmarking + multi-seed performance study (mean ± stddev)

## Features
- Implements scheduling strategies: **Priority**, **EDF**, **SJF**
- Prints per-task metrics (start, finish, wait, turnaround)
- Gantt timeline output:
  - `[X ]` = finishes on-time
  - `[X!]` = finishes after deadline
- Benchmark framework:
  - Avg wait time
  - Avg turnaround time
  - Deadline misses + miss rate (%)
  - Multi-seed study with mean ± stddev (Welford’s algorithm)

## Project Structure
src/
model/ Task model
scheduler/ Priority, EDF, SJF implementations
metrics/ Metric calculations
io/ CSV loader
simulation/ Simulator, Gantt printer, benchmark runner, task generator
data/
tasks.csv Example input

## Run

### Compile
```bash
javac -d out $(find src -name "*.java")
Run with CSV input
java -cp out Main data/tasks.csv
Run benchmark study (multi-seed)
java -cp out Main --benchmark 1000 --seeds 20
Example Output (Gantt)
Timeline: [F ][B ][B ][E ][A!][A!][A!][D!][D!][C!][C!][C!][C!]
Time:     0   1       3   4           7       9               13
Legend:
  A -> (p=3, d=6, r=3)  MISSED
  ...
Results Summary
A multi-seed benchmark study (20 trials × 1000 tasks) shows that SJF consistently minimizes average wait/turnaround time and reduces missed deadlines under the tested parameters, highlighting classic scheduling tradeoffs between throughput and deadline guarantees.

Save.

---

# 2) Commit + push README improvements

Run:

```bash
git add README.md
git commit -m "docs: polish README with usage and benchmark details"
git push
