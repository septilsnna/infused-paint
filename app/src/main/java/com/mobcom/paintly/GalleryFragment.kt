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
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
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
        Thread {
            RetrofitClient.instance.getGallery(
                email,
            ).enqueue(object : Callback<List<GalleryData>> {
                override fun onResponse(
                    call: Call<List<GalleryData>>,
                    response: Response<List<GalleryData>>
                ) {
                    if (response.code() == 200) {
                        mView.rv_gallery.visibility = View.VISIBLE
                        if (response.body() != null) {
                            val galleryAdapter = GalleryAdapter(response.body()!!,
                                mView.context,
                                object : GalleryAdapter.ClickListener {
                                    override fun onClick(file_result: String?) {
                                        val arguments = Bundle()
                                        arguments.putString("file_result", file_result)
                                        val fragment = GalleryInfoDialog()
                                        fragment.arguments = arguments
                                        fragment.show(childFragmentManager, "ArtworkInfo")
                                    }
                                })
                                mView.rv_gallery.adapter = galleryAdapter
                                mView.rv_gallery.layoutManager = GridLayoutManager(mView.context, 3)
                                mView.progress_bar_gallery.visibility = View.GONE
                        }
                    } else {
                        mView.empty.visibility = View.VISIBLE
                        mView.progress_bar_gallery.visibility = View.GONE
                        mView.rv_gallery.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<GalleryData>>, t: Throwable) {
                    mView.failed_gallery.visibility = View.VISIBLE
                    mView.empty.visibility = View.GONE
                    mView.progress_bar_gallery.visibility = View.GONE
                    mView.rv_gallery.visibility = View.GONE
                }
            })
            runOnUiThread {  }
        }.start()
    }

}