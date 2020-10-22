package com.mobcom.paintly

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import maes.tech.intentanim.CustomIntent
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobcom.paintly.R.id.btn_starryNight
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.layout_upload.*

lateinit var mView: View
class Home : Fragment() {
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
}


