eurusd csv built using this Python func

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
