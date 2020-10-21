package com.mobcom.paintly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var button: Button
//    val EXTRA_USERNAME = "com.mobcom.paintly.USERNAME"
//    val PREFS_NAME = "RegisterSP"
//    val KEY_USERNAME = "key.username"
//    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        button = findViewById(R.id.login_button)
        val email = findViewById<EditText>(R.id.email_input)
        val password = findViewById<EditText>(R.id.password_input)
        button.setOnClickListener(){
            getUser(
                email.text.toString().split("@").get(0),
                password.text.toString()
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
        if(v_password.isEmpty()){
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun getUser(username: String, password: String) {
        if(!validateEmail() || !validatePassword()){
            return
        }

//        RetrofitClient.instance.getUser(
//            username
//        ).enqueue(object : Callback<Void> {
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//
//            } override fun onResponse(call: Call<Void>, response: Response<Void>) {
//
//            }
//        })


        val intent = Intent(this, BottomNavActivity::class.java)
//            .apply {
//            putExtra(EXTRA_USERNAME, username)
//        }
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
        finish()
    }

    fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

}