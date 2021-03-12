package net.nextome.phoenix_scanner;

import android.app.Notification;
import android.content.Context;

import net.nextome.phoenix_scanner.interf.NextomeBeaconReceiver;
import net.nextome.phoenix_scanner.interf.NextomeCustomBeaconReceiver;
import net.nextome.phoenix_scanner.service.PhoenixScannerServiceManager;
import net.nextome.phoenix_scanner.models.beacon.NextomeBeaconListRaw;
import net.nextome.phoenix_scanner.models.beacon.NextomeRssiBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PhoenixScanner {
    private Context context;

    private long foregroundScanPeriod = 1000L;
    private long foregroundBetweenScanPeriod = 250L;
    private long backgroundScanPeriod = 1000L;
    private long backgroundBetweenScanPeriod = 250L;
    private int beaconListMaxSize = 12;
    private int rssiThreshold = -75;

    private MutableLiveData<HashMap<String, NextomeBeaconListRaw>> liveBeaconsMap = new MutableLiveData<>();

    private NextomeBeaconReceiver mainBeaconReceiver;
    private NextomeCustomBeaconReceiver customBeaconReceiver = null;
    private List<String> beaconLayoutList = new ArrayList<>();

    private PhoenixScannerServiceManager manager;
    private long timeToFlushMillis = 8000;

    private static long currentTs = 0;
    private static final long flushTs = 10000;

    public PhoenixScanner(Context context) {
        liveBeaconsMap.setValue(new HashMap<String, NextomeBeaconListRaw>());

        currentTs = System.currentTimeMillis();

        // Temporary buffer between scan period
        final HashMap<String, NextomeBeaconListRaw> tempBuffer = liveBeaconsMap.getValue();

        this.context = context;
        this.mainBeaconReceiver = new NextomeBeaconReceiver() {

            @Override
            public void onBeaconLeScan(NextomeRssiBean beacon) {
                if (beacon != null) {
                    if (beacon.getRSSI() > rssiThreshold) {
                        // Between scans, build a buffer
                        String key = getKeyFromBeacon(beacon);

                        if (tempBuffer.containsKey(key)) {
                            tempBuffer.get(key).addBeacon(beacon);
                        } else {
                            NextomeBeaconListRaw list = new NextomeBeaconListRaw(beaconListMaxSize);
                            list.addBeacon(beacon);
                            tempBuffer.put(key, list);
                        }
                    }
                }
            }

            @Override
            public void onScanRangingFinished() {
                currentTs = System.currentTimeMillis();

                // Filtered beacon after expiry time
                HashMap<String, NextomeBeaconListRaw> scannedBeacons = new HashMap<>();


                // only save beacons after certain timestamp
                for (Map.Entry<String, NextomeBeaconListRaw> listRawEntry : tempBuffer.entrySet()) {
                    if (listRawEntry.getValue().getUpdatedAt() > currentTs - flushTs) {
                        scannedBeacons.put(listRawEntry.getKey(),listRawEntry.getValue());
                    }
                }


                liveBeaconsMap.setValue(scannedBeacons);
            }
        };
    }

    public void startScan() {
        manager = new PhoenixScannerServiceManager(context);
        manager.startService(
                foregroundScanPeriod,
                foregroundBetweenScanPeriod,
                mainBeaconReceiver,
                customBeaconReceiver,
                beaconLayoutList);
    }

    public void startForegroundScan(int serviceCode, Notification notification) {
        manager = new PhoenixScannerServiceManager(context);

        manager.startForegroundService(notification, serviceCode,
                foregroundScanPeriod,
                foregroundBetweenScanPeriod,
                backgroundScanPeriod,
                backgroundBetweenScanPeriod,
                mainBeaconReceiver,
                customBeaconReceiver,
                beaconLayoutList);
    }

    public LiveData<HashMap<String, NextomeBeaconListRaw>> getBeaconsLiveData(){
        return liveBeaconsMap;
    }

    public void stop() {
        manager.stopService();
    }

    public LifecycleOwner getServiceManager(){
        return manager;
    }

    public void addBeaconLayout(String beaconLayout) {
        beaconLayoutList.add(beaconLayout);
    }

    public List<String> getBeaconLayoutList() {
        return beaconLayoutList;
    }

    public void setBeaconLayoutList(List<String> beaconLayoutList) {
        this.beaconLayoutList = beaconLayoutList;
    }

    public void setMainBeaconReceiver(NextomeBeaconReceiver mainBeaconReceiver) {
        this.mainBeaconReceiver = mainBeaconReceiver;
    }

    public NextomeCustomBeaconReceiver getCustomBeaconReceiver() {
        return customBeaconReceiver;
    }

    public void setCustomBeaconReceiver(NextomeCustomBeaconReceiver customBeaconReceiver) {
        this.customBeaconReceiver = customBeaconReceiver;
    }

    public long getBackgroundScanPeriod() {
        return backgroundScanPeriod;
    }

    public void setBackgroundScanPeriod(long backgroundScanPeriod) {
        this.backgroundScanPeriod = backgroundScanPeriod;
    }

    public long getBackgroundBetweenScanPeriod() {
        return backgroundBetweenScanPeriod;
    }

    public void setBackgroundBetweenScanPeriod(long backgroundBetweenScanPeriod) {
        this.backgroundBetweenScanPeriod = backgroundBetweenScanPeriod;
    }

    public NextomeBeaconReceiver getMainBeaconReceiver(){
        return mainBeaconReceiver;
    }

    private String getKeyFromBeacon(NextomeRssiBean b){
        return b.getUUID() +b.getMajor()+b.getMinor();
    }

    public long getForegroundScanPeriod() {
        return foregroundScanPeriod;
    }

    public void setForegroundScanPeriod(long foregroundScanPeriod) {
        this.foregroundScanPeriod = foregroundScanPeriod;
    }

    public long getForegroundBetweenScanPeriod() {
        return foregroundBetweenScanPeriod;
    }

    public void setForegroundBetweenScanPeriod(long foregroundBetweenScanPeriod) {
        this.foregroundBetweenScanPeriod = foregroundBetweenScanPeriod;
    }

    public int getBeaconListMaxSize() {
        return beaconListMaxSize;
    }

    public void setBeaconListMaxSize(int beaconListMaxSize) {
        this.beaconListMaxSize = beaconListMaxSize;
    }

    public Context getContext() {
        return context;
    }

    public int getRssiThreshold() {
        return rssiThreshold;
    }

    public void setRssiThreshold(int rssiThreshold) {
        this.rssiThreshold = rssiThreshold;
    }
}