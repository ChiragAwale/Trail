package com.chiragawale.trail.models;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chiragawale.trail.SaveSharedPreference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmEntry extends RealmObject {
    private String name,macAddress,location, type, bluetoothAddress, username;
    private int rssi;
    private double distance;
    private long ms_time,time;
    private boolean isUploaded;


    public RealmEntry(){}

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static JsonObject serializeEntry(RealmEntry realmEntry) {
        JsonObject object = new JsonObject();
        object.addProperty("location", realmEntry.getLocation());
        object.addProperty("type", realmEntry.getType());
        object.addProperty("bluetooth_address", realmEntry.getBluetoothAddress());
        object.addProperty("time", realmEntry.getTime());
        object.addProperty("username", realmEntry.getUsername());
        object.addProperty("app_key", "app key 0011");
        object.addProperty("ms_time", realmEntry.getMs_time());
        object.addProperty("distance", realmEntry.getDistance());
        Log.e("RealmEntry","OBJECT " +  new Gson().toJson(object));
        return object;
    }

    public RealmEntry(String name, long time, String location, String type, String bluetoothAddress, double distance, int rssi, long ms_time, String username) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.type = type;
        this.bluetoothAddress = bluetoothAddress;
        this.distance = distance;
        this.rssi = rssi;
        this.ms_time = ms_time;
        this.isUploaded = false;
        this.username = username;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
