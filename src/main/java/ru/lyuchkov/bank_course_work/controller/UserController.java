package ru.lyuchkov.bank_course_work.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.lyuchkov.bank_course_work.model.Transfer;
import ru.lyuchkov.bank_course_work.service.DepositService;
import ru.lyuchkov.bank_course_work.service.LoanService;
import ru.lyuchkov.bank_course_work.service.TransferService;
import ru.lyuchkov.bank_course_work.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class UserController {
    private final UserService userService;
    private final LoanService loanService;
    private final DepositService depositService;
    private final TransferService transferService;

    public UserController(UserService userService, LoanService loanService, DepositService depositService, TransferService transferService) {
        this.userService = userService;
        this.loanService = loanService;
        this.depositService = depositService;

        this.transferService = transferService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "./forms/login.html";
    }

    @GetMapping("/acc")
    public String getAccountForm(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            int amount = userService.getAmountFromAccountWithId(id);
            model.addAttribute("amount", String.valueOf(amount));
        } else {
            String username = principal.toString();
        }
        return "./forms/personal_form.html";
    }

    @GetMapping("/acc/credit")
    public String getLoanForm(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            Date date = loanService.getDeadlineById(id);
            if(loanService.isExistLoanWithAccountId(id)){
                model.addAttribute("loan_exist", "true");
                model.addAttribute("deadline_message", "Крайняя дата выплаты:   " + new SimpleDateFormat("dd-MM-yyyy").format(loanService.getDeadlineById(id)));
            }else {
                model.addAttribute("error", "Нет доступных кредитов");
            }
        } else {

            String username = principal.toString();
        }
        return "./forms/acc/loan_form.html";
    }
    @GetMapping("/acc/deposit")
    public String getDepositForm(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            Date date = depositService.getDeadlineById(id);
            if(depositService.isExistDepositWithAccountId(id)){
                model.addAttribute("deposit_exist", "true");
                model.addAttribute("deadline_message", "Крайняя дата:   " + new SimpleDateFormat("dd-MM-yyyy").format(depositService.getDeadlineById(id)));
            }else {
                model.addAttribute("error", "Нет доступных вкладов");
            }
        } else {

            String username = principal.toString();
        }
        return "./forms/acc/acc_deposit.html";
    }
    @GetMapping("/acc/transfer")
    public String getTransferForm(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            model.addAttribute( "transfer", new Transfer());
        } else {

            String username = principal.toString();
        }
        return "./forms/acc/transfer_form.html";
    }
    @PostMapping("/redeemdeposit")
    public String redeemDeposit(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            if(depositService.isExistDepositWithAccountId(id)){
                depositService.redeem(id, model);
            }
            return "./forms/acc/acc_deposit.html";
        }else return "./forms/acc/acc_deposit.html";
    }
    @PostMapping("/redeemloan")
    public String redeemLoan(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            if(loanService.isExistLoanWithAccountId(id)){
               loanService.redeemLoan(id, model);
            }
            return "./forms/acc/loan_form.html";
        }else return "./forms/acc/loan_form.html";
    }
    @PostMapping("/createtransfer")
    public String createNewTransfer(Model model, @ModelAttribute Transfer transfer,  RedirectAttributes redirectAttributes){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            int id = userService.getIdByEmail(email);
            transfer.setAccountFromId(id);
        }else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return "redirect:/acc/transfer";
        }
        if (transferService.createTransferByNumber(transfer, model)) return "redirect:/acc";
        else {
            redirectAttributes.addAttribute("error_message", model.getAttribute("error_message"));
            return  "redirect:/acc/transfer";
        }
    }
}
