package com.mobcom.paintly

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.email_input
import kotlinx.android.synthetic.main.fragment_register.view.google_login_button
import kotlinx.android.synthetic.main.fragment_register.view.password_input
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {
    lateinit var mView: View
    lateinit var password: EditText
    lateinit var username: EditText
    lateinit var name: EditText
    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"
    val QUOTA = "quota"
    lateinit var calendar: Calendar
    lateinit var dateFormat: SimpleDateFormat
    val DATE = "date"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_register, container, false)

        val button = mView.register_button
        val email = mView.email_input
        password = mView.password_input
        username = mView.email_input
        name = mView.email_input

        button.setOnClickListener(){
            checkEmail(
                username.text.toString().split("@").get(0),
                password.text.toString(),
                name.text.toString().split(
                    "@"
                ).get(0),
                email.text.toString(),
                "email"
            )
        }

        // google register
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        val googleLoginBtn = mView.google_login_button
        googleLoginBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, 111)
            }
        })

        mView.already_hav.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val fragment = LoginFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fl_start, fragment)
                fragmentTransaction.disallowAddToBackStack()
                fragmentTransaction.commit()
            }
        })

        return mView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (acct != null) {
                val personName = acct.displayName
                val personEmail = acct.email
                val personId = acct.id
//                val personPhoto: Uri? = acct.photoUrl
                checkEmail(
                    personEmail!!.split("@").get(0),
                    personId!!,
                    personName!!,
                    personEmail,
                    "gmail"
                )
            }

        } catch (e: ApiException) {
            Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
        }
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
        val v_confirm_password = mView.confirm_password_input.text.toString()
        if(v_password.isEmpty()){
            Toast.makeText(activity, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(v_confirm_password.isEmpty()){
            Toast.makeText(activity, "Konfirmasi password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if(!v_password.matches(Regex("^.{8,}$"))){
            Toast.makeText(activity, "Password harus terdiri lebih dari 8 karakter", Toast.LENGTH_SHORT).show()
            return false
        }
//        else if(!v_password.matches(Regex("^(?=\\s+$)$"))){
//            Toast.makeText(activity, "Password tidak boleh terdapat spasi", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[0-9])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 angka", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[a-z])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 huruf kecil", Toast.LENGTH_SHORT).show()
//            return false
//        } else if(!v_password.matches(Regex("^(?=.*[A-Z])$"))){
//            Toast.makeText(activity, "Password harus terdiri dari minimal 1 huruf besar", Toast.LENGTH_SHORT).show()
//            return false
//        }
        else if(v_password != v_confirm_password) {
            Toast.makeText(activity, "Password konfirmasi tidak sesuai", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun createUser(username: String, password: String, name: String, email: String) {
        // Implementasi Register
        RetrofitClient.instance.createUser(
            username,
            password,
            name,
            email,
            5,
            0,
            0
        ).enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    Toast.makeText(activity, "Register Success!", Toast.LENGTH_SHORT).show()
                    saveData(email, 5)
                    goToApp()
                } else {
                    Toast.makeText(activity, "Register Failed!", Toast.LENGTH_SHORT).show()
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

    fun saveData(email: String, quota: Int) {
        calendar = Calendar.getInstance()
        dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(calendar.time)

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(EMAIL, email)
        editor?.putInt(QUOTA, quota)
        editor?.putString(DATE, date)
        editor?.apply()
    }

    fun checkEmail(username: String, password: String, name: String, email: String, res: String){
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, "Failed to register, please check your connection", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    Toast.makeText(activity, "Email sudah digunakan", Toast.LENGTH_SHORT).show()
                } else {
                    if (res == "email") {
                        if (!validateEmail() || !validatePassword()) {
                            return
                        }
                    }
                    createUser(username, password, name, email)
                }
            }
        })
    }
}