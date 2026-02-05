package com.example.utils;

import com.example.config.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
    public static List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DbConfig.getConnectionString());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= cols; i++) {
                        row.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(row);
                }
            }
        }
        return list;
    }
}
