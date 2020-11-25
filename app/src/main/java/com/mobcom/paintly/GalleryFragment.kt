package com.mobcom.paintly

import android.content.Context
import android.os.Bundle
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

class GalleryFragment : Fragment(){
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
                        val galleryAdapter = GalleryAdapter(response.body()!!, mView.context)
                        mView.rv_gallery.adapter = galleryAdapter
//                        mView.rv_gallery.layoutManager = GridLayoutManager(activity, 3)
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

}