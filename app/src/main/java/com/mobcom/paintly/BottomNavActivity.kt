package com.mobcom.paintly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnav)

        val homeFragment = Home()
        val galleryFragment = Gallery()
        val profileFragment = Profile()
        val username = intent.getStringExtra("EXTRA_USERNAME")

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


    override fun onStart() {
        super.onStart()
        Log.d("BottomNavBar Activity", "ON START")
    }

    override fun onResume() {
        super.onResume()
        Log.d("BottomNavBar Activity", "ON RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.d("BottomNavBar Activity", "ON PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.d("BottomNavBar Activity", "ON STOP")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("BottomNavBar Activity", "ON RESTART")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BottomNavBar Activity", "ON DESTROY")
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
}