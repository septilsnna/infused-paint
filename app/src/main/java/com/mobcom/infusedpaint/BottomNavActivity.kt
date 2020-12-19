package com.mobcom.infusedpaint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_bottomnav.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomNavActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var user_id: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnav)

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        email = sharedPreferences?.getString(EMAIL, "").toString()
        user_id = sharedPreferences?.getString("USER_ID", "").toString()
        if(email.isEmpty()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // logging
            logging(user_id, "Membuka aplikasi")

            val homeFragment = HomeFragment()
            val galleryFragment = GalleryFragment()
            val profileFragment = ProfileFragment()

            setCurrentFragment(homeFragment)

            bottomNav.setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.menu_home -> setCurrentFragment(homeFragment)
                    R.id.menu_gallery -> setCurrentFragment(galleryFragment)
                    R.id.menu_profile -> setCurrentFragment(profileFragment)
                }
                true
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onDestroy() {
        if(user_id.isNotEmpty()) {
            // logging
            logging(user_id, "Menutup aplikasi")
        }
        super.onDestroy()
    }

    private fun logging(user_id: String, action: String){
        RetrofitClient.instance.createLog(
            user_id, action
        ).enqueue(object : Callback<LogData> {
            override fun onFailure(call: Call<LogData?>, t: Throwable) {
//                Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<LogData?>, response: Response<LogData?>) {
                if (response.code() == 200) {
                } else {
                }
            }
        })
    }
}