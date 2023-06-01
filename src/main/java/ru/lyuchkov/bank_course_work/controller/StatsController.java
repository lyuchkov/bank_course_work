package ru.lyuchkov.bank_course_work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.lyuchkov.bank_course_work.service.StatsService;

import java.util.Map;

@Controller
@RequestMapping("/stats")
public class StatsController {
    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @GetMapping("")
    public String getStats(Model model){
        Map<String, Integer> allExpiredLoans = service.getAllExpiredLoans();
        Map<String, Integer> allRedeemedLoans = service.getAllRedeemedLoans();
        Map<String, Integer> allRedeemedDeposit = service.getAllRedeemedDeposit();
        model.addAttribute("loanMap", allExpiredLoans);
        model.addAttribute("loanMapRed", allRedeemedLoans);
        model.addAttribute("depositMap", allRedeemedDeposit);
        return "stats.html";
    }
}
