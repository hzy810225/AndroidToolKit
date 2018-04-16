package com.example.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationServices
import kotlin.math.log


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //  權限同意值
    private var userAgreePermissionCode = 1

    //  定位服務的物件
    private lateinit var userLocationClient: FusedLocationProviderClient

    //  更新的值會在這個物件出現
    private lateinit var locationCallback: LocationCallback

    //  可不可以得到上一次定位
    private var canGetLastLocation = true
    //  可不可以開啟定位更新
    private var canStartUpdate = false
    //  可不可以停止定位更新
    private var canStopUpdate = false

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

        //  首次定位按鈕 => 只能開啟定位更新
        findViewById<Button>(R.id.InitialLocate).setOnClickListener{
            if(canGetLastLocation){
                userLocationClient = LocationServices.getFusedLocationProviderClient(this)
                userLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            //  上一次定位的經緯度在這裡 => 如果沒紀錄 => 可能為空
                            findViewById<TextView>(R.id.Longitude).text = location?.longitude.toString()
                            findViewById<TextView>(R.id.Latitude).text = location?.latitude.toString()
                        }
                canGetLastLocation = false
                canStartUpdate = true
                canStopUpdate = false
            }else{
                Log.i("Message:","You need reset!!!")
            }
        }

        //  開始更新定位按鈕 => 可以停止定位更新
        findViewById<Button>(R.id.StartUpdateLocate).setOnClickListener{
            if(canStartUpdate){

                //  更新的參數
                val userLocationRequest = LocationRequest().apply {
                    interval = 1000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                //  更新後的值在這邊出現
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (location in locationResult.locations){
                            //  更新的經緯度在這裡
                            findViewById<TextView>(R.id.Longitude).text = location?.longitude.toString()
                            findViewById<TextView>(R.id.Latitude).text = location?.latitude.toString()
                        }
                    }
                }

                //  啟動這個更新功能
                userLocationClient.requestLocationUpdates(userLocationRequest,locationCallback,null)

                //  只能夠停止更新
                canGetLastLocation = false
                canStartUpdate = false
                canStopUpdate = true
            }else{

                //  你沒有首次定位
                Log.i("Message:","You need locate first or you already start update")
            }
        }

        //  停止更新定位按鈕
        findViewById<Button>(R.id.StopUpdateLocate).setOnClickListener{
            if(canStopUpdate){

                //  刪除 Update 功能
                userLocationClient.removeLocationUpdates(locationCallback)

                //  重新啟動一次定位服務
                userLocationClient = LocationServices.getFusedLocationProviderClient(this)
                //  只能夠開啟更新
                canStartUpdate = true
                canStopUpdate = false
                canGetLastLocation = false
            }else{
                Log.i("Message:","you already stop update!!")
            }
        }

        //  重設定位按鈕
        findViewById<Button>(R.id.Reset).setOnClickListener{
            //  只能首次定位
            canStartUpdate = false
            canStopUpdate = false
            canGetLastLocation = true
        }

        //  離開按鈕
        findViewById<Button>(R.id.Close).setOnClickListener{
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

    //  這三個我不知道要幹嘛，但是不寫會錯-.-
    override fun onConnected(p0: Bundle?) {}
    override fun onConnectionSuspended(p0: Int) {}
    override fun onConnectionFailed(p0: ConnectionResult) {}
    //  -----------------------------------------

}
