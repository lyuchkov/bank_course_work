package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.Client;
import ru.lyuchkov.bank_course_work.model.Loan;
import ru.lyuchkov.bank_course_work.service.ConnectionService;
import ru.lyuchkov.bank_course_work.utils.DaoUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanDao {
    private final ConnectionService connectionService;
    private final ClientDao clientDao;

    private Connection connection;



    public LoanDao(ConnectionService connectionService, ClientDao clientDao) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
        this.clientDao = clientDao;
    }

    public boolean createNewLoan(Loan loan) throws SQLException {
        String sql = "insert into accounting.loan(amount, account_id, percent, deadline) values (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, loan.getAmount());
        stmt.setInt(2, loan.getAccountId());
        stmt.setInt(3, loan.getPercent());
        stmt.setDate(4, new Date(loan.getDeadline().getTime()));
        String updateSql = """
                UPDATE accounting.balance
                SET value = balance.value + ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);
                """;
        PreparedStatement updStmt = connection.prepareStatement(updateSql);
        updStmt.setInt(1, loan.getAmount());
        updStmt.setInt(2, loan.getAccountId());

        updStmt.executeUpdate();

        stmt.executeUpdate();
        return true;
    }

    public boolean isExistWithId(int accountId) {
        String sql = "select exists(select * from accounting.loan where account_id = ? and is_redeemed=false)";
        return DaoUtils.isRowExistFromAnyTableByIdAndStatement(accountId, sql, connection);
    }

    public int getLoanAmountByAccountId(int accountId) {
        String sql = "select amount from accounting.loan where account_id = ?";
        return DaoUtils.getIntValueFromAnyTableByIdAndStatement(accountId, sql, connection);
    }

    public int getPercentByAccountId(int accountId) {
        String sql = "select percent from accounting.loan where account_id = ?";
        return DaoUtils.getIntValueFromAnyTableByIdAndStatement(accountId, sql, connection);
    }

    public boolean redeem(int id) throws SQLException {
        String updateSql = """
                UPDATE accounting.balance
                SET value = balance.value - ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);""";
        PreparedStatement updStmt = connection.prepareStatement(updateSql);
        updStmt.setInt(1, getLoanAmountByAccountId(id) * ((getPercentByAccountId(id) / 100) + 1));
        updStmt.setInt(2, id);

        updStmt.executeUpdate();

        String updateSql1 = """
                UPDATE accounting.loan
                                    SET is_redeemed = true
                                    WHERE account_id = ?;
                    """;
        PreparedStatement updStmt1 = connection.prepareStatement(updateSql);
        updStmt.setInt(1, id);
        updStmt1.executeUpdate();
        return true;
    }

    public List<Client> getAllClientsWithExpiredLoan() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                """
            select *
            from accounting.client
            inner join (select account_id
            from accounting.loan
            where is_expired = true
            group by account_id) as sub using (account_id)
             """
        );
        ArrayList<Client> list = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        return ClientDao.getClients(list, resultSet);
    }


}

