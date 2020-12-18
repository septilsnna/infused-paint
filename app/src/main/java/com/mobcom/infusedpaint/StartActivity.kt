package com.mobcom.infusedpaint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val login_fragment = LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_start, login_fragment)
            commit()
        }
    }

    override fun finish() {
        CustomIntent.customType(this, "fadein-to-fadeout")
        super.finish()
    }
}