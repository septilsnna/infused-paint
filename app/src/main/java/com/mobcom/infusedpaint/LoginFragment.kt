package com.mobcom.infusedpaint

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.view.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class LoginFragment : Fragment() {
    lateinit var mView: View
    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"
    val QUOTA = "quota"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)

        val button = mView.login_button
        val email = mView.email_input
        val password = mView.password_input
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // validasi email input dan password input user
                if(!validateEmail() || !validatePassword()){
                    return
                }
                Toast.makeText(activity, "Please wait, we logging you in :)", Toast.LENGTH_SHORT).show()
                getUser(
                    email.text.toString(),
                    password.text.toString()
                )
            }
        })

        // google login
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
//        val googleLoginBtn = mView.google_login_button
//        googleLoginBtn.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                val signInIntent = mGoogleSignInClient.signInIntent
//                startActivityForResult(signInIntent, 111)
//            }
//        })

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 111) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            val acct = GoogleSignIn.getLastSignedInAccount(activity)
//            if (acct != null) {
//                val personEmail = acct.email
//                val personId = acct.id
//                Toast.makeText(activity, "Please wait, we logging you in", Toast.LENGTH_SHORT).show()
//                getUser(personEmail!!, personId!!)
//            }
//
//        } catch (e: ApiException) {
//            Toast.makeText(activity, "Failed to login, please check your connection", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun validateEmail()
            : Boolean {
        val v_email = mView.email_input.text.toString()
        if(v_email.isEmpty()){
            Toast.makeText(activity, "Sorry, email can't be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
            Toast.makeText(activity, "Your email is not valid", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun validatePassword()
            : Boolean {
        val v_password = mView.password_input.text.toString()
        if(v_password.isEmpty()){
            Toast.makeText(activity, "Sorry, password can't be empty", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun getUser(email: String, password: String) {
        // Implementasi Login
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, "Failed to login, please check your connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    if (password == response.body()?.password) {
                        Toast.makeText(activity, "Login Success!", Toast.LENGTH_SHORT).show()
                        saveData(response.body()!!.username!!, email, response.body()!!.quota_today!!)
                        goToApp()
                    } else {
                        Toast.makeText(activity, "Your password is wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Login is failed, please check your email and password", Toast.LENGTH_SHORT).show()
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

    fun saveData(user_id: String, email: String, quota: Int) {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val date = sharedPreferences?.getString("date", "")
        if (date == null) {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            val date_today = dateFormat.format(calendar.time)
            editor?.putString("date", date_today)
        }
        editor?.putString(EMAIL, email)
        editor?.putString("USER_ID", user_id)
        editor?.putInt(QUOTA, quota)
        editor?.apply()

        // logging
        logging(user_id, "Masuk ke akun")
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