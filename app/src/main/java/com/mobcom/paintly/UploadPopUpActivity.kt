package com.mobcom.paintly

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_home.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment



class UploadPopUpActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val UploadSheetFragment = UploadSheetFragment()

        btn_starryNight.setOnClickListener {
            UploadSheetFragment.show(supportFragmentManager, "UploadSheetDialog")
        }
    }
}