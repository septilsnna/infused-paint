package com.mobcom.paintly
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.Button
//import androidx.recyclerview.widget.LinearLayoutManager
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class GalleryActvity : Activity() {
//    private lateinit var button: Button
//    private lateinit var Api : Api
//    private lateinit var gallery : List<GalleryData>
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_gallery)
//
////        val exampleList = getDummyList(10)
//        val galleryList = getGalleryList()
//
//        recyclerview.adapter = GalleryAdapter(galleryList)
//        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.setHasFixedSize(true)
//    }
//
//    private fun getGalleryList() : List<GalleryData>{
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("https://www.infused-paint-web-services.pikupa.id/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        Api = retrofit.create(Api::class.java)
//
//        val call : Call<List<GalleryData>> = Api.getGallery("septilsnna")
//
//        call.enqueue(object : Callback<List<GalleryData>> {
//            override fun onResponse(call: Call<List<GalleryData>>, response: Response<List<GalleryData>>) {
//                gallery = response.body()!!
//            }
//            override fun onFailure(call: Call<List<GalleryData>>, t: Throwable) {
//
//            }
//        })
//
//        return gallery
//    }
//
////    private fun getDummyList(size: Int) : List<GalleryGet> {
////        val list = ArrayList<GalleryGet>()
////
////        for (i in 0 until size) {
////            val drawable = when(i % 3) {
////                0 -> R.drawable.picture_1
////                1 -> R.drawable.picture_2
////                else -> R.drawable.picture_3
////            }
////
////            val item = GalleryGet(drawable)
////            list += item
////        }
////
////        return list
////    }
//
//}