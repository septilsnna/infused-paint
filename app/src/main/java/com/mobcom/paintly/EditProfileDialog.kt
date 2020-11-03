package com.mobcom.paintly

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileDialog : AppCompatDialogFragment() {
    lateinit var mView: View

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val emaill = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(emaill)

        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(R.layout.fragment_edit_profile, null)

        val edit_foto_btn = mView.edit_foto_btn
        val save_btn = mView.save_btn

        edit_foto_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
        })

        save_btn.setOnClickListener(){
            updateUser(mView.et_nama.text.toString(), mView.et_email.text.toString())
        }

        builder.setView(mView).setTitle("Edit Profile")
        return builder.create()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            mView.profile_image.setImageURI(data?.data)
        }

        if (requestCode==123)
        {
            var bmp= data?.extras?.get("data") as? Bitmap
            mView.profile_image.setImageBitmap(bmp)
        }
    }

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

    private fun updateUser(name: String, email: String) {
//         validasi email input user
        if(!validateEmail()){
            return
        }

        // implementasi backend update
        val fields: MutableMap<String, String> = HashMap()
        fields["name"] = name
        fields["email"] = email

        RetrofitClient.instance.updateUser(
            email,
            fields
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
//                    Toast.makeText(activity, response.body()?.name, Toast.LENGTH_SHORT).show()
//                    Toast.makeText(activity, response.body()?.email, Toast.LENGTH_SHORT).show()
//                    saveData(email)
                } else {
                    Toast.makeText(activity, "Failed to update user", Toast.LENGTH_SHORT).show()
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
