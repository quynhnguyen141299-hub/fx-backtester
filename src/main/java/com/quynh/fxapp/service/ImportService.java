package com.quynh.fxapp.service;

import com.opencsv.CSVReader;
import com.quynh.fxapp.db.FxPriceDao;
import com.quynh.fxapp.model.FxPrice;

import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ImportService {

    private final FxPriceDao dao = new FxPriceDao();

    // CSV schema: ts,ccy_pair,spot  e.g. 2020-01-01T00:00:00Z,EURUSD,1.1205
    public void importCsv(String path) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            List<FxPrice> batch = new ArrayList<>();
            reader.readNext(); // header
            while ((line = reader.readNext()) != null) {
                Instant ts = Instant.parse(line[0]);
                String ccy = line[1];
                double spot = Double.parseDouble(line[2]);
                batch.add(new FxPrice(ccy, ts, spot));
                if (batch.size() >= 10_000) {
                    dao.insertBatch(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                dao.insertBatch(batch);
            }
        }
    }

    public static Instant parseDateUtc(String isoDate) {
        // date-only helper: 2020-01-01
        return LocalDateTime.parse(isoDate + "T00:00:00").toInstant(ZoneOffset.UTC);
    }
}