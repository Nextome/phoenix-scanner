package net.nextome.phoenix_scanner.models.helper;

import net.nextome.phoenix_scanner.models.beacon.NextomeBeaconDetection;
import net.nextome.phoenix_scanner.models.beacon.NextomeRssiBean;

public class BeaconConverter {
    public static NextomeBeaconDetection beaconToBeaconDetection(NextomeRssiBean beacon) {
        NextomeBeaconDetection beaconDetection = new NextomeBeaconDetection();
        beaconDetection.setId(0);
        beaconDetection.setMajor(beacon.getMajor());
        beaconDetection.setMinor(beacon.getMinor());
        beaconDetection.setUuid(beacon.getUUID());
        beaconDetection.setValue((int) beacon.getRSSI());

        return beaconDetection;
    }
}
