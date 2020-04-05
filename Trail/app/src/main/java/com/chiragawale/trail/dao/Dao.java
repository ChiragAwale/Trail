package com.chiragawale.trail.dao;

import com.chiragawale.trail.models.RealmEntry;

import java.util.HashMap;
import java.util.List;


public interface Dao {

    public void addEntry(RealmEntry realmEntry);
    public void addMap(HashMap<String , RealmEntry> hmap);
    public List<RealmEntry> getEntryList();
    public List<RealmEntry> getEntryListToday();
    public List<RealmEntry> getEntryListToUpload();
}
