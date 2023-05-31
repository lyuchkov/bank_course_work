package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.Branch;
import ru.lyuchkov.bank_course_work.service.ConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BranchDao {
    private  Connection connection;
    private final ConnectionService connectionService;

    public BranchDao(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }


    public void createBranch(String address, String phone) throws SQLException {
        Statement stmt = stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(
                "insert into accounting.bank_branch(address, " +
                        "phone) " +
                        "VALUES ('" + address + "','" + phone+ "');");
    }
    public List<Branch> all() throws SQLException {
        Statement stmt = connection.createStatement();
        ArrayList<Branch> list = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM accounting.bank_branch;");

        while (resultSet.next()) {
            int id = resultSet.getInt("branch_id");
            String address = resultSet.getString("address");
            String phone = resultSet.getString("phone");

            Branch branch = new Branch(id, address, phone);
            list.add(branch);
        }

        return list;
    }
}
