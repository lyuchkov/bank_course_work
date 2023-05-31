package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.AccountDao;
import ru.lyuchkov.bank_course_work.dao.LoanDao;
import ru.lyuchkov.bank_course_work.model.Client;
import ru.lyuchkov.bank_course_work.model.Loan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {
    private final LoanDao loanDao;
    private final AccountDao accountDao;

    public LoanService(LoanDao loanDao, AccountDao accountDao) {
        this.loanDao = loanDao;
        this.accountDao = accountDao;
    }

    public boolean createLoan(Loan loan, Model model) {
        if(!accountDao.isExistWithId(loan.getAccountId())){
            model.addAttribute("error_message",
                    "Счёта с таким ID не существует.");
            return false;
        }
        if(loanDao.isExistWithId(loan.getAccountId())){
            model.addAttribute("error_message",
                    "Нельзя выдать более одного займа.");
            return false;
        }
        if(loan.getAmount()<=0 || loan.getAmount()>=1000000){
            model.addAttribute("error_message",
                    "Количество денег введено некорректно.");
            return false;
        }
        if(loan.getPercent()<=0 || loan.getPercent()>=30){
            model.addAttribute("error_message",
                    "Процент введен некорректно.");
            return false;
        }
        if(loan.getDeadline().before(new Date())){
            model.addAttribute("error_message",
                    "Дата выплаты введена некорректно.");
            return false;
        }
        try {
            return loanDao.createNewLoan(loan);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error_message",
                    "Во время проведения операции произошла ошибка");
            return false;
        }
    }

    public boolean redeemLoan(int id, Model model) {
        if (!accountDao.isExistWithId(id)) {
            model.addAttribute("error_message",
                    "Счёта с таким ID не существует.");
            return false;
        }
        if (!loanDao.isExistWithId(id)) {
            model.addAttribute("error_message",
                    "Нет активного кредита. ");
            return false;
        }
        if (accountDao.isAccountAmountLessThan(id,
                loanDao.getLoanAmountByAccountId(id) *(
                        (loanDao.getPercentByAccountId(id)/100)+1))) {
            model.addAttribute("error_message",
                    "Недостаточно средств. ");
            return false;
        }
        try {
            return loanDao.redeem(id);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error_message",
                    "Во время проведения операции произошла ошибка");
            return false;
        }
    }

   public List<Client> getAllClientsWithExpiredLoan(Model model){
       try {
           return loanDao.getAllClientsWithExpiredLoan();
       } catch (SQLException e) {
           e.printStackTrace();
           model.addAttribute("error_message",
                   "Во время проведения операции произошла ошибка");
           return new ArrayList<>();
       }
   }
    //todo check if is expire or get all expired

}
