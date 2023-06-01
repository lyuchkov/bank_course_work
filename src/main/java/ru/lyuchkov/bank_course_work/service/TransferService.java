package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.AccountDao;
import ru.lyuchkov.bank_course_work.dao.TransferDao;
import ru.lyuchkov.bank_course_work.model.Transfer;

import java.sql.Date;
import java.sql.SQLException;

@Service
public class TransferService {
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferService(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    public boolean createTransferByNumber(Transfer transfer, Model model){
        String phone = "7"+ transfer.getToNumber();
        transfer.setToNumber(phone);
        int amount = transfer.getAmount();
        if(!accountDao.isExistWithPhone(phone)){
            model.addAttribute("error_message", "Такого номера не существует");
            return false;
        }
        if(amount<=0 || amount>=1000000){
            model.addAttribute("error_message",
                    "Количество денег при зачислении/снятии введено некорректно.");
            return false;
        }

        if(accountDao.isAccountAmountLessThan(transfer.getAccountFromId(), amount)){
            model.addAttribute("error_message",
                    "Недостаточно средств.");
            return false;
        }

        try {
            transfer.setAccountToId(transferDao.getAccountIdByNumber(transfer.toNumber));
            transfer.setDate(new java.util.Date());
            transferDao.createNewTransfer(transfer);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
