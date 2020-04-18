package com.chiragawale.trail.dao;

import android.content.Context;

import com.chiragawale.trail.models.RealmEntry;

import java.util.HashMap;
import java.util.List;


public interface Dao {

    public void addEntry(RealmEntry realmEntry);
    public void addMap(HashMap<String , RealmEntry> hmap);
    public List<RealmEntry> getEntryList(Context context);
    public List<RealmEntry> getEntryListToday(Context context);
    public List<RealmEntry> getEntryListToUpload(Context context);
    public void markUploadedEntryList(Context context);
}
