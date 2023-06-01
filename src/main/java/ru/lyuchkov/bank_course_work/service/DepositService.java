package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.AccountDao;
import ru.lyuchkov.bank_course_work.dao.DepositDao;
import ru.lyuchkov.bank_course_work.model.Deposit;

import java.sql.SQLException;
import java.util.Date;

@Service
public class DepositService {
    private final AccountDao accountDao;
    private final DepositDao depositDao;

    public DepositService(AccountDao accountDao, DepositDao depositDao) {
        this.accountDao = accountDao;
        this.depositDao = depositDao;
    }

    public boolean createDeposit(Deposit deposit, Model model) {
        if (!accountDao.isExistWithId(deposit.getAccountId())) {
            model.addAttribute("error_message",
                    "Счёта с таким ID не существует.");
            return false;
        }
        if (depositDao.isExistDepositWithId(deposit.getAccountId())) {
            model.addAttribute("error_message",
                    "Нельзя оформить более одного депозита.");
            return false;
        }
        if (deposit.getAmount() <= 0 || deposit.getAmount() >= 1000000) {
            if (accountDao.isAccountAmountLessThan(deposit.getAccountId(), deposit.getAmount())) {
                model.addAttribute("error_message",
                        "Количество денег введено некорректно.");
            }
            return false;
        }
        if (deposit.getPercent() <= 0 || deposit.getPercent() >= 30) {
            model.addAttribute("error_message",
                    "Процент для вклада введен некорректно.");
            return false;
        }
        if (deposit.getDeadline().before(new Date())) {
            model.addAttribute("error_message",
                    "Дата возврата введена некорректно.");
            return false;
        }
        try {
            return depositDao.createNewDeposit(deposit);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error_message",
                    "Во время проведения операции произошла ошибка");
            return false;
        }
    }

    public boolean redeem(int id, Model model){
        if (!accountDao.isExistWithId(id)) {
            model.addAttribute("error_message",
                    "Счёта с таким ID не существует.");
            return false;
        }
        if (!depositDao.isExistDepositWithId(id)) {
            model.addAttribute("error_message",
                    "Нет активного депозита. ");
            return false;
        }
        if(depositDao.getExpirationDate(id).before(new Date())){
            try {
                depositDao.redeem(id);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("error_message",
                        "Во время проведения операции произошла ошибка");
                return false;
            }
        }else {
            try {
            depositDao.getBack(id);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                model.addAttribute("error_message",
                        "Во время проведения операции произошла ошибка");
                return false;
            }
        }

    }

    public boolean isExistDepositWithAccountId(int id) {
        return depositDao.isExistDepositWithId(id);
    }

    public Date getDeadlineById(int id) {
        return depositDao.getExpirationDate(id);
    }
}
