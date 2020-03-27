package com.chiragawale.trail.dao;

import com.chiragawale.trail.models.RealmEntry;
import java.util.List;

public class DaoImpl implements Dao {
    @Override
    public void addEntry(RealmEntry realmEntry) {
        realm.beginTransaction();
        RealmEntry e = realm.createObject(RealmEntry.class);
        e.setName(realmEntry.getName());
        e.setMacAddress(realmEntry.getMacAddress());
        e.setRssi(realmEntry.getRssi());
        e.setTime(realmEntry.getTime());
        e.setLocation("N/A");
        realm.commitTransaction();
    }

    @Override
    public List<RealmEntry> getEntryList() {
        return realm.where(RealmEntry.class).findAll();
    }
}
