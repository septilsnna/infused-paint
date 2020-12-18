package com.mobcom.infusedpaint

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable {
            try {
                val intent = Intent(applicationContext, BottomNavActivity::class.java)
                startActivity(intent)
                finish()
                CustomIntent.customType(this, "fadein-to-fadeout")
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 250)
    }
}