package ru.lyuchkov.bank_course_work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.Deposit;
import ru.lyuchkov.bank_course_work.model.Loan;
import ru.lyuchkov.bank_course_work.service.DepositService;

@Controller
@RequestMapping("/deposit")
public class DepositController {
    private final DepositService service;

    public DepositController(DepositService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String getDepositForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("deposit", new Deposit());
        return "./forms/deposit_form.html";
    }

    @PostMapping("/new")
    public String createNewDepositOperation(@ModelAttribute Deposit deposit, Model model, RedirectAttributes redirectAttributes) {
        if (service.createDeposit(deposit, model)) return "redirect:/";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/deposit/new";
        }
    }
}
