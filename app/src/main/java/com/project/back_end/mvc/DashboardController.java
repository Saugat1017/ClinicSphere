package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final Service sharedService;


    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        boolean valid = sharedService.validateToken(token, "admin");
        if (valid) {
            return "admin/adminDashboard";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        boolean valid = sharedService.validateToken(token, "doctor");
        if (valid) {
            return "doctor/doctorDashboard";
        } else {
            return "redirect:/";
        }
    }
}
