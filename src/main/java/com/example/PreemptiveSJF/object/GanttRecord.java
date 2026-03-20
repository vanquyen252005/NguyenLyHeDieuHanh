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

    // Thuộc tính này để CSS tính tỷ lệ độ rộng của khối trên màn hình
    public int getDuration() { return endTime - startTime; }
}
