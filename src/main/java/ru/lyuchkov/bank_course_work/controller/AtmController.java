package ru.lyuchkov.bank_course_work.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.AtmOperation;
import ru.lyuchkov.bank_course_work.model.Branch;
import ru.lyuchkov.bank_course_work.service.AtmService;

@Controller
@RequestMapping("/atm")
@PreAuthorize("hasRole('WORKER')")
public class AtmController {
    private final AtmService service;

    public AtmController(AtmService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String getForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("atmOperation", new AtmOperation());
        return "./forms/atm_form.html";
    }

    @PostMapping("/new")
    public String createNewAtmOperation(@ModelAttribute AtmOperation operation, Model model, RedirectAttributes redirectAttributes) {
        if (service.createOperation(operation, model)) return "redirect:/";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/atm/new";
        }
    }
}
