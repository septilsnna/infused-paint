package com.mobcom.infusedpaint

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_processing.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class GalleryInfoDialog : AppCompatDialogFragment() {
    lateinit var mView: View
    lateinit var imageBitmap: Bitmap
    lateinit var email: String
    lateinit var user_id: String
    var fileResult: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_processing, null)

        fileResult = arguments?.getString("file_result")
        val decodedString = Base64.decode(fileResult, Base64.DEFAULT)
        imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        val sharedPreferences = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        email = sharedPreferences?.getString("email", "").toString()
        if (sharedPreferences != null) {
            user_id = sharedPreferences.getString("USER_ID","").toString()
        }

        mView.processing.visibility = View.GONE
        mView.result.visibility = View.VISIBLE
        mView.horay.visibility = View.GONE
        mView.backgroundeu.background = null
        Glide.with(this).load(imageBitmap).into(mView.result_image)

        mView.save_result.setOnClickListener {
            saveResult()
        }

        mView.share_result.setOnClickListener {
            shareResult()
        }

        builder.setView(mView).setTitle("Artwork Info")
        return builder.create()

    }

    private fun saveResult() {
        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            imageBitmap,
            "result_image.jpg",
            "Image of " + "result_image.jpg"
        )
        Uri.parse(savedImageURL)

        mView.save_result.isEnabled = false
        mView.save_result.isClickable = false
        Toast.makeText(activity, "Save Success!", Toast.LENGTH_SHORT).show()

        // logging
        logging(user_id, "Menyimpan hasil lukisan")

    }

    private  fun shareResult() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(activity?.contentResolver, imageBitmap, "tempimage", null)

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT,"I made this with Infused Paint!");

        // logging
        logging(user_id, "Membagikan hasil lukisan")

        // Step 7: Start/Launch Share intent
        startActivity(intent)

    }

    private fun logging(user_id: String, action: String){
        RetrofitClient.instance.createLog(
            user_id, action
        ).enqueue(object : Callback<LogData> {
            override fun onFailure(call: Call<LogData?>, t: Throwable) {
//                Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<LogData?>, response: Response<LogData?>) {
                if (response.code() == 200) {
                } else {
                }
            }
        })
    }

}
