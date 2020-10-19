package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class Landing1Activity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing1)
        button = findViewById(R.id.landpage1_next)
        button.setOnClickListener() {
            goToPage2()
        }
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

    private fun goToPage2() {
        val intent = Intent(this, Landing2Activity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "left-to-right")
    }
}