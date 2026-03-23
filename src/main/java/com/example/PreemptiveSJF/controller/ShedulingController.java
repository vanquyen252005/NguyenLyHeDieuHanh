package com.example.PreemptiveSJF.controller;

import com.example.PreemptiveSJF.object.ProcessForm;
import com.example.PreemptiveSJF.object.ScheduleResult;
import com.example.PreemptiveSJF.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.PreemptiveSJF.model.Process;

import java.util.List;
@Controller
public class ShedulingController {
    @Autowired
    private SchedulingService schedulingService;


    @GetMapping("/")
    public String showInputPage(Model model) {
        model.addAttribute("processForm", new ProcessForm());
        return "input";
    }

    @PostMapping("/calculate")
    public String calculateAlgorithm(
            @RequestParam("algorithmType") String algorithmType,
            @ModelAttribute ProcessForm processForm,
            Model model) {

        ScheduleResult result = null;


        if ("SRTF".equals(algorithmType)) {
            result = schedulingService.runSrtf(processForm.getProcesses());
        } else if ("SJF".equals(algorithmType)) {
            result = schedulingService.runSjf(processForm.getProcesses());
        }

        model.addAttribute("result", result);
        model.addAttribute("algoName", algorithmType);

        return "result";
    }
}
