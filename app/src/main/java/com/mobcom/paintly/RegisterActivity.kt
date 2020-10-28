package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var username: EditText
    private lateinit var name: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button = register_button
        email = email_input
        password = password_input
        username = email_input
        name = email_input

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

        RetrofitClient.instance.createUser(
            username,
            password,
            name,
            email,
            0,
            0
        ).enqueue(object : Callback<UserCreate?> {
            override fun onFailure(call: Call<UserCreate?>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<UserCreate?>,response: Response<UserCreate?>) {
                if (response.code() == 200) {
                    Toast.makeText(this@RegisterActivity, "Register Success!", Toast.LENGTH_SHORT).show()
                    goToApp()
                } else {
                    Toast.makeText(this@RegisterActivity, "Register Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun goToApp() {
        val intent = Intent(this, BottomNavActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
        finish()
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}