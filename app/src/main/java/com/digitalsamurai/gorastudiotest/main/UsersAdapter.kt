package com.digitalsamurai.gorastudiotest.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.digitalsamurai.gorastudiotest.R
import com.digitalsamurai.gorastudiotest.User
import com.digitalsamurai.gorastudiotest.photos.PhotosActivity

class UsersAdapter(private var data: ArrayList<User>,
                   private val context : Context) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        with(LayoutInflater.from(context)){
            var view = this.inflate(R.layout.item_user,parent, false)
            return UsersViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.userTextView.setText(data.get(position).name)
        holder.c = context
        holder.userId = data.get(position).id
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var userTextView : TextView
        var mainLay : ConstraintLayout
        var userId  = -1
        lateinit var c : Context
        init {
            userTextView = itemView.findViewById(R.id.item_user_view_user)
            mainLay = itemView.findViewById(R.id.item_user_dataLay)
            mainLay.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context, PhotosActivity::class.java)
            intent.putExtra("userId", userId)
            p0?.context?.startActivity(intent)
        }
    }
}