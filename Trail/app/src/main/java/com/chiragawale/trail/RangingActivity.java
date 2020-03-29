package com.chiragawale.trail;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.TimeUtils;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends BaseActivity
        implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        Log.e(TAG, "Oncreate reached");
        beaconManager.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Beacon beacon = beacons.iterator().next();
//                    Log.e(TAG, "The first beacon I see is about "+beacon.getBluetoothAddress() + " " + beacons.iterator().next().getDistance()+" meters away.")
                    Log.e(TAG, "BAddress " + beacon.getBluetoothAddress() + " Bname " + beacon.getBluetoothName() );
                    Log.e(TAG, "Distance " + beacon.getDistance() + " idfer " + beacon.getIdentifier(1));
                    RealmEntry entry = new RealmEntry("tName", TimeUtils.currentTimeStamp(),"","beacon",beacon.getBluetoothAddress(),beacon.getDistance(),beacon.getRssi());
                    dao.addEntry(entry);
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }
}