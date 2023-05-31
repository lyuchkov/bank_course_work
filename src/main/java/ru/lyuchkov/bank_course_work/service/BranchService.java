package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.BranchDao;
import ru.lyuchkov.bank_course_work.model.Branch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {
     final BranchDao dao;

    public BranchService(BranchDao dao) {
        this.dao = dao;
    }

    public boolean createBranch(Branch branch, Model model) {
        String address = branch.getAddress();
        String phone = "7"+ branch.getPhone();
        if(address.length()>70 || address.isEmpty()){
            model.addAttribute( "error_message","Некоректный адрес");
            return false;
        }
        if(!isPhoneValid(phone)){
            model.addAttribute( "error_message","Некоректный номер");
            return false;
        }
        try {
            dao.createBranch(address, phone);
        } catch (SQLException e) {
            model.addAttribute( "error_message",e.getMessage());
            return false;
        }
        return true;
    }
    private boolean isPhoneValid(String phone) {
        String PHONE_TEMPLATE = "7\\d{10}";
        return phone.matches(PHONE_TEMPLATE);
    }


    public List<Branch> getAll() {
        try {
            return dao.all();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
