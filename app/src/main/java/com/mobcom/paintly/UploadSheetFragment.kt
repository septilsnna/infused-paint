package com.mobcom.paintly

import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_upload.*
import maes.tech.intentanim.CustomIntent


class UploadSheetFragment : BottomSheetDialogFragment() {
    var styleId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        styleId = arguments?.getString("styleId")
        return inflater.inflate(R.layout.layout_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_uploadCamera.setOnClickListener() {
            val intent = Intent(activity, AfterUploadActivity::class.java)
            intent.putExtra("MEDIA", "CAMERA")
            intent.putExtra("STYLE_ID", styleId)
            startActivity(intent)
            CustomIntent.customType(activity, "fadein-to-fadeout")
        }

        btn_uploadGallery.setOnClickListener() {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                val intent = Intent(activity, AfterUploadActivity::class.java)
                intent.putExtra("MEDIA", "GALLERY")
                intent.putExtra("STYLE_ID", styleId)
                startActivity(intent)
                CustomIntent.customType(activity, "fadein-to-fadeout")
            }
        }
    }

}

