package ru.lyuchkov.bank_course_work.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DaoUtils {
    public static boolean isRowExistFromAnyTableByIdAndStatement(int accountId,
                                                                 String sql,
                                                                 Connection connection){
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isRowExistFromAnyTableByIdAndStatement(String val,
                                                                 String sql,
                                                                 Connection connection){
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, val);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getIntValueFromAnyTableByIdAndStatement(int accountId,
                                                              String sql,
                                                              Connection connection){
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static int getIntValueFromAnyTableByStringAndStatement(String val,
                                                              String sql,
                                                              Connection connection){
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, val);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
