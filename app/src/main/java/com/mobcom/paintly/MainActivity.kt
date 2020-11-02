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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
