package com.mobcom.paintly

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mobcom.paintly.R.layout.afterchoosingimage
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.activity_profile.view.profile_image
import kotlinx.android.synthetic.main.afterchoosingimage.*
import kotlinx.android.synthetic.main.afterchoosingimage.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*


class AfterUploadFragment : Fragment() {
    var imageView: ImageView? = null
    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.afterchoosingimage, container, false)

        return mView
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(afterchoosingimage)
//
//        val intent = intent
//        val bitmap = intent.getParcelableExtra<Parcelable>("BitmapImage") as? Bitmap
//        val d: Drawable = BitmapDrawable(bitmap)
//
//        image_view.setImageDrawable(d)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        val photo = data?.getStringExtra(UploadSheetFragment.EXTRA_BITMAP_IMAGE)
//        if (resultCode == Activity.RESULT_OK && requestCode == 100){
//            mView.image_view.setImageURI(data?.data)
//        }
//        if (requestCode==123)
//        {
//            var bmp= data?.extras?.get("data") as? Bitmap
//            mView.image_view.setImageBitmap(bmp)
//        }
//    }
}
