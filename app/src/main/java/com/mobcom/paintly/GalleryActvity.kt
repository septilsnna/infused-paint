package com.mobcom.paintly

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_gallery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryActvity : Activity() {
    private lateinit var button: Button
    private lateinit var Api : Api
    private lateinit var gallery : List<GalleryGet>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

//        val exampleList = getDummyList(10)
        val galleryList = getGalleryList()

        recyclerview.adapter = GalleryAdapter(galleryList)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
    }

    private fun getGalleryList() : List<GalleryGet>{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.infused-paint-web-services.pikupa.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Api = retrofit.create(Api::class.java)

        val call : Call<List<GalleryGet>> = Api.getGallery("septilsnna")

        call.enqueue(object : Callback<List<GalleryGet>> {
            override fun onResponse(call: Call<List<GalleryGet>>, response: Response<List<GalleryGet>>) {
                gallery = response.body()!!
            }
            override fun onFailure(call: Call<List<GalleryGet>>, t: Throwable) {

            }
        })

        return gallery
    }

//    private fun getDummyList(size: Int) : List<GalleryGet> {
//        val list = ArrayList<GalleryGet>()
//
//        for (i in 0 until size) {
//            val drawable = when(i % 3) {
//                0 -> R.drawable.picture_1
//                1 -> R.drawable.picture_2
//                else -> R.drawable.picture_3
//            }
//
//            val item = GalleryGet(drawable)
//            list += item
//        }
//
//        return list
//    }

}