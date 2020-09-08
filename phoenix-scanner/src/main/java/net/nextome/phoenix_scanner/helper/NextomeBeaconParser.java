package net.nextome.phoenix_scanner.helper;

import net.nextome.phoenix_scanner.models.beacon.NextomeRssiBean;

public class NextomeBeaconParser {
    public static NextomeRssiBean getBeanFromIBeacon(byte[] scanRecord, int rssi){
        IBeacon b = IBeacon.fromScanData(scanRecord, rssi);

        if (b == null) {
            return null;
        }

        return new NextomeRssiBean(
                b.getProximityUuid().toUpperCase(),
                b.getRssi(),
                b.getMajor(),
                b.getMinor(),
                b.getTxPower(),
                NextomeRssiBean.iBeaconProximity.unknow, b.getBattery());
    }
}
