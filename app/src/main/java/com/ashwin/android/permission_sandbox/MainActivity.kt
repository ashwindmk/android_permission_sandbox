package com.ashwin.android.permission_sandbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG = "permission-sandbox"
const val PERMISSIONS_REQUEST_READ_CONTACTS = 1024

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)

        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "delay: start")
            for (i in 1..15) {
                delay(1_000)
                Log.d(TAG, "  i: $i")
            }
            Log.d(TAG, "delay: finish")
            val contact = getContact(this@MainActivity, "Ashwin", "ashwin@email.com")
            Log.d(TAG, "Contact: $contact")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}