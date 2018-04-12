package com.example.scanqrcode

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.TextView
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    //  宣告使用者掃描物件
    private lateinit var userScannerView: ZXingScannerView

    //  使用者權限值
    private val userAgreePermission = 1

    private lateinit var goBackIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  要求權限
        val userCurrentPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if(userCurrentPermission!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),userAgreePermission)

        //  掃描按鈕執行
        findViewById<Button>(R.id.Scan).setOnClickListener{
            userScannerView = ZXingScannerView(this)
            setContentView(userScannerView)
            userScannerView.setResultHandler(this)
            userScannerView.startCamera()

        }

    }

    //  ---------------------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            userAgreePermission -> {
                if( (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) )
                else finish()
            }
        }

    }
    override fun handleResult(result: Result?) {
        val userScanResult = result?.text.toString()
        val goBackIntent = Intent(this,MainActivity::class.java)
        goBackIntent.putExtra("userScanResult", userScanResult)
        startActivity(goBackIntent)
    }
    //  ----------------------------------------------------
}
