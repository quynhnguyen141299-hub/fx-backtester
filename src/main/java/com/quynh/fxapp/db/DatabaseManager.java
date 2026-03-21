package com.quynh.fxapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:h2:./fxdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS fx_price (
                    id IDENTITY PRIMARY KEY,
                    ccy_pair VARCHAR(10) NOT NULL,
                    ts TIMESTAMP NOT NULL,
                    spot DOUBLE PRECISION NOT NULL
                );
                CREATE INDEX IF NOT EXISTS idx_fx_price_pair_ts
                ON fx_price (ccy_pair, ts);
            """);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing DB", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}