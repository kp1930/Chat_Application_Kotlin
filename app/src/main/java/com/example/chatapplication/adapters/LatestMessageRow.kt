package com.example.chatapplication.adapters

import android.content.Context
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.models.ChatMessage
import com.example.chatapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

/**
 * Created by Kush Pandya on 8/6/2019.
 */
class LatestMessageRow(private val chatMessage: ChatMessage, private val context: Context): Item<ViewHolder>() {
    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewLatestMessage.text = chatMessage.text

        val chatPartnerId: String = if (chatMessage.fromId == FirebaseAuth.getInstance().uid)
            chatMessage.toId
        else
            chatMessage.fromId

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                viewHolder.itemView.textViewUsername.text = chatPartnerUser?.username
                Glide.with(context).load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.imageViewUser)
            }
        })
    }
}