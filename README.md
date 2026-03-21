# FX Backtester

A lightweight Java CLI application for backtesting FX spot trading strategies on historical price data.

## Overview

This tool ingests FX spot prices from CSV into a local embedded H2 database, supports time-windowed querying, and runs configurable moving average crossover backtests with PnL and drawdown reporting.

## Tech Stack

- Java 21
- Maven
- H2 (embedded database, JDBC)
- OpenCSV

## Project Structure
fx-backtester/
├── pom.xml
└── src/main/java/com/quynh/fxapp/
├── App.java
├── db/
│ ├── DatabaseManager.java
│ └── FxPriceDao.java
├── model/
│ └── FxPrice.java
├── strategy/
│ ├── Strategy.java
│ ├── MaCrossoverStrategy.java
│ └── BacktestResult.java
└── service/
├── BacktestEngine.java
└── ImportService.java


## Getting Started

### Prerequisites

- JDK 21
- Maven

### Build

```bash
mvn clean package
```

### Import CSV data

```bash
java -jar target/fx-backtester-1.0-SNAPSHOT-jar-with-dependencies.jar import ./eurusd.csv
```

CSV format:
ts,ccy_pair,spot
2020-01-01T00:00:00Z,EURUSD,1.1205
2020-01-01T00:01:00Z,EURUSD,1.1207

text

### Run backtest

```bash
java -jar target/fx-backtester-1.0-SNAPSHOT-jar-with-dependencies.jar backtest EURUSD 2020-01-01T00:00:00Z 2020-01-01T02:00:00Z 5 20
```

Arguments: `cyPair> <from> <to> <fastMA> <slowMA>`

### Sample output
Strategy=MA(5,20), trades=2, PnL=0.0160, maxDD=0.0

text

## Architecture

- **db/** – JDBC database manager and DAO for batch inserts and time-windowed queries
- **model/** – FxPrice domain object
- **strategy/** – Strategy interface and MA crossover implementation with PnL/drawdown tracking
- **service/** – Import service (CSV ingest) and backtest engine

## Roadmap

- [ ] Additional strategies (mean reversion, Bollinger Bands, RSI)
- [ ] JavaFX GUI
- [ ] Multi-pair and cross-pair support
- [ ] Sharpe ratio, Sortino ratio, and additional risk metrics
- [ ] Live data feed integration
- [ ] Swap to Postgres/MySQL for production use

## License

MIT
Save as README.md in C:\Users\urname\fx-backtester\, then push:

text
git add README.md
git commit -m "Add README"
git push
----------
Note: sample eurusd.csv was generated using this Python snippet:

import csv
from datetime import datetime, timedelta, timezone

start = datetime(2020, 1, 1, 0, 0, 0, tzinfo=timezone.utc)
rows = []
spot = 1.1205

for i in range(100):
    ts = start + timedelta(minutes=i)
    rows.append([ts.isoformat().replace("+00:00", "Z"), "EURUSD", f"{spot:.4f}"])
    spot += 0.0002  # small drift

with open("eurusd.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["ts", "ccy_pair", "spot"])
    writer.writerows(rows)

print("Created eurusd.csv")
