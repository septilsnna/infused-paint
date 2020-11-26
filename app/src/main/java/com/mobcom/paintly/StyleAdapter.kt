package com.mobcom.paintly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deeparteffects.sdk.android.model.Styles
import com.mobcom.paintly.StyleAdapter.ViewHolder
import kotlinx.android.synthetic.main.style_item.view.*

class StyleAdapter internal constructor(// StyleAdapter berbentuk RecyclerView.Adapter (langsung di set)
    private val mContext: Context,
    private val mStyles: List<StyleData>,
//    private val mStyles: Styles,
    private val mClickListener: ClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        // ViewHolder berbentuk RecyclerView.ViewHolder (langsung di set)
        var styleImage: ImageView?
        var img_title: TextView
        override fun onClick(view: View) {
            if (styleImage == null) {
                return
            }
            mClickListener.onClick(mStyles[adapterPosition].style_id)
        }

        init {
            styleImage = view.findViewById(R.id.btn_style) // image view dari item_style.xml
            img_title = view.img_title
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = mStyles[position].image_file
        val title = mStyles[position].title
        Glide.with(mContext).load(imageUrl).centerCrop().into(holder.styleImage!!)
        holder.img_title.text = title
    }

    override fun getItemCount(): Int {
        return mStyles.size
    }

    interface ClickListener {
        fun onClick(styleId: String?)
    }
}
