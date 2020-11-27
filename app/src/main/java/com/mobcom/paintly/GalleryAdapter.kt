package com.mobcom.paintly

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_item.view.*
import kotlinx.android.synthetic.main.style_item.view.*

class GalleryAdapter(
    private val galleryList: List<GalleryData>,
    private val mContext: Context,
    private val mClickListener: ClickListener
) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    inner class GalleryViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var resultImage: ImageView
        override fun onClick(view: View) {
            mClickListener.onClick(galleryList[adapterPosition].file_result)
        }

        init {
            resultImage = view.findViewById(R.id.image_result_item)
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item,
            parent, false)
        return GalleryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentItem = galleryList[position]

        val decodedString = Base64.decode(currentItem.file_result, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        Glide.with(mContext).load(decodedByte).centerCrop().into(holder.resultImage)
    }

    override fun getItemCount() = galleryList.size

    interface ClickListener {
        fun onClick(file_result: String?)
    }
}