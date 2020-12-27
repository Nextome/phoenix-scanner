package net.nextome.phoenix_scanner.models.beacon;

import java.util.LinkedList;

/**
 * Raw results from a beacon scanning
 */
public class NextomeBeaconListRaw {
    private LinkedList<NextomeRssiBean> beaconList = new LinkedList<>();
    private long updatedAt;
    private int maxListSize;

    public NextomeBeaconListRaw(int maxListSize) {
        this.maxListSize = maxListSize;
    }

    public void addBeacon(NextomeRssiBean b) {
        if (b != null) {

            if (beaconList.size() >= maxListSize) {
                beaconList.pollLast();
                beaconList.push(b);
            } else {
                beaconList.push(b);
            }

            updatedAt = System.currentTimeMillis();
        }
    }

    public LinkedList<NextomeRssiBean> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(LinkedList<NextomeRssiBean> beaconList) {
        this.beaconList = beaconList;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getMaxListSize() {
        return maxListSize;
    }

    public void setMaxListSize(int maxListSize) {
        this.maxListSize = maxListSize;
    }
}
