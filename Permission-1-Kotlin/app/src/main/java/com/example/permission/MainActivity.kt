package com.example.permission

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log


class MainActivity : AppCompatActivity() {

    //  同意的值
    private var userAgreePermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  你要開啟的權限清單
        val somePermission = arrayOf(android.Manifest.permission.READ_CONTACTS,
                                    android.Manifest.permission.WRITE_CONTACTS,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_SMS,
                                    android.Manifest.permission.CAMERA)

        //  一個一個檢查，有一個權限沒開啟就要求全部權限，接著直接跳出迴圈
        //  但在跳出迴圈之前會進入 onRequestPermissionsResult
        for (p in somePermission ){
            if(ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED)

                //  同一類的危險權限只會問一次，如果不同意，那類危險權限就權部都不同意
                ActivityCompat.requestPermissions(this, somePermission , userAgreePermissionCode)
                break
        }
    }
    //  -----------------------------------------
    //  這裡很重要
    //  grantResults 裡面有所有權限的值
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            userAgreePermissionCode -> {
                //  有 I 個權限，一個一個問，有一個不同意就不給他開啟
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
