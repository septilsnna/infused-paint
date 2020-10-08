package com.mobcom.paintly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnav)

        val homeFragment = Home()
        val galleryFragment = Gallery()
        val profileFragment = Profile()

        setCurretFragment(homeFragment)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> setCurretFragment(homeFragment)
                R.id.menu_gallery -> setCurretFragment(galleryFragment)
                R.id.menu_profile -> setCurretFragment(profileFragment)
            }
            true
        }
    }

    private fun setCurretFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
}