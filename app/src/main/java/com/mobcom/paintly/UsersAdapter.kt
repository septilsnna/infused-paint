package com.mobcom.paintly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter(private val list: ArrayList<UserData>): RecyclerView.Adapter<UsersAdapter.PostViewHolder>(){
    inner class PostViewHolder(itemViev: View): RecyclerView.ViewHolder(itemViev){
        fun bind(usersResponse: UserData){
            val text: String = "username: ${usersResponse.username}\n" +
                    "password: ${usersResponse.password}\n" +
                    "name: ${usersResponse.name}\n" +
                    "email: ${usersResponse.email}\n"
            // panggil recycler view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_login, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //holder.bind(list(position))
    }
}