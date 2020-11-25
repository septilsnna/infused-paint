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


class EditProfileDialog : AppCompatDialogFragment() {
    lateinit var mView: View
    lateinit var imageBitmap: Bitmap
    lateinit var imageString: String
    lateinit var emaill: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_edit_profile, null)

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emaill = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(emaill)

        mView.profile_image_edit.visibility = View.INVISIBLE
        mView.progress_edit.visibility = View.VISIBLE
        mView.save_btn.visibility = View.GONE

        val edit_foto_btn = mView.edit_foto_btn

        edit_foto_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
        })

        mView.save_btn.setOnClickListener(){
            if (mView.et_email.text.toString() != emaill){
                checkEmail(emaill, mView.et_email.text.toString())
            } else {
                mView.progress_edit.visibility = View.VISIBLE
                mView.save_btn.visibility = View.GONE
                updateUser(emaill)
            }
        }

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

            Glide.with(mView.context).load(imageBitmap).centerInside().into(mView.profile_image_edit)
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

                    if(response.body()?.photo != ""){
                        val decodedString = Base64.decode(imageString, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        Glide.with(mView.context).load(decodedByte).centerInside().into(mView.profile_image_edit)
                    }
                } else {
                    Toast.makeText(activity, "Failed to load user", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun validateEmail()
            : Boolean {
        val v_email = mView.et_email.text.toString()
        if(v_email.isEmpty()){
            Toast.makeText(activity, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
            Toast.makeText(activity, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun updateUser(emaill: String) {
        // validasi email input user
        if(!validateEmail()){
            mView.progress_edit.visibility = View.GONE
            mView.save_btn.visibility = View.VISIBLE
            return
        }

        // Implementasi Edit Profile
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
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 40, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun checkEmail(email: String, emailNew: String) {
        RetrofitClient.instance.getUser(
            emailNew,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    Toast.makeText(activity, "Email sudah digunakan", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    mView.progress_edit.visibility = View.VISIBLE
                    mView.save_btn.visibility = View.GONE
                    updateUser(email)
                }
            }
        })
    }
}
