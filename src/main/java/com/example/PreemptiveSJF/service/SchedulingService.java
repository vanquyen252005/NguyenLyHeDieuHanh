package com.example.PreemptiveSJF.service;

import com.example.PreemptiveSJF.object.GanttRecord;
import com.example.PreemptiveSJF.object.ScheduleResult;
import org.springframework.stereotype.Service;
import com.example.PreemptiveSJF.model.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


@Service
public class SchedulingService {
    public ScheduleResult runSjf(List<Process> p) {

        for (int i = 0; i < p.size(); i++) {
            for (int j = i; j < p.size(); j++) {
                if (p.get(j).getArrivalTime() < p.get(i).getArrivalTime()) {
                    Collections.swap(p, i, j);
                } else if (p.get(j).getArrivalTime() == p.get(i).getArrivalTime()) {
                    if (p.get(j).getBurstTime() < p.get(i).getBurstTime()) {
                        Collections.swap(p, i, j);
                    }
                }
            }
        }

        List<GanttRecord> ganttChart = new ArrayList<>();
        int index = 0;
        boolean check = false;


        for (int i = 0; i < p.size(); i++) {
            check = false;
            if (i == 0) {
                p.get(i).setStartTime(p.get(i).getArrivalTime());

                if (p.get(i).getStartTime() > 0) {
                    ganttChart.add(new GanttRecord("Idle", 0, p.get(i).getStartTime()));
                }
            } else {
                for (int j = i; j < p.size(); j++) {
                    if (p.get(j).getArrivalTime() <= p.get(index).getCompletionTime()) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    for (int j = i; j < p.size(); j++) {
                        if (p.get(j).getArrivalTime() <= p.get(index).getCompletionTime()) {
                            if (p.get(j).getBurstTime() < p.get(i).getBurstTime()) {
                                Collections.swap(p, i, j);
                            }
                        }
                    }
                    p.get(i).setStartTime(p.get(index).getCompletionTime());
                } else {

                    ganttChart.add(new GanttRecord("Idle", p.get(index).getCompletionTime(), p.get(i).getArrivalTime()));
                    p.get(i).setStartTime(p.get(i).getArrivalTime());
                }
            }


            p.get(i).setWaitingTime(p.get(i).getStartTime() - p.get(i).getArrivalTime());
            p.get(i).setResponse_time(p.get(i).getStartTime() - p.get(i).getArrivalTime());
            p.get(i).setCompletionTime(p.get(i).getStartTime() + p.get(i).getBurstTime());
            p.get(i).setTurnaroundTime(p.get(i).getCompletionTime() - p.get(i).getArrivalTime());


            ganttChart.add(new GanttRecord("P" + p.get(i).getProcessId(), p.get(i).getStartTime(), p.get(i).getCompletionTime()));

            index = i;
        }


        int cnt = p.size()-1;
        ScheduleResult result = calculateMetrics(p, cnt);
        result.setGanttChart(ganttChart);

        return result;
    }
    public ScheduleResult runSrtf(List<Process> p) {
        boolean[] is_complete = new boolean[p.size()];
        int complete = 0;
        int x = p.size();
        int current_time = 0;
        int index;
        int prev_index = -2;
        int cnt = 0;
        List<GanttRecord> ganttChart = new ArrayList<>();
        int block_start = 0;
        for (Process process : p) {
            process.setRemaining_time(process.getBurstTime());
        }

        while (complete != x) {
            index = -1;
            int mn_max = 1000000;
            for (int i = 0; i < x; i++) {
                if (p.get(i).getArrivalTime() <= current_time && !is_complete[i]) {
                    if (p.get(i).getRemaining_time() < mn_max) {
                        mn_max = p.get(i).getRemaining_time();
                        index = i;
                    } else if (p.get(i).getRemaining_time() == mn_max) {
                        if (p.get(i).getArrivalTime() < p.get(index).getArrivalTime()) {
                            mn_max = p.get(i).getRemaining_time();
                            index = i;
                        }
                    }
                }
            }

            if (current_time > 0 && index != prev_index) {
                if (prev_index == -1) {
                    ganttChart.add(new GanttRecord("Idle", block_start, current_time));
                } else if (prev_index >= 0) {
                    ganttChart.add(new GanttRecord("P" + p.get(prev_index).getProcessId(), block_start, current_time));
                }
                block_start = current_time; // Bắt đầu đếm khúc mới
            }

            if (prev_index != index && prev_index != -2 && prev_index != -1 && index != -1) {
                System.out.println(current_time + '\n');
                cnt++;
            }
            prev_index = index;

            if (index != -1) {
                if (p.get(index).getRemaining_time() == p.get(index).getBurstTime()) {
                    p.get(index).setStartTime(current_time);
                }
                p.get(index).setRemaining_time(p.get(index).getRemaining_time() - 1);
                current_time++;

                if (p.get(index).getRemaining_time() == 0) {
                    p.get(index).setCompletionTime(current_time);
                    p.get(index).setResponse_time(p.get(index).getStartTime() - p.get(index).getArrivalTime());
                    p.get(index).setTurnaroundTime(p.get(index).getCompletionTime() - p.get(index).getArrivalTime());
                    p.get(index).setWaitingTime(p.get(index).getTurnaroundTime() - p.get(index).getBurstTime());
                    is_complete[index] = true;
                    complete++;
                }
            } else {
                current_time++;
            }
        }
        if (prev_index == -1) {
            ganttChart.add(new GanttRecord("Idle", block_start, current_time));
        } else if (prev_index >= 0) {
            ganttChart.add(new GanttRecord("P" + p.get(prev_index).getProcessId(), block_start, current_time));
        }

        ScheduleResult result = calculateMetrics(p, cnt);
        result.setGanttChart(ganttChart); // Set dữ liệu biểu đồ vào kết quả
        return result;
    }


    private ScheduleResult calculateMetrics(List<Process> p, int contextSwitches) {
        int wt = 0, tat = 0, res = 0;
        int complete_max = p.get(0).getCompletionTime();
        int arrival_min = p.get(0).getArrivalTime();

        for (Process process : p) {
            complete_max = max(complete_max, process.getCompletionTime());
            arrival_min = min(arrival_min, process.getArrivalTime());
            wt += process.getWaitingTime();
            tat += process.getTurnaroundTime();
            res += process.getResponse_time();
        }

        ScheduleResult result = new ScheduleResult();
        result.setProcesses(p);
        result.setAvgWt(1.0 * wt / p.size());
        result.setAvgTat(1.0 * tat / p.size());
        result.setAvgRes(1.0 * res / p.size());
        result.setThroughput((double) p.size() / (complete_max - arrival_min));
        result.setContextSwitches(contextSwitches);
        return result;
    }
}
