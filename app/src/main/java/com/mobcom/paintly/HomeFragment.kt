package com.mobcom.paintly

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Pick Your Style")
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImage : Uri? = data?.data


            val image_stream =
                selectedImage?.let { context?.getContentResolver()?.openInputStream(it) };
            val getBitmap = BitmapFactory.decodeStream(image_stream);
            img_result.setImageBitmap(getBitmap)

        }
    }
}


