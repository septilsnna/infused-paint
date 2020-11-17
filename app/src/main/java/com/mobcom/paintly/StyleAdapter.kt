package com.mobcom.paintly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.style_item.view.*

class StyleAdapter(private val styleList: List<StyleItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<StyleAdapter.StyleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.style_item,
            parent, false)

        return StyleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StyleViewHolder, position: Int) {
        val currentItem = styleList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
    }

    override fun getItemCount() = styleList.size

    inner class StyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.btn_style
        val textView1: TextView = itemView.img_title
        val textView2: TextView = itemView.img_author

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}