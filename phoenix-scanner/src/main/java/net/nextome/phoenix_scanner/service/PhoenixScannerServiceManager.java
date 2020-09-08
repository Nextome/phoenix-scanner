package net.nextome.phoenix_scanner.service;


import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import net.nextome.phoenix_scanner.helper.NextomeBeaconParser;
import net.nextome.phoenix_scanner.interf.NextomeBeaconReceiver;
import net.nextome.phoenix_scanner.interf.NextomeCustomBeaconReceiver;
import net.nextome.phoenix_scanner.models.beacon.NextomeRssiBean;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.scanner.NonBeaconLeScanCallback;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import static net.nextome.phoenix_scanner.helper.NextomeBeaconLayouts.BEACON_LAYOUT_IBEACON;

public class PhoenixScannerServiceManager implements BeaconConsumer, LifecycleOwner {

    private static final Region RANGING_REGION =
            new Region("RangingRegion", null, null, null);

    BeaconManager beaconManager;
    Context context;
    NextomeBeaconReceiver mainReceiver;
    NextomeCustomBeaconReceiver customReceiver;
    LifecycleRegistry lifecycleRegistry;

    public PhoenixScannerServiceManager(Context context) {

        this.context = context;
        this.beaconManager = BeaconManager.getInstanceForApplication(context);

        this.lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    public void startForegroundService(Notification foregroundNotification,
                                       int code,
                                       long scanPeriod,
                                       long betweenScanPeriod,
                                       long backgroundScanPeriod,
                                       long backgroundBetweenScanPeriod,
                                       NextomeBeaconReceiver receiver,
                                       NextomeCustomBeaconReceiver customBeaconReceiver,
                                       List<String> beaconLayoutList){

        beaconManager.enableForegroundServiceScanning(foregroundNotification, code);
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setBackgroundScanPeriod(backgroundScanPeriod);
        beaconManager.setBackgroundBetweenScanPeriod(backgroundBetweenScanPeriod);

        beaconManager.setBackgroundMode(true);

        startService(scanPeriod, betweenScanPeriod, receiver, customBeaconReceiver, beaconLayoutList);
    }

    public void startService(long scanPeriod,
                             long betweenScanPeriod,
                             NextomeBeaconReceiver receiver,
                             NextomeCustomBeaconReceiver customBeaconReceiver,
                             List<String> beaconLayoutList){

        mainReceiver = receiver;
        customReceiver = customBeaconReceiver;

        addBeaconLayouts(beaconLayoutList);

        beaconManager.setForegroundScanPeriod(scanPeriod);
        beaconManager.setNonBeaconLeScanCallback(new NonBeaconLeScanCallback() {
            @Override
            public void onNonBeaconLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                NextomeRssiBean bean = NextomeBeaconParser.getBeanFromIBeacon(scanRecord, rssi);

                if (bean != null) {
                    mainReceiver.onBeaconLeScan(bean);
                }


                if (customReceiver != null) {
                    customReceiver.onBeaconLeScan(bean);
                }
            }
        });

        beaconManager.setForegroundBetweenScanPeriod(betweenScanPeriod);
        beaconManager.bind(this);
    }

    private void addBeaconLayouts(List<String> beaconLayoutList) {
        // Always add iBeacon format for Nextome Beacons
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(BEACON_LAYOUT_IBEACON));

        for (String beaconLayout : beaconLayoutList) {
            beaconManager.getBeaconParsers().add(new BeaconParser()
                    .setBeaconLayout(beaconLayout));
        }
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                mainReceiver.onScanRangingFinished();

                if (customReceiver != null) {
                    customReceiver.onScanRangingFinished();
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(RANGING_REGION);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void stopService(){
        try {
            beaconManager.unbind(this);
            beaconManager.stopRangingBeaconsInRegion(RANGING_REGION);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
        return context.bindService(intent, serviceConnection, i);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
