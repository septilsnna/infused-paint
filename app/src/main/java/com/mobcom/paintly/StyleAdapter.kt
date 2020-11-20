package com.mobcom.paintly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deeparteffects.sdk.android.model.Styles
import com.mobcom.paintly.StyleAdapter.ViewHolder

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.style_item.view.*
//
//class StyleAdapter(private val styleList: ArrayList<StyleItem>, private val listener: OnItemClickListener) :
//    RecyclerView.Adapter<StyleAdapter.StyleViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.style_item,
//            parent, false)
//
//        return StyleViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: StyleViewHolder, position: Int) {
//        val currentItem = styleList[position]
//
//        holder.imageView.setImageResource(currentItem.imageResource)
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2
//    }
//
//    override fun getItemCount() = styleList.size
//
//    inner class StyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        val imageView: ImageView = itemView.btn_style
//        val textView1: TextView = itemView.img_title
//        val textView2: TextView = itemView.img_author
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                listener.onItemClick(position)
//            }
//        }
//    }
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//}

class StyleAdapter internal constructor(// StyleAdapter berbentuk RecyclerView.Adapter (langsung di set)
    private val mContext: Context,
    private val mStyles: Styles,
    private val mClickListener: ClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        // ViewHolder berbentuk RecyclerView.ViewHolder (langsung di set)
        var styleImage: ImageView?
        override fun onClick(view: View) {
            if (styleImage == null) {
                return
            }
            mClickListener.onClick(mStyles[adapterPosition].id)
        }

        init {
            styleImage = view.findViewById(R.id.btn_style) // image view dari item_style.xml
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = mStyles[position].url
        Glide.with(mContext).load(imageUrl).centerCrop().into(holder.styleImage!!)
    }

    override fun getItemCount(): Int {
        return mStyles.size
    }

    interface ClickListener {
        fun onClick(styleId: String?)
    }
}
