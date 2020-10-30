package com.mobcom.paintly

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing1)
        button = findViewById(R.id.landpage1_next)
        button.setOnClickListener() {
            val intent_lp1 = Intent(this, Landing2Activity::class.java)
//            startActivity(intent_lp1)
            startActivityForResult(intent_lp1, 2)
            CustomIntent.customType(this, "left-to-right")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        supportActionBar!!.title = ""
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == 1){
            finish()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
