package com.mobcom.paintly

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.view.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    lateinit var mView: View
    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)

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
                val fragment = RegisterFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
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
        // validasi email input dan password input user
        if(!validateEmail() || !validatePassword()){
            return
        }

        // Implementasi Login
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    if (password == response.body()?.password) {
                        Toast.makeText(activity, "Login Success!", Toast.LENGTH_SHORT).show()
                        saveData(email)
                        goToApp()
                    } else {
                        Toast.makeText(activity, "Password Tidak Sesuai!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun goToApp() {
        val intent = Intent(activity, BottomNavActivity::class.java)
        startActivity(intent)
        activity?.finish()
        CustomIntent.customType(activity, "fadein-to-fadeout")
    }

    fun saveData(email: String) {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(EMAIL, email)
        editor?.apply()
    }
}