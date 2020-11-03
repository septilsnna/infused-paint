package com.mobcom.paintly

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_profile.view.*
import maes.tech.intentanim.CustomIntent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(){
    lateinit var mView: View
    private lateinit var edit_profile_button: Button
    private lateinit var about_app_button: Button
    private lateinit var send_feedback_button: Button
    private lateinit var app_version_button: Button
    private lateinit var email: String

    val SHARED_PREFS = "sharedPrefs"
    val EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        email = sharedPreferences?.getString(EMAIL, "").toString()
        getUser(email)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_profile, container, false)

        edit_profile_button = mView.editprofile_button
        edit_profile_button.setOnClickListener(){
            EditProfileDialog().show(this.childFragmentManager, "Edit Profile")
        }

        about_app_button = mView.button_about
        about_app_button.setOnClickListener(){
            AboutAppDialog().show(this.childFragmentManager, "About App")
        }

        send_feedback_button = mView.button_sendfeedback
        send_feedback_button.setOnClickListener(){
            AppRate.with(activity).showRateDialog(activity);
        }

        app_version_button = mView.button_app_version
        app_version_button.setOnClickListener(){
            Toast.makeText(activity, "App Version 1.0.0", Toast.LENGTH_SHORT).show()
        }

        setHasOptionsMenu(true)
        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Profile")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.button_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserData?> {
            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.code() == 200) {
                    mView.name.setText(response.body()?.name)
                    mView.email.setText(response.body()?.email)
                    mView.created_at.setText(
                        "Joined At: " + response.body()?.created_at?.split(" ")?.get(
                            0
                        )
                    )
                    mView.number_artwork.setText("Number of Artwork: " + response.body()?.edit_freq)
                } else {
                    Toast.makeText(activity, "Failed to load user", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun logout() {
        val sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        Toast.makeText(activity, "Logout Success!", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, StartActivity::class.java)
        startActivity(intent)
        CustomIntent.customType(activity, "fadein-to-fadeout")
        activity?.finish()
    }

}