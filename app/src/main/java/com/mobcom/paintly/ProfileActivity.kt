package com.mobcom.paintly

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.email
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var tv_name: TextView
    private lateinit var tv_email: TextView
    private lateinit var tv_created_at: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tv_name = name
        tv_email = email
        tv_created_at = created_at

        getUser("namjoohyuk@gmail.com")
    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserGet?> {
            override fun onFailure(call: Call<UserGet?>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserGet?>, response: Response<UserGet?>) {
                if (response.code() == 200) {
                    tv_name.setText(response.body()?.name)
                    tv_email.setText(response.body()?.email)
                    tv_created_at.setText(response.body()?.created_at)
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to load user", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })
    }

    private fun editProfile() {

    }

    private fun aboutApp() {

    }

    private fun sendFeedback() {

    }

    private fun logout() {

    }
}
