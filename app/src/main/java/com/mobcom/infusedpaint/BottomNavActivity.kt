package com.mobcom.infusedpaint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_bottomnav.*

class BottomNavActivity : AppCompatActivity() {
    private lateinit var email: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnav)

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        email = sharedPreferences?.getString(EMAIL, "").toString()
        if(email.isEmpty()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
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
}