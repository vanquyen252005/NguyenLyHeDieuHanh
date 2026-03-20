package com.example.PreemptiveSJF.object;
import com.example.PreemptiveSJF.model.Process;
import java.util.ArrayList;
import java.util.List;

public class ProcessForm {
    private List<Process> processes = new ArrayList<>();

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
