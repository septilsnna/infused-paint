package com.mobcom.paintly

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mobcom.paintly.R.layout.afterchoosingimage
import kotlinx.android.synthetic.main.afterchoosingimage.*


class AfterUploadActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(afterchoosingimage)
    }
}