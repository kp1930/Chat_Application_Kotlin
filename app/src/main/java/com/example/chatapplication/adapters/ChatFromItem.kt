package com.example.chatapplication.adapters

import android.content.Context
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.models.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

/**
 * Created by Kush Pandya on 8/6/2019.
 */
class ChatFromItem(private val text:String, private val user: User, private val context: Context): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatTo.text = text
        Glide.with(context).load(user.profileImageUrl).into(viewHolder.itemView.imageViewChatTo)
    }
}