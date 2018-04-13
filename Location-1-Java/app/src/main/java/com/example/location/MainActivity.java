package com.example.location;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity{

    final private int userAgreePermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  要求與開啟權限
        int currentPermission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        if( currentPermission!=PackageManager.PERMISSION_GRANTED ) ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, userAgreePermissionCode);

        //  定位按鈕
        findViewById(R.id.Locate).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                //  getApplicationContext() 取代 this => 我不知道為何但是這樣可以跑
                FusedLocationProviderClient userLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                //  獲取定位的座標 => 可能會出現 null => 然後出錯唷 => 別害怕 => 在其他範例可以找到答案
                userLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String userLastLongitude = location.getLongitude() + "";
                        String userLastLatitude  = location.getLatitude() + "";
                        TextView Longitude = findViewById(R.id.Longitude);
                        TextView Latitude = findViewById(R.id.Latitude);
                        Longitude.setText(userLastLongitude);
                        Latitude.setText(userLastLatitude);
                    }
                });
            }
        });

        //  離開按鈕
        findViewById(R.id.Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case userAgreePermissionCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.i("Status:","Granted");
                } else {
                    // Permission Denied
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
