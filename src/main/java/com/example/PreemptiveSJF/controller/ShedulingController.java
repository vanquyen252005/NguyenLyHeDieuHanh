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

    // Trang 1: Hiển thị form nhập liệu
    @GetMapping("/")
    public String showInputPage(Model model) {
        model.addAttribute("processForm", new ProcessForm());
        return "input"; // Gọi file input.html
    }

    // Nút submit ở trang 1 sẽ gọi vào đây
    @PostMapping("/calculate")
    public String calculateSRTF(@ModelAttribute ProcessForm processForm, Model model) {
        // Gọi service xử lý thuật toán
        ScheduleResult result = schedulingService.runSrtf(processForm.getProcesses());

        // Gắn kết quả vào Model để truyền sang file result.html
        model.addAttribute("result", result);

        // Chuyển hướng sang trang kết quả
        return "result"; // Gọi file result.html
    }
}
