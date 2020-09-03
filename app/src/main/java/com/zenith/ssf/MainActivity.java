package com.zenith.ssf;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    String deviceList = "start => ";
    TextView devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devices = (TextView) findViewById(R.id.level);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                assert device != null;
                deviceList += device.getName() + " # " + device.getAddress() + "\n";
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Toast.makeText(getApplicationContext(), "  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void updateText(View view) {
        if (bluetoothAdapter == null) {
            System.out.println("Device not supported :(");
            finishAndRemoveTask();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        bluetoothAdapter.startDiscovery();

        devices.setText(deviceList);
    }

    @Override
    protected void onDestroy() {
        devices.setText(R.string.done);
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}