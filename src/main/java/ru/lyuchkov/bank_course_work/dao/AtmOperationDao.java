package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.AtmOperation;
import ru.lyuchkov.bank_course_work.service.ConnectionService;

import java.sql.*;

@Component
public class AtmOperationDao {
    private Connection connection;
    private final ConnectionService connectionService;


    public AtmOperationDao(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }

    public boolean createNewOperation(AtmOperation operation) throws SQLException {
        String sql ="insert into accounting.atm_operations(amount, is_operation_input, account_id) values (?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1,operation.getAmount());
        stmt.setBoolean(2, operation.isInput());
        stmt.setInt(3,operation.getAccountId());


        String updateSql;
        if(operation.isInput()){
            updateSql = """
                    UPDATE accounting.balance
                    SET value = balance.value + ?
                    WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);""";
        }else {
            updateSql = """
                    UPDATE accounting.balance
                    SET value = balance.value - ?
                    WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);""";
        }
            PreparedStatement updStmt = connection.prepareStatement(updateSql);
            updStmt.setInt(1,operation.getAmount());
            updStmt.setInt(2,operation.getAccountId());

            updStmt.executeUpdate();

            stmt.executeUpdate();
            return true;
    }
}
