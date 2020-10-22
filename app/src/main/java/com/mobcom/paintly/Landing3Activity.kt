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
        setResult(1)
        super.onStop()
    }

    override fun onDestroy() {
        setResult(1)
        super.onDestroy()
    }

    private fun goToLoginPage() {
        val intent_lp3 = Intent(this, LoginActivity::class.java)
//        intent_lp3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent_lp3)
        startActivityForResult(intent_lp3, 1)
        CustomIntent.customType(this, "fadein-to-fadeout")
        finish()
    }
}