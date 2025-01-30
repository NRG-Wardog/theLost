package com.kira.the_lost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kira.the_lost.R;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothScanCallback mScanCallback;
    private static final String MICROBIT_NAME = "The_Lost_Microbit";
    private static final int NOTIFICATION_ID = 1;
    private Button btn_exit;
    private Button btn_settings;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        btn_settings = findViewById(R.id.btn_settings);
        btn_exit = findViewById(R.id.btn_exit);

        // Check if Bluetooth is supported and enabled
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not enabled or supported", Toast.LENGTH_SHORT).show();
            return;
        }

        mScanCallback = new BluetoothScanCallback();
        startScanning();

        // כפתור הגדרות
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS); // פותח את הגדרות הטלפון
                startActivity(intent);
            }
        });

        // כפתור יציאה
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // סוגר את האפליקציה
            }
        });
    }



    private void startScanning() {
        // Start scanning for Bluetooth devices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mBluetoothLeScanner.startScan(mScanCallback);
    }

    private class BluetoothScanCallback extends ScanCallback {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            if (MICROBIT_NAME.equals(device.getName())) {
                int rssi = result.getRssi();
                double distance = calculateDistance(rssi);
                if (distance > 5.0) {
                    sendNotification();
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    }

    private double calculateDistance(int rssi) {
        // Convert RSSI to distance in meters
        return Math.pow(10, (27.55 - (20 * Math.log10(2400)) + Math.abs(rssi)) / 20.0);
    }

    private static final String CHANNEL_ID = "default";

    private void sendNotification() {
        // Check if notification permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it's not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            return; // Exit to wait for user response
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("You are too far from the Micro:bit!")
                .setContentText("Move closer to avoid losing the connection.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Remove notification when clicked

        // Get NotificationManager and send notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());



    }


}
