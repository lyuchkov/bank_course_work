package ru.lyuchkov.bank_course_work.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.AtmOperation;
import ru.lyuchkov.bank_course_work.model.Loan;
import ru.lyuchkov.bank_course_work.service.LoanService;

import java.sql.SQLException;

@Controller
@RequestMapping("/loan")
@PreAuthorize("hasRole('WORKER')")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String getLoanForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("loan", new Loan());
        return "./forms/loan_form.html";
    }

    @PostMapping("/new")
    public String createNewLoanOperation(@ModelAttribute Loan loan, Model model, RedirectAttributes redirectAttributes) {
        if (service.createLoan(loan, model)) return "redirect:/";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/loan/new";
        }
    }
    @GetMapping("/expired")
    public String getExpiredLoans(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("clientList", service.getAllClientsWithExpiredLoan(model));
        return "clients.html";
    }
}
