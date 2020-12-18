package com.mobcom.infusedpaint

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class EditProfileActivity : AppCompatActivity() {
    lateinit var imageBitmap: Bitmap
    lateinit var imageString: String
    lateinit var emaill: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_profile)

        val sharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emaill = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(emaill)

        profile_image_edit.visibility = View.INVISIBLE
        progress_edit.visibility = View.VISIBLE

        edit_foto_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

    }

    override fun finish() {
        super.finish()
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.mobcom.infusedpaint.R.menu.save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.mobcom.infusedpaint.R.id.button_update -> updateUser(emaill)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            imageBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                data!!.data,
                this.contentResolver!!, 768
            )!!

            imageString = convertBitmapToBase64(imageBitmap)!!

            Glide.with(this).load(imageBitmap).centerInside().into(profile_image_edit)
        }
    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                progress_edit.visibility = View.GONE
                profile_image_edit.visibility = View.VISIBLE
                failed_edit.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    progress_edit.visibility = View.GONE
                    profile_image_edit.visibility = View.VISIBLE
                    et_nama.setText(response.body()?.name)
                    et_email.setText(response.body()?.email)
                    imageString = response.body()?.photo!!

                    if (response.body()?.photo != "") {
                        val decodedString = Base64.decode(imageString, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(
                            decodedString,
                            0,
                            decodedString.size
                        )
                        Glide.with(applicationContext).load(decodedByte).centerInside().into(
                            profile_image_edit
                        )
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to load user", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })
    }

    private fun validateEmail()
            : Boolean {
        val v_email = et_email.text.toString()
        if(v_email.isEmpty()){
            Toast.makeText(applicationContext, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
            Toast.makeText(applicationContext, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun updateUser(emaill: String) {
        if (et_email.text.toString() != emaill){
            checkEmail(emaill, et_email.text.toString())
        } else {
            progress_edit.visibility = View.VISIBLE

            // validasi email input user
            if (!validateEmail()) {
                progress_edit.visibility = View.GONE
                return
            }

            // Implementasi Edit Profile
            RetrofitClient.instance.updateUser(
                emaill,
                et_nama.text.toString(),
                et_email.text.toString(),
                imageString
            ).enqueue(object : Callback<UserData?> {
                override fun onFailure(call: Call<UserData?>, t: Throwable) {
                    progress_edit.visibility = View.GONE
                    profile_image_edit.visibility = View.VISIBLE
                    failed_edit.visibility = View.VISIBLE
                    failed_edit.text = "Failed to update your profile,\nplease check your connection."
                }

                override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                    if (response.code() == 200) {
                        saveData(et_email.text.toString())
                        Toast.makeText(applicationContext, "Update Success!", Toast.LENGTH_SHORT)
                            .show()
                        progress_edit.visibility = View.GONE
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to load user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })
        }
    }

    fun saveData(email: String) {
        val sharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
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
                progress_edit.visibility = View.GONE
                profile_image_edit.visibility = View.VISIBLE
                failed_edit.visibility = View.VISIBLE
                failed_edit.text = "Failed to update your profile,\nplease check your connection."
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    Toast.makeText(applicationContext, "Email sudah digunakan", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    progress_edit.visibility = View.VISIBLE
                    updateUser(email)
                }
            }
        })
    }

}