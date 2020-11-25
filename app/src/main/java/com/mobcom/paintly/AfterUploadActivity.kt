package com.mobcom.paintly

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobcom.paintly.R.layout.afterchoosingimage
import kotlinx.android.synthetic.main.afterchoosingimage.*
import kotlinx.android.synthetic.main.fragment_processing.view.*
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

        media = intent.getStringExtra("MEDIA")
        val styleId = intent.getStringExtra("STYLE_ID")
        val intent_action: Intent
        if (media == "CAMERA") {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            intent_action = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //intent_action = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //intent_action.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        } else {
            intent_action = Intent(Intent.ACTION_GET_CONTENT)
            intent_action.type = "image/*"
        }

        CustomIntent.customType(this, "fadein-to-fadeout")
        startActivityForResult(intent_action, 100)

        button_process.setOnClickListener() {
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
    }




    //gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (media == "CAMERA") {
                    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
                        val bitmap_img = data.extras?.get("data") as Bitmap
                        imageBitmap = Bitmap.createScaledBitmap(bitmap_img,
                            (bitmap_img.width * 0.7).roundToInt(), (bitmap_img.height * 0.7).roundToInt(), false)

                        Glide.with(this).load(imageBitmap).into(image_view)

                    }
                } else {
                    val bitmap_img = ImageHelper.loadSizeLimitedBitmapFromUri(
                        data!!.data,
                        this.contentResolver, 768
                    )!!

                    imageBitmap = Bitmap.createScaledBitmap(bitmap_img,
                        (bitmap_img.width * 0.7).roundToInt(), (bitmap_img.height * 0.7).roundToInt(), false)

                    Glide.with(this).load(imageBitmap).into(image_view)



                }
            } else {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
