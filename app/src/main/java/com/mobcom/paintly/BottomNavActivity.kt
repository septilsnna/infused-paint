package com.mobcom.paintly

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnav)

        val homeFragment = Home()
        val galleryFragment = Gallery()
        val profileFragment = Profile()

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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }
}