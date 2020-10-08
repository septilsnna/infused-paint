package com.mobcom.paintly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    setContent("Home")
                    true
                }
                R.id.menu_gallery -> {
                    setContent("Notification")
                    true
                }
                R.id.menu_profile -> {
                    setContent("Search")
                    true
                }
                else -> false
            }
        }
    }
    private fun setContent(content: String) {
        setTitle(content)
        tvLabel.text = content
    }
}