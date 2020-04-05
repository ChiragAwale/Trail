package com.chiragawale.trail.dao;


import android.util.Log;

import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.CustomTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmResults;

public class DaoImpl implements Dao {
    public static long entryId;
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
        e.setUploaded(false);
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

    @Override
    public List<RealmEntry> getEntryListToUpload() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(RealmEntry.class).equalTo("isUploaded", false).findAll();
    }

    @Override
    public void markUploadedEntryList() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<RealmEntry> entries1 = realm1.where(RealmEntry.class).equalTo("isUploaded", false).findAll();
            entries1.setValue("isUploaded",true);
        });
    }
    //    @Override
//    public List<RealmEntry> markUploadedEntryList(List<Long> uploadedEntries) {
//        Realm realm = Realm.getDefaultInstance();
//        //RealmResults<Person> persons = realm.where(Person.class).equalTo("invited", false).findAll();
//        return realm.where(RealmEntry.class).equalTo("isUploaded", false).findAll();
//    }
}
