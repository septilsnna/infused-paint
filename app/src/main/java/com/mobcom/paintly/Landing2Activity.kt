package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class Landing2Activity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing2)
        button = findViewById(R.id.landpage2_next)
        button.setOnClickListener() {
            goToPage3()
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

    private fun goToPage3() {
        val intent = Intent(this, Landing3Activity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "left-to-right")
    }

}