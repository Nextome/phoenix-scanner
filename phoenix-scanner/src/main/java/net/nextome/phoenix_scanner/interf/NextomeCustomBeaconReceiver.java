package net.nextome.phoenix_scanner.interf;

import net.nextome.phoenix_scanner.models.beacon.NextomeRssiBean;

public interface NextomeCustomBeaconReceiver {
    void onBeaconLeScan(NextomeRssiBean beacon);
    void onScanRangingFinished();
}
