package com.chiragawale.trail.dao;

import android.util.TimeUtils;

import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.CustomTimeUtils;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public class DaoImpl implements Dao {
    @Override
    public void addEntry(RealmEntry realmEntry) {
        Realm realm = Realm.getDefaultInstance();
        try {
        realm.beginTransaction();
        RealmEntry e = realm.createObject(RealmEntry.class);
        e.setName(realmEntry.getName());
        e.setBluetoothAddress(realmEntry.getBluetoothAddress());
        e.setDistance(realmEntry.getDistance());
        e.setTime(realmEntry.getTime());
        e.setType(realmEntry.getType());
        e.setMacAddress(realmEntry.getMacAddress());
        e.setRssi(realmEntry.getRssi());
        e.setMs_time(realmEntry.getMs_time());
        realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    @Override
    public void addMap(HashMap<String , RealmEntry> hmap) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insert(hmap.values());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


    @Override
    public List<RealmEntry> getEntryList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(RealmEntry.class).findAll();
    }

    @Override
    public List<RealmEntry> getEntryListToday() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(RealmEntry.class).equalTo("ms_time", CustomTimeUtils.trimmedCurrentTimestampLong()).findAll();
    }
}
