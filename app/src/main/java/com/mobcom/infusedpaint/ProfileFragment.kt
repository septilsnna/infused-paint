package com.mobcom.infusedpaint

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amazonaws.mobileconnectors.apigateway.ApiClientException
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_profile.view.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment(){
    lateinit var mView: View
    private lateinit var edit_profile_button: Button
    private lateinit var about_app_button: Button
    private lateinit var send_feedback_button: Button
    private lateinit var app_version_button: Button
    private lateinit var email: String
    private lateinit var user_id: String
//    private lateinit var mGoogleSignInClient: GoogleSignInClient

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_profile, container, false)

        try {
            edit_profile_button = mView.editprofile_button
            edit_profile_button.setOnClickListener {
                val intent = Intent(activity, EditProfileActivity::class.java)
                startActivity(intent)
                CustomIntent.customType(activity, "fadein-to-fadeout")
            }

            about_app_button = mView.button_about
            about_app_button.setOnClickListener {
                AboutAppDialog().show(this.childFragmentManager, "About App")
            }

            send_feedback_button = mView.button_sendfeedback
            send_feedback_button.setOnClickListener {
                AppRate.with(activity)
                    .setTitle("Rate Infused Paint")
                    .showRateDialog(activity)
            }

            app_version_button = mView.button_app_version
            app_version_button.setOnClickListener {
                Toast.makeText(activity, "App Version 1.0.0", Toast.LENGTH_SHORT).show()
            }

            // google auth for logout
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        } catch (e: ApiClientException) {
            val intent = Intent(activity, BottomNavActivity::class.java)
            startActivity(intent)
            activity?.finish()
            CustomIntent.customType(activity, "fadein-to-fadeout")
        }

        setHasOptionsMenu(true)
        return mView
    }

    override fun onResume() {
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Profile")

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        email = sharedPreferences?.getString(EMAIL, "").toString()
        user_id = sharedPreferences?.getString("USER_ID", "").toString()
        getUser(email)

        // logging
        logging(user_id, "Melihat profil")

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.button_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUser(email: String) {
            RetrofitClient.instance.getUser(
                email,
            ).enqueue(object : Callback<UserData?> {
                override fun onFailure(call: Call<UserData?>, t: Throwable) {
                    mView.progress_profile.visibility = View.GONE
                    mView.info_profile.visibility = View.GONE
                    mView.failed_profile.visibility = View.VISIBLE
                }

                override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                    if (response.code() == 200) {
                        mView.progress_profile.visibility = View.GONE
                        mView.info_profile.visibility = View.VISIBLE
                        mView.name.text = response.body()?.name
                        mView.email.text = response.body()?.email
                        mView.created_at.text =
                            "Joined At: " + response.body()?.created_at?.split(" ")?.get(
                                0
                            )
                        mView.number_artwork.text =
                            "Number of Artwork: " + response.body()?.edit_freq

                        if (response.body()?.photo != "") {
                            val decodedString =
                                Base64.decode(response.body()?.photo, Base64.DEFAULT)
                            val decodedByte = BitmapFactory.decodeByteArray(
                                decodedString,
                                0,
                                decodedString.size
                            )
                            Glide.with(mView.context).load(decodedByte).centerInside()
                                .into(mView.profile_image)
                        }
                    } else {
                        mView.failed_profile.visibility = View.VISIBLE
                        mView.progress_profile.visibility = View.GONE
                    }
                }

            })
    }

    private fun logout() {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
//        mGoogleSignInClient.signOut()
        Toast.makeText(activity, "Logout Success!", Toast.LENGTH_SHORT).show()

        // logging
        logging(user_id, "Keluar dari akun")

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(calendar.time)
        editor?.putString("date", date)
        editor?.apply()

        val intent = Intent(activity, StartActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(activity, "fadein-to-fadeout")
        activity?.finish()
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