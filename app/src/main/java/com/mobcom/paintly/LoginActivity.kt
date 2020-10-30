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
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button = login_button
        email = email_input
        password = password_input
        button.setOnClickListener(){
            getUser(
                email.text.toString(),
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
        setResult(1)
        super.onStop()
    }

    override fun onDestroy() {
        setResult(1)
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

    private fun getUser(email: String, password: String) {
//        if(!validateEmail() || !validatePassword()){
//            return
//        }

        // Implementasi Backend Login

        val intent = Intent(this, BottomNavActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
        finish()
    }

    fun goToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

}