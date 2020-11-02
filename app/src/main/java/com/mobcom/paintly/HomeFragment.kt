package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.view.*

class HomeFragment : Fragment() {
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.activity_home, container, false)
        val UploadSheetFragment = UploadSheetFragment()
        mView.btn_starryNight.setOnClickListener {

            UploadSheetFragment.show(this.childFragmentManager, "UploadSheetDialog")
        }
        return mView
    }
<<<<<<< HEAD:app/src/main/java/com/mobcom/paintly/Home.kt
=======

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
    }


>>>>>>> 7071c8d6d229533ff3077ed4976d79184a94f1a2:app/src/main/java/com/mobcom/paintly/HomeFragment.kt
}


