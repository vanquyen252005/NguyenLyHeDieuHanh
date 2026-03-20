package com.example.PreemptiveSJF.object;
import com.example.PreemptiveSJF.model.Process;
import java.util.List;

public class ScheduleResult {
    private List<GanttRecord> ganttChart;
    private List<Process> processes;
    private double avgWt;
    private double avgTat;
    private double avgRes;
    private double throughput;
    private int contextSwitches;
    public List<Process> getProcesses() {
        return processes;
    }

    public double getAvgWt() {
        return avgWt;
    }

    public double getAvgTat() {
        return avgTat;
    }

    public double getAvgRes() {
        return avgRes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public void setAvgWt(double avgWt) {
        this.avgWt = avgWt;
    }

    public void setAvgTat(double avgTat) {
        this.avgTat = avgTat;
    }

    public void setAvgRes(double avgRes) {
        this.avgRes = avgRes;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public void setContextSwitches(int contextSwitches) {
        this.contextSwitches = contextSwitches;
    }

    public double getThroughput() {
        return throughput;
    }

    public int getContextSwitches() {
        return contextSwitches;
    }
    public List<GanttRecord> getGanttChart() { return ganttChart; }
    public void setGanttChart(List<GanttRecord> ganttChart) { this.ganttChart = ganttChart; }
}
