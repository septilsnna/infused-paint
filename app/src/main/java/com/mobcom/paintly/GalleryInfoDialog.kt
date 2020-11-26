package com.mobcom.paintly

import android.app.Activity
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
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_processing.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class GalleryInfoDialog : AppCompatDialogFragment() {
    lateinit var mView: View
    lateinit var userData: UserData

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_processing, null)

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val emaill = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(emaill)

        mView.processing.visibility = View.GONE
        mView.result.visibility = View.VISIBLE
        mView.horay.visibility = View.GONE

        val strImage = arguments?.getString("imageBitmap")
        val decodedString = Base64.decode(strImage, Base64.DEFAULT)
        val imageBitmap = BitmapFactory.decodeByteArray(
            decodedString,
            0,
            decodedString.size
        )
        Glide.with(this).load(imageBitmap).into(mView.result_image)

        mView.save_result.setOnClickListener {
            // Save image to gallery
            val savedImageURL = MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                imageBitmap,
                "result_image" +  "_" + userData.edit_freq!!.plus(1) + ".jpg",
                "Image of " + "result_image" +  "_" + userData.edit_freq!!.plus(1) + ".jpg"
            )
            Uri.parse(savedImageURL)

            mView.save_result.isEnabled = false
            Toast.makeText(activity, "Save Success!", Toast.LENGTH_SHORT).show()
        }

        builder.setView(mView).setTitle("Artwork Info")
        return builder.create()

    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    userData = response.body()!!
                } else {
                }
            }

        })
    }
}
