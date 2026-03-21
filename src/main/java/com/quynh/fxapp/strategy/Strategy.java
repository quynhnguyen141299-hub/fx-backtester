package com.quynh.fxapp.strategy;

import com.quynh.fxapp.model.FxPrice;

import java.util.List;

public interface Strategy {
    BacktestResult run(List<FxPrice> prices);
    String getName();
}