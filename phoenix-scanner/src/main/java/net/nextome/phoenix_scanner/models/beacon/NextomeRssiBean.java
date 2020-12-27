package net.nextome.phoenix_scanner.models.beacon;

import java.util.Date;

public class NextomeRssiBean implements Comparable<NextomeRssiBean>{
    public String UUID;
    public double RSSI;
    public int minor;
    public int major;
    public int map;
    public int TxPower;
    public byte[] scanResult;

    @Override
    public int compareTo(NextomeRssiBean nextomeRssiBean) {
        int last = Double.compare(this.getRSSI(), nextomeRssiBean.getRSSI());

        return last == 0 ? Double.compare(this.getRSSI(), nextomeRssiBean.getRSSI()) : last;
    }

    public enum iBeaconProximity {immediate, near,middle, far, unknow};
    public iBeaconProximity Proximity;
    public double nodeX;
    public double nodeY;
    private int timesFound = 0;
    public double calculatedDistance = 0;
    private int battery = -1;
    private double variance = 1.0;
    private Date time = new Date();

    public NextomeRssiBean(){
    }

    public NextomeRssiBean(String UUID, double rssi, int major, int minor, int txPower,
                           iBeaconProximity proximity, int battery,
                           byte[] scanResult) {
        this.RSSI = rssi;
        this.UUID  = UUID;
        this.minor = minor;
        this.major = major;
        this.TxPower = txPower;
        this.Proximity = proximity;
        this.battery = battery;
        this.scanResult = scanResult;
    }

    public String getKey(){
        return UUID + major+minor;
    }


    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getRSSI() {
        return RSSI;
    }

    public void setRSSI(double RSSI) {
        this.RSSI = RSSI;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getTxPower() {
        return TxPower;
    }

    public void setTxPower(int txPower) {
        TxPower = txPower;
    }

    public iBeaconProximity getProximity() {
        return Proximity;
    }

    public void setProximity(iBeaconProximity proximity) {
        Proximity = proximity;
    }

    public double getNodeX() {
        return nodeX;
    }

    public void setNodeX(double nodeX) {
        this.nodeX = nodeX;
    }

    public double getNodeY() {
        return nodeY;
    }

    public void setNodeY(double nodeY) {
        this.nodeY = nodeY;
    }

    public int getTimesFound() {
        return timesFound;
    }

    public void setTimesFound(int timesFound) {
        this.timesFound = timesFound;
    }

    public double getCalculatedDistance() {
        return calculatedDistance;
    }

    public void setCalculatedDistance(double calculatedDistance) {
        this.calculatedDistance = calculatedDistance;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte[] getScanResult() {
        return scanResult;
    }

    public void setScanResult(byte[] scanResult) {
        this.scanResult = scanResult;
    }
}
