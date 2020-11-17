package com.mobcom.paintly

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class EditProfileDialog : AppCompatDialogFragment() {
    lateinit var mView: View

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_edit_profile, null)

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

    override fun onResume() {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val emaill = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(emaill)

        val save_btn = mView.save_btn

        save_btn.setOnClickListener(){
            updateUser(emaill)
        }

        super.onResume()
    }

    private fun validateEmail()
            : Boolean {
        val v_email = mView.email_input.text.toString()
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

    private fun validatePassword()
            : Boolean {
        val v_password = mView.password_input.text.toString()
        val v_confirm_password = mView.confirm_password_input.text.toString()
        if(v_password.isEmpty()){
            Toast.makeText(activity, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(v_confirm_password.isEmpty()){
            Toast.makeText(activity, "Konfirmasi Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_password.matches(Regex("^.{8,}$"))){
            Toast.makeText(
                activity,
                "Password harus terdiri lebih dari 8 karakter",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
//        else if(!v_password.matches(Regex("^(?=\\s+$)$"))){
//            Toast.makeText(activity, "Password tidak boleh terdapat spasi", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[0-9])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 angka", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[a-z])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 huruf kecil", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[A-Z])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 huruf besar", Toast.LENGTH_SHORT).show()
//            return false
//        }
        else if(v_password != v_confirm_password) {
            Toast.makeText(activity, "Password konfirmasi tidak sesuai", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            mView.profile_image.setImageURI(data?.data)
        }
        if (requestCode==123)
        {
            var bmp= data?.extras?.get("data") as? Bitmap
            mView.profile_image.setImageBitmap(bmp)
        }
    }

//    private fun createTempFile(bitmap: Bitmap): File? {
//        val file = File(
//            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//            System.currentTimeMillis().toString() + "_image.webp"
//        )
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, bos)
//        val bitmapdata: ByteArray = bos.toByteArray()
//        //write the bytes in file
//        try {
//            val fos = FileOutputStream(file)
//            fos.write(bitmapdata)
//            fos.flush()
//            fos.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return file
//    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    mView.et_nama.setText(response.body()?.name)
                    mView.et_email.setText(response.body()?.email)
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
            mView.et_email.text.toString()
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    saveData(mView.et_email.text.toString())
                    Toast.makeText(activity, "Update Success!", Toast.LENGTH_SHORT).show()
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
}
