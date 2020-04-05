package com.chiragawale.trail.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chiragawale.trail.dao.Dao;
import com.chiragawale.trail.dao.DaoImpl;
import com.chiragawale.trail.models.RealmEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UploadWorker extends Worker {
    Context context;
    Dao dao;
    List<RealmEntry> realmEntries;
    private static final String WORK_RESULT = "work_result";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.dao = new DaoImpl();
    }

    @NonNull
    @Override
    public Result doWork() {
        Gson gson  = new Gson();
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        URL url = null;
        //List<Long> uploadedEntries = new ArrayList<>();
        realmEntries = dao.getEntryListToUpload();
        if(!realmEntries.isEmpty()) {
            try {
                url = new URL("https://trail34.herokuapp.com/entrys/post");
            } catch (MalformedURLException e) {
                Log.e("Upload Worker", e.getMessage());
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                String json = "[";
                for (int i = 0; i < realmEntries.size() - 1; i++) {
                    json += gson.toJson(RealmEntry.serializeEntry(realmEntries.get(i))) + ",";
                    //   uploadedEntries.add(realmEntry.getEntryId());
                }
                json += gson.toJson(RealmEntry.serializeEntry(realmEntries.get(realmEntries.size() - 1))) + "]";
                writer.write(json);
                writer.flush();
                Log.e("STATUS", String.valueOf(conn.getResponseCode()));
                Log.e("MSG", conn.getResponseMessage());
                writer.close();
                conn.disconnect();
                dao.markUploadedEntryList();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("UW", e.getMessage());
            }
        } else Log.e("UW","Emplty list");
        return Result.success(outputData);
    }
}
