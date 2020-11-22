package com.mobcom.paintly

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.mobcom.paintly.R.layout.afterchoosingimage
import kotlinx.android.synthetic.main.afterchoosingimage.*
import maes.tech.intentanim.CustomIntent
import java.io.ByteArrayOutputStream


class AfterUploadActivity() : AppCompatActivity() {
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(afterchoosingimage)

        val media = intent.getStringExtra("MEDIA")
        val styleId = intent.getStringExtra("STYLE_ID")
        var intent_action: Intent
        if (media == "CAMERA") {
            intent_action = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        } else {
            intent_action = Intent(Intent.ACTION_GET_CONTENT)
        }

        intent_action.type = "image/*"
        CustomIntent.customType(this, "fadein-to-fadeout")
        startActivityForResult(intent_action, HomeFragment().REQUEST_GALLERY)

        button_process.setOnClickListener() {
            // Convert to byte array
            val stream = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            // pindah ke halaman proses pake intent
            val intent = Intent(this, ProcessingActivity::class.java)
            intent.putExtra("STYLE_ID", styleId)
            intent.putExtra("IMAGE", byteArray)
            startActivity(intent)
            finish()
            CustomIntent.customType(this, "fadein-to-fadeout")
        }
    }

    //gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == HomeFragment().REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                imageBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                    data!!.data,
                    this.contentResolver, HomeFragment().IMAGE_MAX_SIDE_LENGTH

                )
                image_view.setImageBitmap(imageBitmap)
            } else {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
