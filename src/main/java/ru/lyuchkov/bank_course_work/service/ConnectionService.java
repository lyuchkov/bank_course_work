package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class ConnectionService {
    private final Connection connection;

    public ConnectionService() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank", "postgres", "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
