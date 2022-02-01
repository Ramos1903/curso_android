package com.example.bluetoothfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Type;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button searchBtn;
    TextView textView;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayAdapter arrayAdapter;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                textView.setText("Finished");
                searchBtn.setEnabled(true);
            } else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                Log.i("Device FOUND", "name: " + name + " Adress: " + address + " RSSI range: " + rssi);

                String deviceString = "";

                if(name == null || name.equals("")){
                    deviceString = address + " - RSSI" + rssi + "dBm";
                    //bluetoothDevices.add(address + " - RSSI" + rssi + "dBm");
                } else {
                    deviceString = name + " - RSSI" + rssi + "dBm";
                    //bluetoothDevices.add(name + " - RSSI" + rssi + "dBm");
                }

                if (!bluetoothDevices.contains(deviceString)){
                    bluetoothDevices.add(deviceString);
                }

                arrayAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        searchBtn = findViewById(R.id.searchBtn);
        textView = findViewById(R.id.textView);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(broadcastReceiver, intentFilter);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        listView.setAdapter(arrayAdapter);

        searchBtn.setOnClickListener(v -> {
            textView.setText("SEARCHING ...");
            searchBtn.setEnabled(false);
            bluetoothDevices.clear();
            bluetoothAdapter.startDiscovery();
        });

    }
}