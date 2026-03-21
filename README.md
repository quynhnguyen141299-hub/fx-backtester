# FX Backtester

A lightweight Java CLI application for backtesting FX spot trading strategies on historical price data.

# Strategy used for backtesting for illustration: 
When short-term trend (last 5 period-prices) > long-term trend (last 20-period prices), the price is likely rising — so buy. When it falls back below — sell.

# Details
The strategy backtested was a Moving Average (MA) Crossover:
How it works
We defined 2 moving averages: a fast MA (short window) and a slow MA (long window)
When the fast MA crosses above the slow MA → buy (go long)
When the fast MA crosses below the slow MA → exit (close position)

# Our specific run
text
```
backtest EURUSD 2020-01-01T00:00:00Z 2020-01-01T02:00:00Z 5 20
```
Fast MA = 5-period

Slow MA = 20-period

Pair = EURUSD

Window = 2 hours of 1-minute data

Result = 2 trades, PnL of 0.0160 (≈160 pips), no drawdown
## Overview

This tool ingests FX spot prices from CSV into a local embedded H2 database, supports time-windowed querying, and runs configurable MA crossover backtests with PnL and drawdown reporting.

## Tech Stack

- Java 21
- Maven
- H2 (embedded database, JDBC) : we use this bc H2 is lightweight, and supports local data stored on user's machine, (e.g. fxdb.mv.db in your project folder), not on a remote MySQL/Postgres server. So when your app runs DatabaseManager.getConnection(), it just opens that local file directly. No network, no setup, no passwords. When your app stops, the file stays — data persists between runs.
- OpenCSV

## Project Structure
 ```
fx-backtester/
│
├── pom.xml
│
└── src/main/java/com/quynh/fxapp/
│
├── App.java # CLI entry point
│
├── db/
│ ├── DatabaseManager.java # JDBC connection & schema init
│ └── FxPriceDao.java # Batch insert & time-windowed queries
│
├── model/
│ └── FxPrice.java # FX spot price domain object
│
├── strategy/
│ ├── Strategy.java # Strategy interface
│ ├── MaCrossoverStrategy.java # MA crossover implementation
│ └── BacktestResult.java # PnL, trades & drawdown container
│
└── service/
├── ImportService.java # CSV ingest into H2
└── BacktestEngine.java # Loads data & runs strategy
 ```
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
```
ts,ccy_pair,spot
2020-01-01T00:00:00Z,EURUSD,1.1205
2020-01-01T00:01:00Z,EURUSD,1.1207
```
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

## How to push to git

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
