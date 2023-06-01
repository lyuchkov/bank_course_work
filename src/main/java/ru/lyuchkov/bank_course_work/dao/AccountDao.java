package ru.lyuchkov.bank_course_work.dao;

import org.springframework.stereotype.Component;
import ru.lyuchkov.bank_course_work.utils.DaoUtils;

import java.sql.*;

@Component
public class AccountDao {
    private final Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank", "postgres", "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int createNewAccount(int branchId, String title) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM create_account_for_new_user(0," +branchId+",'"+title+"')";
        ResultSet set = stmt.executeQuery(sql);
        if(!set.next())return -1;
        return set.getInt("create_account_for_new_user");
    }

    public boolean isExistWithId(int accountId){
        String sql = "select exists(select * from accounting.accounts where account_id = ?)";
        return DaoUtils.isRowExistFromAnyTableByIdAndStatement(accountId, sql, connection);

    }

    public boolean isAccountAmountLessThan(int accountId, int amount) {
        String sql = """
                select value
                from accounting.balance
                where balance_id = (select balance_id from accounting.accounts where account_id = ?)""";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,accountId);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) return false;
            int a = rs.getInt("value");
            System.out.println(a);
            return amount>a;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isExistWithPhone(String phone) {
        String sql = "select exists(select * from accounting.client where phone = ?)";
        return DaoUtils.isRowExistFromAnyTableByIdAndStatement(phone, sql, connection);
    }

    public boolean isExistWithEmail(String email) {
        String sql = "select exists(select * from accounting.client where email = ?)";
        return DaoUtils.isRowExistFromAnyTableByIdAndStatement(email, sql, connection);

    }

    public int getAmountForAccountWithId(int id){
        String sql = "select value from accounting.balance where balance_id = (select balance_id from accounting.accounts where account_id = ?)";
        return DaoUtils.getIntValueFromAnyTableByIdAndStatement(id, sql, connection);
    }
}
