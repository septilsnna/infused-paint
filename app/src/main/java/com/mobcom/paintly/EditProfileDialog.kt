package com.mobcom.paintly

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class EditProfileDialog : AppCompatDialogFragment() {
    lateinit var mView: View
    lateinit var imageBitmap: Bitmap
    lateinit var imageString: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_edit_profile, null)

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val emaill = sharedPreferences?.getString(EMAIL, "").toString()
        mView.profile_image_edit.visibility = View.INVISIBLE
        mView.progress_edit.visibility = View.VISIBLE
        mView.save_btn.visibility = View.GONE
        getUser(emaill)

        val save_btn = mView.save_btn

        save_btn.setOnClickListener(){
            mView.progress_edit.visibility = View.VISIBLE
            mView.save_btn.visibility = View.GONE
            updateUser(emaill)
        }

        val edit_foto_btn = mView.edit_foto_btn

        edit_foto_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
        })

        builder.setView(mView).setTitle("Edit Profile")
        return builder.create()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            imageBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                data!!.data,
                activity?.contentResolver!!, 768
            )!!

            imageString = convertBitmapToBase64(imageBitmap)!!

            mView.profile_image_edit.setImageBitmap(imageBitmap)
        }
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
                    mView.progress_edit.visibility = View.GONE
                    mView.save_btn.visibility = View.VISIBLE
                    mView.profile_image_edit.visibility = View.VISIBLE
                    mView.et_nama.setText(response.body()?.name)
                    mView.et_email.setText(response.body()?.email)
                    imageString = response.body()?.photo!!
//
                    val decodedString = Base64.decode(imageString, Base64.DEFAULT)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    mView.profile_image_edit.setImageBitmap(decodedByte)
                } else {
                    Toast.makeText(activity, "Failed to load user", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun updateUser(emaill: String) {
        RetrofitClient.instance.updateUser(
            emaill,
            mView.et_nama.text.toString(),
            mView.et_email.text.toString(),
            imageString
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    saveData(mView.et_email.text.toString())
                    Toast.makeText(activity, "Update Success!", Toast.LENGTH_SHORT).show()
                    mView.progress_edit.visibility = View.GONE
                    mView.save_btn.visibility = View.VISIBLE
                } else {
                    Toast.makeText(activity, "Failed to load user", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun saveData(email: String) {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(EMAIL, email)
        editor?.apply()
    }

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
