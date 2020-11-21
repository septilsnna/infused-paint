package com.mobcom.paintly

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.mobcom.paintly.R.layout.activity_result
import com.mobcom.paintly.R.layout.afterchoosingimage
import kotlinx.android.synthetic.main.afterchoosingimage.*
import maes.tech.intentanim.CustomIntent
import java.io.ByteArrayOutputStream


class ResultActivity() : AppCompatActivity() {
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_result)


    }
}
