package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.service.ConnectionService;

import java.sql.*;

@Component
public class UserDao {
    private Connection connection;
    private final ConnectionService connectionService;

    public UserDao(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }
    public void createUser(String login, String password) throws SQLException {
        String sql = "insert into accounting.usr (username, password) values(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,login);
        stmt.setString(2,password);

        stmt.executeUpdate();
    }
}
