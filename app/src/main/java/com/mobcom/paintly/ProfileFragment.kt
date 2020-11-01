package com.mobcom.paintly

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(){
    lateinit var mView: View
    private lateinit var edit_profile_button: Button
    private lateinit var about_app_button: Button
    private lateinit var send_feedback_button: Button
    private lateinit var app_version_button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_profile, container, false)
        getUser("septilusianna19@gmail.com")

        edit_profile_button = mView.editprofile_button
        edit_profile_button.setOnClickListener(){
            Toast.makeText(activity, "Edit Profile Pressed!", Toast.LENGTH_SHORT).show()
        }

        about_app_button = mView.button_about
        about_app_button.setOnClickListener(){
            AboutAppDialog().show(this.childFragmentManager, "About App")
        }

        send_feedback_button = mView.button_sendfeedback
        send_feedback_button.setOnClickListener(){
            Toast.makeText(activity, "Ready to send feedback!", Toast.LENGTH_SHORT).show()
        }

        app_version_button = mView.button_app_version
        app_version_button.setOnClickListener(){
            Toast.makeText(activity, "Version app pressed!", Toast.LENGTH_SHORT).show()
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
        ).enqueue(object : Callback<UserGet?> {
            override fun onFailure(call: Call<UserGet?>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserGet?>, response: Response<UserGet?>) {
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
        Toast.makeText(activity, "Logout Pressed!", Toast.LENGTH_SHORT).show()
    }

}