package com.quynh.fxapp.model;

import java.time.Instant;

public class FxPrice {
    private final String ccyPair;
    private final Instant ts;
    private final double spot;

    public FxPrice(String ccyPair, Instant ts, double spot) {
        this.ccyPair = ccyPair;
        this.ts = ts;
        this.spot = spot;
    }

    public String getCcyPair() { return ccyPair; }
    public Instant getTs() { return ts; }
    public double getSpot() { return spot; }
}