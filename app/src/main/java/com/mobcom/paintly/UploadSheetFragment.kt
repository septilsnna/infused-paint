package com.mobcom.paintly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_upload.*

class UploadSheetFragment: BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_uploadCamera.setOnClickListener(){
            Toast.makeText(context, "camera", Toast.LENGTH_SHORT).show()
        }

        btn_uploadCamera.setOnClickListener(){
            Toast.makeText(context, "gallery", Toast.LENGTH_SHORT).show()
        }
    }
}



