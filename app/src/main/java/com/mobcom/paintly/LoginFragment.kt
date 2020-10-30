package com.mobcom.paintly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_login.view.*
import maes.tech.intentanim.CustomIntent


class LoginFragment : Fragment() {
    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_login, container, false)

        val button = mView.login_button
        val email = mView.email_input
        val password = mView.password_input
        button.setOnClickListener(){
            getUser(
                email.text.toString(),
                password.text.toString()
            )
        }

        mView.don_t_have_.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val fragment: Fragment = RegisterFragment()
                val fragmentManager: FragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fl_start, fragment)
                fragmentTransaction.disallowAddToBackStack()
                fragmentTransaction.commit()
            }
        })

        return mView
    }

    private fun validateEmail()
            : Boolean {
        val v_email = mView.email_input.text.toString()
        if(v_email.isEmpty()){
            Toast.makeText(activity, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
            Toast.makeText(activity, "Email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun validatePassword()
            : Boolean {
        val v_password = mView.password_input.text.toString()
        if(v_password.isEmpty()){
            Toast.makeText(activity, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun getUser(email: String, password: String) {
//        if(!validateEmail() || !validatePassword()){
//            return
//        }

        // Implementasi Backend Login

        val intent = Intent(activity, BottomNavActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(activity, "fadein-to-fadeout")
        (activity as Activity?)!!.overridePendingTransition(0, 0)
//        finish()
    }
}