package com.mobcom.paintly

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mvp.handyopinion.URIPathHelper
import kotlinx.android.synthetic.main.afterchoosingimage.*
import kotlinx.android.synthetic.main.layout_upload.*
import maes.tech.intentanim.CustomIntent


class UploadSheetFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_upload, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_uploadCamera.setOnClickListener() {
            Toast.makeText(context, "camera", Toast.LENGTH_SHORT).show()
        }

        btn_uploadGallery.setOnClickListener() {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                ImagePicker.create(this) // Activity or Fragment
                    .toolbarFolderTitle("Folder") // folder selection title
                    .single() // single mode
                    .returnMode(ReturnMode.ALL)
                    .start();

                val intent = Intent(activity, AfterUploadActivity::class.java)
                startActivity(intent)
        }



    }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                val image : com.esafirm.imagepicker.model.Image = ImagePicker.getFirstImageOrNull(data)
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
}

