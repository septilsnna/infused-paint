package com.mobcom.paintly

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryAdapter(
    private val galleryList: List<GalleryData>,
    private val mContext: Context,
//    private val mClickListener: StyleAdapter.ClickListener
) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item,
            parent, false)

        return GalleryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentItem = galleryList[position]

        val decodedString = Base64.decode(currentItem.file_result, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        Glide.with(mContext).load(decodedByte).centerInside().into(holder.resultImage)
    }

    override fun getItemCount() = galleryList.size

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resultImage = itemView.image_result_item
    }
}