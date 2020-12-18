package com.mobcom.infusedpaint

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_landing3.view.*
import maes.tech.intentanim.CustomIntent

class Landing3Fragment : Fragment() {
    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_landing3, container, false)

        val button = mView.button_ready

        button.setOnClickListener() {
            val intent = Intent(activity, StartActivity::class.java)
            startActivity(intent)
            activity?.finish()
            CustomIntent.customType(activity, "fadein-to-fadeout")
        }


        return mView
    }
}