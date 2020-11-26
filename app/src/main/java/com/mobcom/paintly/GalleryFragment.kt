package com.mobcom.paintly

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class GalleryFragment : Fragment() {
    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_gallery, container, false)

        val sharedPreferences = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences?.getString("email", "").toString()
        galleryGet(email)

        return mView
    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as BottomNavActivity)
            .setActionBarTitle("Your Artwork Gallery")
    }

    private fun galleryGet(email: String) {
        mView.progress_bar_gallery.visibility = View.VISIBLE
        RetrofitClient.instance.getGallery(
            email,
        ).enqueue(object : Callback<List<GalleryData>> {
            override fun onResponse(call: Call<List<GalleryData>>, response: Response<List<GalleryData>>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val galleryAdapter = GalleryAdapter(response.body()!!,
                            mView.context,
                            object : GalleryAdapter.ClickListener {
                                override fun onClick(file_result: String?) {
                                    Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show()
//                                    val arguments = Bundle()
//                                    arguments.putString("imageBitmap", convertBitmapToBase64(imageBitmap))
//                                    val fragment = GalleryInfoDialog()
//                                    fragment.arguments = arguments
//                                    fragment.show(childFragmentManager, "ArtworkInfo")
                                }
                        })
                        mView.rv_gallery.adapter = galleryAdapter
                        mView.rv_gallery.layoutManager = LinearLayoutManager(context)
                        mView.progress_bar_gallery.visibility = View.GONE
                    }
                } else {
                    mView.empty.visibility = View.VISIBLE
                    mView.progress_bar_gallery.visibility = View.GONE
                    mView.rv_gallery.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<List<GalleryData>>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}