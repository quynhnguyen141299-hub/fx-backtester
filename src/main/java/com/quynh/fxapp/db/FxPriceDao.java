package com.quynh.fxapp.db;

import com.quynh.fxapp.model.FxPrice;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FxPriceDao {

    public void insertBatch(List<FxPrice> prices) throws SQLException {
        String sql = "INSERT INTO fx_price (ccy_pair, ts, spot) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (FxPrice p : prices) {
                ps.setString(1, p.getCcyPair());
                ps.setTimestamp(2, Timestamp.from(p.getTs()));
                ps.setDouble(3, p.getSpot());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        }
    }

    public List<FxPrice> loadPrices(String ccyPair, Instant from, Instant to) throws SQLException {
        String sql = """
            SELECT ccy_pair, ts, spot
            FROM fx_price
            WHERE ccy_pair = ?
              AND ts BETWEEN ? AND ?
            ORDER BY ts ASC
        """;
        List<FxPrice> out = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ccyPair);
            ps.setTimestamp(2, Timestamp.from(from));
            ps.setTimestamp(3, Timestamp.from(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new FxPrice(
                        rs.getString("ccy_pair"),
                        rs.getTimestamp("ts").toInstant(),
                        rs.getDouble("spot")
                    ));
                }
            }
        }
        return out;
    }
}