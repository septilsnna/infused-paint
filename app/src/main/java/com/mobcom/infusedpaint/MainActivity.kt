package com.mobcom.infusedpaint

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import maes.tech.intentanim.CustomIntent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (checkPersmission()) startApp() else requestPermission()
    }


    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),
            100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ) {

                    startApp()

                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    private fun startApp() {
        val landing1_fragment = Landing1Fragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_main, landing1_fragment)
            commit()
        }

    }

    override fun finish() {
        CustomIntent.customType(this, "fadein-to-fadeout")
        super.finish()
    }

    override fun onResume() {
        supportActionBar!!.title = ""
        super.onResume()
    }
}
