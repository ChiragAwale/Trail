package com.chiragawale.trail.dao;

import com.chiragawale.trail.models.RealmEntry;

import java.util.List;

import io.realm.Realm;

public interface Dao {
    Realm realm = Realm.getDefaultInstance();
    public void addEntry(RealmEntry realmEntry);
    public List<RealmEntry> getEntryList();

}
