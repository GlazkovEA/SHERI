package com.homounikumus1.sheri.drawer_fragments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Homo__000 on 09.05.2018.
 */

abstract class LocalMark extends ArrayList<Parcelable> implements Comparable<LocalMark>, Parcelable {
    private double generalDishMark;
    private String coast;
    private String address;
    private String name;
    private int position;
    private double placeMark;
    private int quantity;
    private String latLng;
    private List<String> list;
    private String paths;

    LocalMark(int localPos, String name, String address,
              String latLng, String cost, double dishMark, double placeMark, String paths) {
        this.position = localPos;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.coast = cost;
        this.generalDishMark = dishMark;
        this.placeMark = placeMark;
        this.paths = paths;

    }

    public String getPaths() {
        return paths;
    }

    private LocalMark(Parcel in) {
        super();
        readFromParcel(in);
    }

    public List<String> getList() {
        return list;
    }

    public String getLatLng() {
        return latLng;
    }

    public double getGeneralDishMark() {
        return generalDishMark;
    }

    public String getCoast() {
        return coast;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPlaceMark(double placeMark) {
        this.placeMark = placeMark;
    }

    public double getPlaceMark() {

        return placeMark;
    }

    public int getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }


    @Override
    public abstract int compareTo(@NonNull LocalMark o);

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public static final Parcelable.Creator<LocalMark> CREATOR = new Parcelable.Creator<LocalMark>() {
        public LocalMark createFromParcel(Parcel in) {
            return new LocalMark(in) {
                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public int compareTo(@NonNull LocalMark o) {
                    return 0;
                }
            };
        }

        public LocalMark[] newArray(int size) {

            return new LocalMark[size];
        }

    };

    private void readFromParcel(Parcel in) {
        name = in.readString();
        address = in.readString();
        generalDishMark = in.readDouble();
        position = in.readInt();
        coast = in.readString();
        latLng = in.readString();
        paths = in.readString();
        placeMark = in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(coast);
        dest.writeString(latLng);
        dest.writeDouble(generalDishMark);
        dest.writeDouble(placeMark);
        dest.writeString(paths);

    }


}

