package com.mobcom.paintly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent

class ProcessingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processing)

        val styleId = intent.getStringExtra("STYLE_ID")
        val byteArray = intent.getByteArrayExtra("IMAGE")

        val processing_fragment = ProcessingFragment()
        val arguments = Bundle()
        arguments.putString("STYLE_ID", styleId)
        arguments.putByteArray("IMAGE", byteArray)
        processing_fragment.arguments = arguments
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_processing, processing_fragment)

            commit()
        }
    }

    override fun finish() {
        CustomIntent.customType(this, "fadein-to-fadeout")
        super.finish()
    }
}