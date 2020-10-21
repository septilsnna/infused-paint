package com.mobcom.paintly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var button: Button
//    val PREFS_NAME = "RegisterSP"
//    val KEY_USERNAME = "key.username"
//    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        button = findViewById(R.id.register_button)
        val email = findViewById<EditText>(R.id.email_input)
        val password = findViewById<EditText>(R.id.password_input)
        val username = findViewById<EditText>(R.id.email_input)
        val name = findViewById<EditText>(R.id.email_input)
        button.setOnClickListener(){
            createUser(
                username.text.toString().split("@").get(0),
                password.text.toString(),
                name.text.toString().split(
                    "@"
                ).get(0),
                email.text.toString()
            )
        }
    }

    override fun finish() {
        super.finish()
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun validateEmail()
            : Boolean {
        val v_email = findViewById<EditText>(R.id.email_input).text.toString()
        if(v_email.isEmpty()){
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun validatePassword()
            : Boolean {
        val v_password = findViewById<EditText>(R.id.password_input).text.toString()
        val v_confirm_password = findViewById<EditText>(R.id.confirm_password_input).text.toString()
        if(v_password.isEmpty()){
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(v_confirm_password.isEmpty()){
            Toast.makeText(this, "Konfirmasi Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_password.matches(Regex("^.{8,}$"))){
            Toast.makeText(this, "Password harus terdiri lebih dari 8 karakter", Toast.LENGTH_SHORT).show()
            return false
        }
//        else if(!v_password.matches(Regex("^(?=\\s+$)$"))){
//            Toast.makeText(this, "Password tidak boleh terdapat spasi", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[0-9])$"))){
//            Toast.makeText(this, "Password harus terdiri dari minimal 1 angka", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[a-z])$"))){
//            Toast.makeText(this, "Password harus terdiri dari minimal 1 huruf kecil", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[A-Z])$"))){
//            Toast.makeText(this, "Password harus terdiri dari minimal 1 huruf besar", Toast.LENGTH_SHORT).show()
//            return false
//        }
        else if(v_password != v_confirm_password) {
            Toast.makeText(this, "Password konfirmasi tidak sesuai", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun createUser(username: String, password: String, name: String, email: String) {
        if(!validateEmail() || !validatePassword()){
            return
        }

//        RetrofitClient.instance.createUser(
//            username,
//            password,
//            name,
//            email,
//            0,
//            0
//        ).enqueue(object : Callback<UserCreateResponse> {
//            override fun onFailure(call: Call<UserCreateResponse>, t: Throwable) {
//                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
//            }
//            override fun onResponse(call: Call<UserCreateResponse>,response: Response<UserCreateResponse>) {
//                if (response.code() == 200) {
//                    Toast.makeText(this@RegisterActivity, "Register Success!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@RegisterActivity, "Register Failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        editor.putString(KEY_USERNAME, username)
//        editor.apply()

        val intent = Intent(this, BottomNavActivity::class.java)

        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
        finish()

    }

    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}