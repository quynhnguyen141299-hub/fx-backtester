package com.quynh.fxapp.service;

import com.quynh.fxapp.db.FxPriceDao;
import com.quynh.fxapp.model.FxPrice;
import com.quynh.fxapp.strategy.BacktestResult;
import com.quynh.fxapp.strategy.Strategy;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class BacktestEngine {

    private final FxPriceDao dao = new FxPriceDao();

    public BacktestResult run(String ccyPair, Instant from, Instant to, Strategy strategy) throws SQLException {
        List<FxPrice> prices = dao.loadPrices(ccyPair, from, to);
        return strategy.run(prices);
    }
}