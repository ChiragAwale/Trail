package com.chiragawale.trail.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chiragawale.trail.MainActivity;
import com.chiragawale.trail.R;
import com.chiragawale.trail.dao.Dao;
import com.chiragawale.trail.dao.DaoImpl;
import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.CustomTimeUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;


public class NotificationWorker extends Worker implements BeaconConsumer {
    protected static final String TAG = "NotificationWorker";
    private BeaconTransmitter beaconTransmitter;
    private static final String WORK_RESULT = "work_result";
    final Handler handler = new Handler(Looper.getMainLooper());
    private BeaconManager beaconManager;
    private Dao dao = new DaoImpl();
    Region region;
    HashMap<String, RealmEntry> hmap;
    private FusedLocationProviderClient fusedLocationClient;
    String mlocation = "";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            mlocation = "Lat:"+ location.getLatitude() + "Long:" + location.getLongitude();
                        }
                    }
                });
    }
    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);
        showNotification("WorkManager", taskDataString != null ? taskDataString : "All set");
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        for(int i = 0; i < 1;i ++) {
            processWork();
            try {
                Thread.sleep(130000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage() + " ");
            }
        }
        Log.e(TAG,"END WORK");
        return Result.success(outputData);
    }

    public void processWork(){
        rangerStart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRanging();
                Log.e("worker", "Paused ranging ");
                transmit();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopTransmit();
                        Log.e("worker", "Paused transmitting ");
                    }
                }, 60000);
            }
        }, 60000);
    }
    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, builder.build());
    }

    private void transmit(){
        Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                .setId2("1")
                .setId3("3")
                .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {5l})) // Remove this for beacon layouts without d: fields
                .build();
        // Change the layout below for other beacon types
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BEACON", "Advertisement start failed with code: "+errorCode);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.e("BEACON", "Advertisement start succeeded.");
            }
        });
    }

    private  void stopTransmit(){
        beaconTransmitter.stopAdvertising();
        Log.e(TAG,"Stopped transmit " + hmap.size());
    }

    private  void stopRanging(){
        beaconManager.removeAllRangeNotifiers();
        try {
            beaconManager.stopMonitoringBeaconsInRegion(region);
        } catch (RemoteException e){Log.e(TAG,"Remote Exception");}
        beaconManager.unbind(this);

        dao.addMap(hmap);
        Log.e(TAG,"MAP Added stoped ranging " + hmap.size());
        Toast.makeText(getApplicationContext(),"MAP ADDED STOPPED RANGIN "+ hmap.size(),Toast.LENGTH_LONG).show();
    }

    public void rangerStart(){
        hmap = new HashMap<>();
//        BluetoothAdapter.getDefaultAdapter().disable();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "bluetooth adapter try to enable");
//                BluetoothAdapter.getDefaultAdapter().enable();
//            }}, 500);
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        Log.e(TAG, "Oncreate reached");
        beaconManager.bind(this);

    }

    @Override
    public void onBeaconServiceConnect() {
        region = new Region("myRangingUniqueId", null, null, null);
        Log.e(TAG, "Ranging Connect");
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Beacon beacon = beacons.iterator().next();
//                    Log.e(TAG, "The first beacon I see is about "+beacon.getBluetoothAddress() + " " + beacons.iterator().next().getDistance()+" meters away.")
                    Log.e(TAG, "BAddress " + beacon.getBluetoothAddress() + " Bname " + beacon.getBluetoothName() );
                    Log.e(TAG, "Distance " + beacon.getDistance() + " idfer " + beacon.getIdentifier(1));
                    RealmEntry entry = new RealmEntry("tName", CustomTimeUtils.currentTimestampLong(),mlocation,"beacon",beacon.getBluetoothAddress(),beacon.getDistance(),beacon.getRssi(),CustomTimeUtils.trimmedCurrentTimestampLong());
                    hmap.put(beacon.getBluetoothAddress(),entry);
                }
            }
        });

        try {
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {    }
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void onStopped() {
        super.onStopped();
        stopRanging();
        stopTransmit();
    }
}