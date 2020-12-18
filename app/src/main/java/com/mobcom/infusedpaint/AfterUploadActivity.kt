package com.mobcom.infusedpaint

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobcom.infusedpaint.R.layout.afterchoosingimage
import kotlinx.android.synthetic.main.afterchoosingimage.*
import maes.tech.intentanim.CustomIntent
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt


class AfterUploadActivity : AppCompatActivity() {
    var media: String? = null
    lateinit var imageBitmap: Bitmap
    var image_uri: Uri? = null
    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(afterchoosingimage)
        val intent_action: Intent
        media = intent.getStringExtra("MEDIA")
        val styleId = intent.getStringExtra("STYLE_ID")


        if (media == "CAMERA") {
            val intent_action : Intent
            //if system os is Marshmallow or Above, we need to request runtime permission

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            intent_action = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            CustomIntent.customType(this, "fadein-to-fadeout")
            startActivityForResult(intent_action, 100)

        } else {
            intent_action = Intent(Intent.ACTION_GET_CONTENT)
            intent_action.type = "image/*"
            CustomIntent.customType(this, "fadein-to-fadeout")
            startActivityForResult(intent_action, 100)
        }





        button_process.setOnClickListener {
            // Convert to byte array
            val stream = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            // pindah ke halaman proses pake intent
            val intent = Intent(this, ProcessingActivity::class.java)
            intent.putExtra("STYLE_ID", styleId)
            intent.putExtra("IMAGE", byteArray)
            startActivity(intent)
            finish()
            CustomIntent.customType(this, "fadein-to-fadeout")
        }

        button_no.setOnClickListener {
            finish()
            CustomIntent.customType(this, "fadein-to-fadeout")
        }
    }




    //gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (media == "CAMERA") {

                        imageBitmap = data?.extras?.get("data") as Bitmap
                        image_view.setImageBitmap(imageBitmap)


                    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
                        imageBitmap = data.extras?.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

                        Glide.with(this).load(imageBitmap).into(image_view)
                    }

                } else {
                    val bitmap_img = ImageHelper.loadSizeLimitedBitmapFromUri(
                        data!!.data,
                        this.contentResolver, 768
                    )!!

                    imageBitmap = bitmap_img
                        Bitmap.createScaledBitmap(
                            bitmap_img,
                            (bitmap_img.width * 0.9).roundToInt(),
                            (bitmap_img.height * 0.9).roundToInt(),
                            false
                        )

                    Glide.with(this).load(imageBitmap).into(image_view)
                }
            } else {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
