package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent


class HomeActivity : AppCompatActivity() {
    private lateinit var button: Button
    val UploadSheetFragment = UploadSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        button = findViewById(R.id.btn_starryNight)
//        button.setOnClickListener() {
//            UploadSheetFragment.show(supportFragmentManager, "UploadSheetDialog")
//        }
    }
}


