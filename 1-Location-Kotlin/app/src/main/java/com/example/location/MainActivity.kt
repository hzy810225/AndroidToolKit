package com.example.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnSuccessListener



class MainActivity : AppCompatActivity() {

    private lateinit var userLocationProviderClient: FusedLocationProviderClient
    private var userAgreePermissionCode = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  檢查並要求權限 => "onRequestPermissionsResult"
        val currentPermission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        if( currentPermission!=PackageManager.PERMISSION_GRANTED ) ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), userAgreePermissionCode)

        //  定位按鈕執行
        findViewById<Button>(R.id.Locate).setOnClickListener{
            userLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            userLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                val userLongitude = location?.longitude
                val userLatitude = location?.latitude
                findViewById<TextView>(R.id.Longitude).text = userLongitude.toString()
                findViewById<TextView>(R.id.Latitude).text = userLatitude.toString()
            }
        }

        //  重置按鈕執行
        findViewById<Button>(R.id.Reset).setOnClickListener{
            findViewById<TextView>(R.id.Longitude).text = "經度(Longitude)"
            findViewById<TextView>(R.id.Latitude).text = "緯度(Latitude)"
        }

        //  離開按鈕執行
        findViewById<Button>(R.id.Close).setOnClickListener{
            finish()
        }
    }

    //  ----------------------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            userAgreePermissionCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //  Permission was granted.
                }else{
                    //  Permission denied.
                    finish()
                }
            }
            //  Other
            else -> {}
        }
    }
    //  ----------------------------------------------------
}

