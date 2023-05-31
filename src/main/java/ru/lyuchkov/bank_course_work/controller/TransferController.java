package ru.lyuchkov.bank_course_work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transfer")
public class TransferController {
    @GetMapping
    public String getTransferForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);

        //todo form for transfer
        //todo transfer dao
        //todo transfer service with finding client by number
        return "./forms/transfer_form.html";
    }

   /* @PostMapping
    public String createNewTransfer(@ModelAttribute Transfer transfer, Model model, RedirectAttributes redirectAttributes) {
        if (service.createDeposit(deposit, model)) return "redirect:/";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/deposit/new";
        }
    }*/

}
