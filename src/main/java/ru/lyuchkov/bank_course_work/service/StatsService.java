package ru.lyuchkov.bank_course_work.service;

import org.springframework.stereotype.Service;
import ru.lyuchkov.bank_course_work.model.Client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatsService {
    private final ConnectionService connectionService;
    private Connection connection;

    public StatsService(ConnectionService connectionService) {
        this.connectionService = connectionService;
        connection = connectionService.getConnection();
    }

    public Map<String, Integer> getAllExpiredLoans() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            HashMap<String, Integer> map = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery("select address, count(sub3)from accounting.bank_branch\n" +
                    "    inner join (select branch_id\n" +
                    "from accounting.balance\n" +
                    "         inner join (select*\n" +
                    "                     from accounting.accounts\n" +
                    "                              inner join (select * from accounting.loan where is_expired = true) as sub1\n" +
                    "                                         on sub1.account_id = accounts.account_id) as sub2 on sub2.balance_id = accounting.balance.balance_id) as sub3 using (branch_id)\n" +
                    "group by address;");

            return getMap(map, resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, Integer> getAllRedeemedLoans() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            HashMap<String, Integer> map = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery("select address, count(sub3)from accounting.bank_branch\n" +
                    "    inner join (select branch_id\n" +
                    "from accounting.balance\n" +
                    "         inner join (select*\n" +
                    "                     from accounting.accounts\n" +
                    "                              inner join (select * from accounting.loan where is_redeemed = true) as sub1\n" +
                    "                                         on sub1.account_id = accounts.account_id) as sub2 on sub2.balance_id = accounting.balance.balance_id) as sub3 using (branch_id)\n" +
                    "group by address;");

            return getMap(map, resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, Integer> getAllRedeemedDeposit() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            HashMap<String, Integer> map = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery("select address, count(sub3)from accounting.bank_branch\n" +
                    "    inner join (select branch_id\n" +
                    "from accounting.balance\n" +
                    "         inner join (select*\n" +
                    "                     from accounting.accounts\n" +
                    "                              inner join (select * from accounting.deposit where is_redeemed = true) as sub1\n" +
                    "                                         on sub1.account_id = accounts.account_id) as sub2 on sub2.balance_id = accounting.balance.balance_id) as sub3 using (branch_id)\n" +
                    "group by address;");

            return getMap(map, resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Map<String, Integer> getMap(HashMap<String, Integer> map, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String address = resultSet.getString(1);
            int count = resultSet.getInt(2);
            map.put(address, map.getOrDefault(address, 0)+count);
        }
        return map;
    }
}
