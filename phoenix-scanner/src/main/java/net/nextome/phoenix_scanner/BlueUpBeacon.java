package net.nextome.phoenix_scanner;

import android.os.ParcelUuid;

public class BlueUpBeacon {
    static ParcelUuid BLUEUP_BATTERY_SERVICE_UUID = ParcelUuid.fromString("0000180f-0000-1000-8000-00805f9b34fb");

    public static int getBattery(ScanRecord sr) {
        byte[] serviceData = sr.getServiceData(BLUEUP_BATTERY_SERVICE_UUID);

        if(serviceData == null){ return -1; }
        if(serviceData.length < 1){ return -1; }

        int battery = serviceData[0];

        final String name = sr.getDeviceName();
        if (name == null) { return -1; }
        if (!name.toUpperCase().startsWith("BLUEUP")) { return -1; }

        return battery;
    }

}
