package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.Deposit;
import ru.lyuchkov.bank_course_work.service.ConnectionService;
import ru.lyuchkov.bank_course_work.utils.DaoUtils;

import java.sql.*;

@Component
public class DepositDao {
    private final Connection connection;
    private final ConnectionService connectionService;

    public DepositDao(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }


    public boolean isExistDepositWithId(int accountId) {
        String sql = "select exists(select * from accounting.deposit where account_id = ? and is_redeemed=false)";
        return DaoUtils.isRowExistFromAnyTableByIdAndStatement(accountId, sql, connection);
    }

    public boolean createNewDeposit(Deposit deposit) throws SQLException {
        String sql = "insert into accounting.deposit(amount, account_id, percent, until_date) values (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, deposit.getAmount());
        stmt.setInt(2, deposit.getAccountId());
        stmt.setInt(3, deposit.getPercent());
        stmt.setDate(4, new Date(deposit.getDeadline().getTime()));
        String updateSql = """
                UPDATE accounting.balance
                SET value = balance.value - ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);
                """;
        PreparedStatement updStmt = connection.prepareStatement(updateSql);
        updStmt.setInt(1, deposit.getAmount());
        updStmt.setInt(2, deposit.getAccountId());

        updStmt.executeUpdate();

        stmt.executeUpdate();
        return true;
    }

    public boolean redeem(int id) throws SQLException {
        String updateSql = """
                UPDATE accounting.balance
                SET value = balance.value + ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);""";
        PreparedStatement updStmt = connection.prepareStatement(updateSql);
        int p = getPercentByAccountId(id);
        double m = ((double) p / 100) + 1;
        double i = (int) getDepositAmountByAccountId(id) * m;
        updStmt.setInt(1, (int)i);
        updStmt.setInt(2, id);

        updStmt.executeUpdate();

        String updateSql1 = """
                UPDATE accounting.deposit
                                    SET is_redeemed = true
                                    WHERE account_id = ?;
                    """;
        PreparedStatement updStmt1 = connection.prepareStatement(updateSql1);
        updStmt1.setInt(1, id);
        updStmt1.executeUpdate();
        return true;
    }

    private int getDepositAmountByAccountId(int id) {
        String sql = "select amount from accounting.deposit where account_id = ?";
        return DaoUtils.getIntValueFromAnyTableByIdAndStatement(id, sql, connection);
    }

    private int getPercentByAccountId(int id) {
        String sql = "select percent from accounting.deposit where account_id = ?";
        return DaoUtils.getIntValueFromAnyTableByIdAndStatement(id, sql, connection);
    }

    public java.util.Date getExpirationDate(int id) {
        String sql = "select until_date from accounting.deposit where account_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())return null;
            java.util.Date date = new java.util.Date(rs.getDate(1).getTime());
            return date;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getBack(int id) throws SQLException {
        String updateSql = """
                UPDATE accounting.balance
                SET value = balance.value + ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?);""";
        PreparedStatement updStmt = connection.prepareStatement(updateSql);
        updStmt.setInt(1, getDepositAmountByAccountId(id));
        updStmt.setInt(2, id);

        updStmt.executeUpdate();

        String updateSql1 = """
                UPDATE accounting.deposit
                                    SET is_redeemed = true
                                    WHERE account_id = ?;
                    """;
        PreparedStatement updStmt1 = connection.prepareStatement(updateSql1);
        updStmt1.setInt(1, id);
        updStmt1.executeUpdate();
        return true;
    }
}
