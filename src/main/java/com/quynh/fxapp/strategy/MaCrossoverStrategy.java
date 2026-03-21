package com.quynh.fxapp.strategy;

import com.quynh.fxapp.model.FxPrice;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MaCrossoverStrategy implements Strategy {

    private final int fastWindow;
    private final int slowWindow;

    public MaCrossoverStrategy(int fastWindow, int slowWindow) {
        if (fastWindow >= slowWindow) {
            throw new IllegalArgumentException("fastWindow < slowWindow required");
        }
        this.fastWindow = fastWindow;
        this.slowWindow = slowWindow;
    }

    @Override
    public String getName() {
        return "MA(" + fastWindow + "," + slowWindow + ")";
    }

    @Override
    public BacktestResult run(List<FxPrice> prices) {
        if (prices.size() < slowWindow + 1) {
            return new BacktestResult(getName(), 0, 0.0, 0.0);
        }

        Deque<Double> fastQ = new ArrayDeque<>();
        Deque<Double> slowQ = new ArrayDeque<>();
        double fastSum = 0.0;
        double slowSum = 0.0;

        boolean longPos = false;
        double entryPrice = 0.0;
        double equity = 0.0;
        double peakEquity = 0.0;
        double maxDD = 0.0;
        long trades = 0;

        for (int i = 0; i < prices.size(); i++) {
            double px = prices.get(i).getSpot();

            // update queues
            fastQ.addLast(px);
            fastSum += px;
            if (fastQ.size() > fastWindow) {
                fastSum -= fastQ.removeFirst();
            }

            slowQ.addLast(px);
            slowSum += px;
            if (slowQ.size() > slowWindow) {
                slowSum -= slowQ.removeFirst();
            }

            if (slowQ.size() < slowWindow) continue;

            double fastMa = fastSum / fastQ.size();
            double slowMa = slowSum / slowQ.size();

            boolean fastAbove = fastMa > slowMa;

            if (!longPos && fastAbove) {
                longPos = true;
                entryPrice = px;
                trades++;
            } else if (longPos && !fastAbove) {
                double pnlTrade = px - entryPrice;
                equity += pnlTrade;
                longPos = false;

                if (equity > peakEquity) peakEquity = equity;
                double dd = peakEquity - equity;
                if (dd > maxDD) maxDD = dd;
            }
        }

        // Close at last price if still long
        if (longPos) {
            double pxLast = prices.getLast().getSpot();
            double pnlTrade = pxLast - entryPrice;
            equity += pnlTrade;
            trades++;
        }

        return new BacktestResult(getName(), trades, equity, maxDD);
    }
}