package com.example.chatapplication.adapters

import android.content.Context
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.models.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

/**
 * Created by Kush Pandya on 8/5/2019.
 */
class UserItem(val user:User, val context: Context): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewUsername.text = user.username
        Glide.with(context).load(user.profileImageUrl).into(viewHolder.itemView.imageViewUser)
    }
}