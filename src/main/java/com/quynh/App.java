package com.quynh.fxapp;

import com.quynh.fxapp.service.BacktestEngine;
import com.quynh.fxapp.service.ImportService;
import com.quynh.fxapp.strategy.BacktestResult;
import com.quynh.fxapp.strategy.MaCrossoverStrategy;

import java.time.Instant;

public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("""
                Usage:
                  import <csvPath>
                  backtest <ccyPair> <fromIso> <toIso> <fast> <slow>
                """);
            return;
        }

        switch (args[0]) {
            case "import" -> {
                String path = args[1];
                new ImportService().importCsv(path);
                System.out.println("Imported " + path);
            }
            case "backtest" -> {
                String ccy = args[1];
                Instant from = Instant.parse(args[2]);   // e.g. 2020-01-01T00:00:00Z
                Instant to   = Instant.parse(args[3]);
                int fast = Integer.parseInt(args[4]);
                int slow = Integer.parseInt(args[5]);

                MaCrossoverStrategy strat = new MaCrossoverStrategy(fast, slow);
                BacktestEngine engine = new BacktestEngine();
                BacktestResult res = engine.run(ccy, from, to, strat);
                System.out.println(res);
            }
            default -> System.out.println("Unknown command: " + args[0]);
        }
    }
}