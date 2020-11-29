package com.mobcom.paintly

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_upload.*
import maes.tech.intentanim.CustomIntent
import java.text.SimpleDateFormat
import java.util.*


class UploadSheetFragment : BottomSheetDialogFragment() {
    var styleId: String? = null
    lateinit var quota: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        styleId = arguments?.getString("styleId")
        val sharedPreferences =
            activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        quota = sharedPreferences?.getInt("quota", 0)!!.toInt().toString()
        return inflater.inflate(R.layout.layout_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_uploadCamera.setOnClickListener() {
            if (quota.toInt() > 0) {
                val intent = Intent(activity, AfterUploadActivity::class.java)
                intent.putExtra("MEDIA", "CAMERA")
                intent.putExtra("STYLE_ID", styleId)
                startActivity(intent)
                CustomIntent.customType(activity, "fadein-to-fadeout")
            } else {
                Toast.makeText(activity, "Ups, you reached your limit today, try again tomorrow", Toast.LENGTH_SHORT).show()
            }
        }

        btn_uploadGallery.setOnClickListener() {
            if (quota.toInt() > 0) {
                //check runtime permission
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    val intent = Intent(activity, AfterUploadActivity::class.java)
                    intent.putExtra("MEDIA", "GALLERY")
                    intent.putExtra("STYLE_ID", styleId)
                    startActivity(intent)
                    CustomIntent.customType(activity, "fadein-to-fadeout")
                }
            } else {
                Toast.makeText(activity, "Ups, you reached your limit today, try again tomorrow", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

