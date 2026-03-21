package com.quynh.fxapp.strategy;

public class BacktestResult {
    private final String strategyName;
    private final long trades;
    private final double pnl;
    private final double maxDrawdown;

    public BacktestResult(String strategyName, long trades, double pnl, double maxDrawdown) {
        this.strategyName = strategyName;
        this.trades = trades;
        this.pnl = pnl;
        this.maxDrawdown = maxDrawdown;
    }

    public String getStrategyName() { return strategyName; }
    public long getTrades() { return trades; }
    public double getPnl() { return pnl; }
    public double getMaxDrawdown() { return maxDrawdown; }

    @Override
    public String toString() {
        return "Strategy=" + strategyName +
               ", trades=" + trades +
               ", PnL=" + pnl +
               ", maxDD=" + maxDrawdown;
    }
}