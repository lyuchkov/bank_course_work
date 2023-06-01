package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import ru.lyuchkov.bank_course_work.dao.AccountDao;
import ru.lyuchkov.bank_course_work.dao.UserDao;

import java.sql.SQLException;

@Service
public class UserService {
    private final UserDao dao;
    private final AccountDao accountDao;

    public UserService(UserDao dao, AccountDao accountDao) {
        this.dao = dao;
        this.accountDao = accountDao;
    }

    public int getIdByEmail(String email){
        try {
            return dao.getAccountIdByEmail(email);
        } catch (SQLException e) {
            return -1;
        }
    }
    public int getAmountFromAccountWithId(int id){
        if(accountDao.isExistWithId(id)) {
            return accountDao.getAmountForAccountWithId(id);
        }
        return -1;
    }
}
