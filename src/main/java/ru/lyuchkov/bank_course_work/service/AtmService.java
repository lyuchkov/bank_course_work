package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.AccountDao;
import ru.lyuchkov.bank_course_work.dao.AtmOperationDao;
import ru.lyuchkov.bank_course_work.model.AtmOperation;

import java.sql.SQLException;

@Service
public class AtmService {

    private final AccountDao accountDao;
    private final AtmOperationDao operationDao;
    public AtmService(AccountDao accountDao, AtmOperationDao operationDao) {
        this.accountDao = accountDao;
        this.operationDao = operationDao;
    }

    public boolean createOperation(AtmOperation operation, Model model) {
        if(!accountDao.isExistWithId(operation.getAccountId())){
            model.addAttribute("error_message",
                    "Счёта с таким ID не существует.");
            return false;
        }
        if(operation.getAmount()<=0 || operation.getAmount()>=1000000){
            model.addAttribute("error_message",
                    "Количество денег при зачислении/снятии введено некорректно.");
            return false;
        }
        if(!operation.isInput()){
           if(accountDao.isAccountAmountLessThan(operation.getAccountId(), operation.getAmount())){
               model.addAttribute("error_message",
                       "Недостаточно средств.");
               return false;
           }
        }
        try {
            return operationDao.createNewOperation(operation);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error_message",
                    "Во время проведения операции произошла ошибка");
            return false;
        }
    }
}
