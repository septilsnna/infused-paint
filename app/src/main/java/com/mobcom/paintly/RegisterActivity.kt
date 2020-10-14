package com.mobcom.paintly

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button = findViewById(R.id.register_button)
        var email = findViewById<EditText>(R.id.email_input)
        var password = findViewById<EditText>(R.id.password_input)
        var cut = email.text.split("@")
        var username: String = cut.get(0)
        var name: String = cut.get(0)
        button.setOnClickListener(){
            createUser(username, password.text.toString(), name, email.text.toString())
        }
    }

    private fun createUser(username: String, password: String, name: String, email: String) {
        RetrofitClient.instance.createUser(
            username,
            password,
            name,
            email
        ).enqueue(object : Callback<UserCreateResponse> {
            override fun onFailure(call: Call<UserCreateResponse>, t: Throwable){
                view_email_input.text = t.message
            }
            override fun onResponse(
                call: Call<UserCreateResponse>,
                response: Response<UserCreateResponse>
            ) {
                val responseText: String = "Response code: ${response.code()} " +
                        "username: ${response.body()?.username} " +
                        "password: ${response.body()?.password} " +
                        "name: ${response.body()?.name} " +
                        "email: ${response.body()?.email} "
                view_email_input.text = responseText
            }
        })
    }
}