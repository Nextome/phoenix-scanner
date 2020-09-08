package net.nextome.phoenix_scanner.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NMLocationBroadcast implements Parcelable {
    private double x;
    private double y;

    public NMLocationBroadcast(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected NMLocationBroadcast(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NMLocationBroadcast> CREATOR = new Creator<NMLocationBroadcast>() {
        @Override
        public NMLocationBroadcast createFromParcel(Parcel in) {
            return new NMLocationBroadcast(in);
        }

        @Override
        public NMLocationBroadcast[] newArray(int size) {
            return new NMLocationBroadcast[size];
        }
    };
}
