package com.chiragawale.trail.models;

import android.bluetooth.BluetoothDevice;

import io.realm.RealmObject;

public class RealmEntry extends RealmObject {

    private String name,macAddress,time,location, type, bluetoothAddress;
    private int rssi;
    private double distance;
    private long ms_time;


    public RealmEntry(){}

    public RealmEntry(String name, String time, String location, String type, String bluetoothAddress, double distance, int rssi, long ms_time) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.type = type;
        this.bluetoothAddress = bluetoothAddress;
        this.distance = distance;
        this.rssi = rssi;
        this.ms_time = ms_time;
    }

    public long getMs_time() {
        return ms_time;
    }

    public void setMs_time(long ms_time) {
        this.ms_time = ms_time;
    }

    @Override
    public String toString() {
        return name + " Mac: " + macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }
}
