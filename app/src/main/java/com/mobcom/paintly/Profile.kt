package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment(){
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_profile, container, false)
        getUser("namjoohyuk@gmail.com")
        return mView
    }

    private fun getUser(email: String) {
        RetrofitClient.instance.getUser(
            email,
        ).enqueue(object : Callback<UserGet?> {
            override fun onFailure(call: Call<UserGet?>, t: Throwable) {
//                Toast.makeText(this@Profile, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UserGet?>, response: Response<UserGet?>) {
                if (response.code() == 200) {
                    mView.name.setText(response.body()?.name)
                    mView.email.setText(response.body()?.email)
                    mView.created_at.setText(response.body()?.created_at)
                } else {
//                    Toast.makeText(this@Profile, "Failed to load user", Toast.LENGTH_SHORT)
//                        .show()
                }
            }

        })
    }


}