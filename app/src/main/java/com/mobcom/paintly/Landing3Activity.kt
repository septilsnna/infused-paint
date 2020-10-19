package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class Landing3Activity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing3)
        button = findViewById(R.id.button_ready)
        button.setOnClickListener() {
            goToLoginPage()
        }
    }

    override fun finish() {
        super.finish()
        CustomIntent.customType(this, "right-to-left")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun goToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}