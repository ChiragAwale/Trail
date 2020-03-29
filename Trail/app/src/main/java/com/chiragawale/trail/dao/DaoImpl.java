package com.chiragawale.trail.dao;

import com.chiragawale.trail.models.RealmEntry;

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
}
