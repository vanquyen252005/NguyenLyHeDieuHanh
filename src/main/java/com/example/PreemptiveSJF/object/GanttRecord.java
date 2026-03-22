package com.example.PreemptiveSJF.object;

public class GanttRecord {
    private String processName;
    private int startTime;
    private int endTime;

    public GanttRecord(String processName, int startTime, int endTime) {
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProcessName() { return processName; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }


    public int getDuration() { return endTime - startTime; }
}
