package com.example.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    //  權限同意值
    private var userAgreePermissionCode = 1

    //  定位服務的物件
    private lateinit var userLocationClient: FusedLocationProviderClient

    //  更新的值會在這個物件出現
    private lateinit var userLocationCallback: LocationCallback

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  權限 => 會呼叫 onRequestPermissionsResult
        val somePermission = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
        for (p in somePermission ){
            if(ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, somePermission , userAgreePermissionCode)
            break
        }

        //  獲取目前位置的按鈕
        findViewById<Button>(R.id.Locate).setOnClickListener{

            //  啟動定位物件
            userLocationClient = LocationServices.getFusedLocationProviderClient(this)

            //  定位參數
            val userLocationRequest = LocationRequest().apply {
                interval = 1000*60*60*240  //  只想要定位一次 => 所以更新頻率足夠大就可以 => 很少人 APP 可以用 10 天的 => 但會不會很耗電就不知道了
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            //  當前定位的結果在這邊出現
            userLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations){

                        //  當前使用者的經緯度在這裡 => 這裡不用說明可能非空 => 因為是先更新再得到座標
                        findViewById<TextView>(R.id.Longitude).text = location.longitude.toString()
                        findViewById<TextView>(R.id.Latitude).text = location.latitude.toString()
                    }
                }
            }

            //  啟動更新功能
            userLocationClient.requestLocationUpdates(userLocationRequest,userLocationCallback,null)

        }

        //  離開按鈕
        findViewById<Button>(R.id.Close).setOnClickListener{

            //  關掉更新
            userLocationClient.removeLocationUpdates(userLocationCallback)
            finish()
        }
    }

    //  -----------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            userAgreePermissionCode -> {
                for( i in 0..(grantResults.size-1) ){
                    if ((grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED))
                        Log.i("Status:", "Agree a permission")
                    else
                        finish()
                }
                return
            }
        }
    }
    //  -----------------------------------------
}
