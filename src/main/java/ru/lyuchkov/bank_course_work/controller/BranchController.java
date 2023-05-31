package ru.lyuchkov.bank_course_work.controller;

import org.apache.catalina.webresources.AbstractResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.Branch;
import ru.lyuchkov.bank_course_work.model.Client;
import ru.lyuchkov.bank_course_work.service.BranchService;

import java.sql.SQLException;

@Controller
@RequestMapping("/branch")
@PreAuthorize("hasRole('WORKER')")
public class BranchController {
    private final BranchService service;

    public BranchController(BranchService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("clientList", service.getAll());
        return "branches.html";
    }

    @GetMapping("/new")
    public String getNewForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("branch", new Branch());
        return "./forms/branch_form.html";
    }
    @PostMapping("/new")
    public String createNewClient(@ModelAttribute Branch branch, Model model, RedirectAttributes redirectAttributes) {
        if (service.createBranch(branch, model)) return "redirect:/branch/all";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/branch/new";
        }
    }
}
