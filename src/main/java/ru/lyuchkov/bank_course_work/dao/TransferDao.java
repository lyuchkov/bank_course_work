package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.model.Transfer;
import ru.lyuchkov.bank_course_work.service.ConnectionService;
import ru.lyuchkov.bank_course_work.utils.DaoUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class TransferDao {
    private final ConnectionService connectionService;
    private final Connection connection;

    public TransferDao(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }

    public int getAccountIdByNumber(String number){
        String sql = """
                select accounting.accounts.account_id
                from accounting.accounts
                         inner join (select account_id
                                     from accounting.client
                                     where phone = ?) as sub on sub.account_id = accounts.account_id;""";
        return DaoUtils.getIntValueFromAnyTableByStringAndStatement(number, sql, connection);
    }

    public boolean createNewTransfer(Transfer transfer) throws SQLException {
        String sqlTransfer ="insert into accounting.transfer (amount, account_to_id,account_from_id, op_date) values (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sqlTransfer);
        stmt.setInt(1,transfer.getAmount());
        stmt.setInt(2, transfer.getAccountToId());
        stmt.setInt(3,transfer.getAccountFromId());
        stmt.setTimestamp(4, new Timestamp(transfer.getDate().getTime()));


        String sqlUpdateValueFrom = """
                UPDATE accounting.balance
                SET value = balance.value - ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?); \s""";
        PreparedStatement stmtFrom = connection.prepareStatement(sqlUpdateValueFrom);
        stmtFrom.setInt(1, transfer.getAmount());
        stmtFrom.setInt(2, transfer.getAccountFromId());

        String sqlUpdateValueTo = """
                UPDATE accounting.balance
                SET value = balance.value + ?
                WHERE balance_id = (select balance_id from accounting.accounts where account_id = ?); \s""";
        PreparedStatement stmtTo = connection.prepareStatement(sqlUpdateValueTo);
        stmtTo.setInt(1, transfer.getAmount());
        stmtTo.setInt(2, transfer.getAccountFromId());

        stmt.executeUpdate();
        stmtFrom.executeUpdate();
        stmtTo.executeUpdate();

        return true;
    }
}
