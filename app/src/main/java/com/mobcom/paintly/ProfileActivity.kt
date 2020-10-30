package com.mobcom.paintly

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {
    private lateinit var tv_name: TextView
    private lateinit var tv_email: TextView
    private lateinit var tv_created_at: TextView
    private lateinit var tv_number_artwork: TextView
    private lateinit var about_app_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tv_name = name
        tv_email = email
        tv_created_at = created_at
        tv_number_artwork = number_artwork
        about_app_button = button_about

        val aboutApp = AboutAppDialog()

        getUser("septilusianna19@gmail.com")

        about_app_button.setOnClickListener(){
            aboutApp.show(supportFragmentManager, "About App")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.button_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
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
                    tv_created_at.setText("Joined At: " + response.body()?.created_at?.split(" ")?.get(0))
                    tv_number_artwork.setText("Number of Artwork: " + response.body()?.edit_freq)
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
        Toast.makeText(applicationContext, "Logout", Toast.LENGTH_SHORT)
            .show()
    }
}
