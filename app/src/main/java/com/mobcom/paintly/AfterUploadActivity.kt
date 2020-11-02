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

        val intent = intent
        val bitmap = intent.getParcelableExtra<Parcelable>("BitmapImage") as? Bitmap
        val d: Drawable = BitmapDrawable(bitmap)

        image_view.setImageDrawable(d)
    }
}