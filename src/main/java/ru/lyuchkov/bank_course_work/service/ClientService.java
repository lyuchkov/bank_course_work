package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.lyuchkov.bank_course_work.dao.*;
import ru.lyuchkov.bank_course_work.model.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClientService {
    final ClientDao clientDAO;
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final LoanDao loanDao;
    private final DepositDao depositDao;


    public ClientService(ClientDao clientDAO, AccountDao accountDao, UserDao userDao, LoanDao loanDao, DepositDao depositDao) {
        this.clientDAO = clientDAO;
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.loanDao = loanDao;
        this.depositDao = depositDao;
    }

    public List<Client> getAllClients() throws SQLException {
        return clientDAO.all();
    }

    public boolean createClient(Client client, Model model){
        String name = client.getName();
        String surname = client.getSurname();
        String email = client.getEmail();
        String phone = "7"+ client.getPhone();
        if(accountDao.isExistWithPhone(phone)){
            model.addAttribute("error_message", "Номер уже занят.");
            return false;
        }
        if(accountDao.isExistWithEmail(email)){
            model.addAttribute("error_message", "Email уже занят.");
            return false;
        }
        if(!isNameOrSurnameValid(name)||!isNameOrSurnameValid(surname)){
            model.addAttribute("error_message", "Поля имени и фамилии не могут быть пустыми и больше 30 знаков.");
            return false;
        }
        if(!isEmailValid(email)){
            model.addAttribute("error_message", "Некорректный формат почты.");
            return false;
        }
        if(client.getPassword().length()>30){
            model.addAttribute("error_message", "Слишком длинный пароль.");
            return false;
        }
        if(!isPhoneValid(phone)){
            model.addAttribute("error_message", "Некорректный формат номера.");
            return false;
        }

        try {
            userDao.createUser(email, client.getPassword());
            clientDAO.createClient(name, surname, email, phone, client.getBranchId());
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute( "error_message",e.getMessage());
            return false;
        }
        return true;
    }

    private boolean isPhoneValid(String phone) {
        String PHONE_TEMPLATE = "7\\d{10}";
        return phone.matches(PHONE_TEMPLATE);
    }

    private boolean isEmailValid(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email).matches();
    }

    private boolean isNameOrSurnameValid(String name){
        return name.length()!=0 && name.length()<=30;
    }

    public Client getClientWithId(int id, Model model) {
        try {
            model.addAttribute( "client_exist","1");
            return clientDAO.getClientWithId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error_message", "Пользователь с данным id не существует");
            model.addAttribute("client_exist", "0" );
            return null;
        }
    }

    public boolean updateClient(Client client, Model model, int id) {
        String name = client.getName();
        String surname = client.getSurname();
        String phone = ""+ client.getPhone();
        if(!isNameOrSurnameValid(name)||!isNameOrSurnameValid(surname)){
            model.addAttribute("error_message", "Поля имени и фамилии не могут быть пустыми и больше 30 знаков.");
            return false;
        }
        if(!isPhoneValid(phone)){
            model.addAttribute("error_message", "Некорректный формат номера.");
            return false;
        }
        try {
            clientDAO.update(client, id);
            return true;
        } catch (SQLException e) {
            model.addAttribute( "error_message",e.getMessage());
            return false;
        }
    }

    public boolean deleteClient(Client client, Model model, int id) {
        if(depositDao.isExistDepositWithId(id)){
            model.addAttribute("error_message", "У клиента есть не закрытый вклад.");
            return false;
        }
        if(loanDao.isExistWithId(id)){
            model.addAttribute("error_message", "У клиента есть не закрытый кредит.");
            return false;
        }
        if(!accountDao.isAccountAmountLessThan(id,1)){
            model.addAttribute("error_message", "У клиента не сняты средства со счета.");
            return false;
        }
        try {
            clientDAO.remove(client, id);
            return true;
        } catch (SQLException e) {
            model.addAttribute( "error_message",e.getMessage());
            return false;
        }
    }

    public int getAccountIdByClientId(int id) {
        return clientDAO.getAccountId(id);
    }
}
