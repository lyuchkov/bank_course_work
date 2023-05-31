package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.Client;
import ru.lyuchkov.bank_course_work.service.ConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClientDao {

    private final Connection connection;
    private final AccountDao accountDao;
    private final ConnectionService connectionService;


    public ClientDao(AccountDao accountDao, ConnectionService connectionService) {
        this.accountDao = accountDao;
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }

    public List<Client> all() throws SQLException {
        Statement stmt = connection.createStatement();
        ArrayList<Client> list = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM accounting.client;");

        return getClients(list, resultSet);
    }

    public Client getClientWithId(int id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM accounting.client where client_id = " + id + ";");
        if(!resultSet.next())return null;
        String name = resultSet.getString("client_name");
        String surname = resultSet.getString("client_surname");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        boolean active = resultSet.getBoolean("active");
        int accountId = resultSet.getInt("account_id");
        return new Client(id, name, surname, email, phone, accountId,active);
    }


    public void createClient(String name, String surname, String email, String phone,int branch) throws SQLException {
        Statement stmt = stmt = connection.createStatement();
        int accountId = accountDao.createNewAccount(branch, name+" "+ phone);
        int res = stmt.executeUpdate(
                "insert into accounting.client(client_name, " +
                        "client_surname," +
                        " email, " +
                        " account_id, " +
                        "phone) " +
                        "VALUES ('" + name + "','" + surname + "','" + email + "','" + accountId + "','" + phone + "');");
    }

    public void update(Client client, int id) throws SQLException {
        Statement stmt  = connection.createStatement();
        String sql = "UPDATE accounting.client " +
                "SET client_name = '"+ client.getName()+"',"+
                "client_surname = '" + client.getSurname()+"'," +
                "phone= '" +client.getPhone()+ "' "+
                "WHERE client_id = " + id;

        stmt.executeUpdate(sql);
    }
    static List<Client> getClients(ArrayList<Client> list, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("client_id");
            String name = resultSet.getString("client_name");
            String surname = resultSet.getString("client_surname");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            int accountId = resultSet.getInt("account_id");
            list.add(new Client(id, name, surname, email, phone, accountId));
        }

        return list;
    }
}
