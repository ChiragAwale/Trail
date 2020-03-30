package com.chiragawale.trail.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.chiragawale.trail.BaseActivity;
import com.chiragawale.trail.R;
import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.CustomTimeUtils;
import com.ederdoski.simpleble.interfaces.BleCallback;
import com.ederdoski.simpleble.models.BluetoothLE;
import com.ederdoski.simpleble.utils.BluetoothLEHelper;
import com.ederdoski.simpleble.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;

public class BluetoothUtility extends BaseActivity {

    BluetoothLEHelper ble;
    AlertDialog dAlert;

    ListView listBle;
    Button btnScan;
    Button btnWrite;
    Button btnRead;
    Realm realm;

    private AlertDialog setDialogInfo(String title, String message, boolean btnVisible){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_standard, null);

        TextView btnNeutral = view.findViewById(R.id.btnNeutral);
        TextView txtTitle   = view.findViewById(R.id.txtTitle);
        TextView txtMessage = view.findViewById(R.id.txtMessage);

        txtTitle.setText(title);
        txtMessage.setText(message);

        if(btnVisible){
            btnNeutral.setVisibility(View.VISIBLE);
        }else{
            btnNeutral.setVisibility(View.GONE);
        }

        btnNeutral.setOnClickListener(view1 -> {
            dAlert.dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private void setList(){

        ArrayList<BluetoothLE> aBleAvailable  = new ArrayList<>();

        if(ble.getListDevices().size() > 0){
            for (int i=0; i<ble.getListDevices().size(); i++) {
                aBleAvailable.add(new BluetoothLE(ble.getListDevices().get(i).getName(), ble.getListDevices().get(i).getMacAddress(), ble.getListDevices().get(i).getRssi(), ble.getListDevices().get(i).getDevice()));
                RealmEntry realmEntry = new RealmEntry();
                realmEntry.setName(ble.getListDevices().get(i).getName());
                realmEntry.setMacAddress(ble.getListDevices().get(i).getMacAddress());
                realmEntry.setRssi(ble.getListDevices().get(i).getRssi());
                realmEntry.setTime(CustomTimeUtils.currentTimeStamp());
                realmEntry.setLocation("N/A");
                dao.addEntry(realmEntry);
            }

            ListAdapter mAdapter = new ListAdapter(this, R.layout.simple_row_list, aBleAvailable) {
                @Override
                public void onItem(Object item, View view, int position) {

                    TextView txtName = view.findViewById(R.id.txtText);

                    String aux = "Name " + ((BluetoothLE) item).getName() + " Mac: " + ((BluetoothLE) item).getMacAddress() + " Rssi: "+  ((BluetoothLE) item).getRssi()   ;
                    txtName.setText(aux);

                }
            };

            listBle.setAdapter(mAdapter);
            listBle.setOnItemClickListener((parent, view, position, id) -> {
                BluetoothLE  itemValue = (BluetoothLE) listBle.getItemAtPosition(position);
                ble.connect(itemValue.getDevice(), bleCallbacks());
            });
        }else{
            dAlert = setDialogInfo("Ups", "We do not find active devices", true);
            dAlert.show();
        }
    }

    private BleCallback bleCallbacks(){

        return new BleCallback(){

            @Override
            public void onBleConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onBleConnectionStateChange(gatt, status, newState);

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    runOnUiThread(() -> Toast.makeText(BluetoothUtility.this, "Connected to GATT server.", Toast.LENGTH_SHORT).show());
                }

                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    runOnUiThread(() -> Toast.makeText(BluetoothUtility.this, "Disconnected from GATT server.", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onBleServiceDiscovered(BluetoothGatt gatt, int status) {
                super.onBleServiceDiscovered(gatt, status);
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    Log.e("Ble ServiceDiscovered","onServicesDiscovered received: " + status);
                }
            }

            @Override
            public void onBleCharacteristicChange(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onBleCharacteristicChange(gatt, characteristic);
                Log.i("BluetoothLEHelper","onCharacteristicChanged Value: " + Arrays.toString(characteristic.getValue()));
            }

            @Override
            public void onBleRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onBleRead(gatt, characteristic, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.i("TAG", Arrays.toString(characteristic.getValue()));
                    runOnUiThread(() -> Toast.makeText(BluetoothUtility.this, "onCharacteristicRead : "+Arrays.toString(characteristic.getValue()), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onBleWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onBleWrite(gatt, characteristic, status);
                runOnUiThread(() -> Toast.makeText(BluetoothUtility.this, "onCharacteristicWrite Status : " + status, Toast.LENGTH_SHORT).show());
            }
        };
    }

    private void scanCollars(){

        if(!ble.isScanning()) {

            dAlert = setDialogInfo("Scan in progress", "Loading...", false);
            dAlert.show();

            Handler mHandler = new Handler();
            ble.scanLeDevice(true);

            mHandler.postDelayed(() -> {
                dAlert.dismiss();
                setList();
            },ble.getScanPeriod());

        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    private void listenerButtons(){

        btnScan.setOnClickListener(v -> {
            if(ble.isReadyForScan()){
                scanCollars();
            }else{
                Toast.makeText(BluetoothUtility.this, "You must accept the bluetooth and Gps permissions or must turn on the bluetooth and Gps", Toast.LENGTH_SHORT).show();
            }
        });

        btnRead.setOnClickListener(v -> {
            if(ble.isConnected()) {
                ble.read(Constants.SERVICE_COLLAR_INFO, Constants.CHARACTERISTIC_CURRENT_POSITION);
            }
        });

        btnWrite.setOnClickListener(v -> {
            if(ble.isConnected()) {
                byte[] aBytes = new byte[8];
                ble.write(Constants.SERVICE_COLLAR_INFO, Constants.CHARACTERISTIC_GEOFENCE, aBytes);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ble.disconnect();
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_utility);

        //--- Initialize BLE Helper
        ble = new BluetoothLEHelper(this);

        listBle  = findViewById(R.id.listBle);
        btnScan  = findViewById(R.id.scanBle);
        btnRead  = findViewById(R.id.readBle);
        btnWrite = findViewById(R.id.writeBle);

        listenerButtons();
        realm = Realm.getDefaultInstance();
        //--- Delete this line to do a search of all the devices
        //ble.setFilterService(Constants.SERVICE_COLLAR_INFO);

    }
}
