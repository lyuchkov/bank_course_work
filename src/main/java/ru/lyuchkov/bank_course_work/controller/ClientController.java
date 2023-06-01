package ru.lyuchkov.bank_course_work.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.Branch;
import ru.lyuchkov.bank_course_work.model.Client;
import ru.lyuchkov.bank_course_work.service.BranchService;
import ru.lyuchkov.bank_course_work.service.ClientService;

import java.sql.SQLException;

@Controller
@RequestMapping("/client")
public class ClientController {
    final ClientService service;
    final BranchService branchService;


    public ClientController(ClientService service, BranchService branchService) {
        this.service = service;
        this.branchService = branchService;
    }



    @GetMapping("/all")
    @PreAuthorize("hasRole('WORKER')")
    public String getAll(Model model, @RequestParam(required = false) String error_message) {
        try {
            model.addAttribute("error_message", error_message);
            model.addAttribute("clientList", service.getAllClients());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "clients.html";
    }

    @GetMapping("/new")
    public String getNewForm(Model model, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("client", new Client());
        model.addAttribute("branchList", branchService.getAll());
        return "./forms/client_form.html";
    }
    @PostMapping("/new")
    public String createNewClient(@ModelAttribute Client client, Model model, RedirectAttributes redirectAttributes) {
        if (service.createClient(client, model)) return "redirect:/client/all";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/client/new";
        }
    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('WORKER')")
    public String editClientWithId(Model model, @PathVariable int id, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("id", id);
        model.addAttribute("client", service.getClientWithId(id, model));
        return "./forms/client_update.html";
    }


    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasRole('WORKER')")
    public String alterUserWithId(@ModelAttribute Client client, Model model, RedirectAttributes redirectAttributes, @PathVariable int id){
        if (service.updateClient(client, model, id)) return "redirect:/client/all";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/client/edit/"+id;
        }
    }
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('WORKER')")
    public String getDeleteForm(Model model, @PathVariable int id, @RequestParam(required = false) String error_message){
        model.addAttribute("error_message", error_message);
        model.addAttribute("id", id);
        model.addAttribute("client", service.getClientWithId(id, model));
        return "./forms/client_delete.html";
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('WORKER')")
    public String deleteClientWithId(@ModelAttribute Client client, Model model, RedirectAttributes redirectAttributes, @PathVariable int id){
        if (service.deleteClient(client, model, service.getAccountIdByClientId(id))) return "redirect:/client/all";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/client/all";
        }
    }
}
